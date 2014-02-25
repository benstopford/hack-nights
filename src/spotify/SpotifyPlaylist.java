package spotify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.Utils.fromJson;
import static util.Utils.wget;

/**
 * From Hack Night @RBS 24th Feb 2014
 *
 * Java team (Ben Stopford, Nick White, Richard Dickenson, Richard Crook)
 *
 * Problem: create a playlist as close as possible to a particular desired length using a search term:
 * For example: create a playlist 45 mins long on the subject of "love".
 * The closer you get to exactly 45 mins the better.
 *
 */
public class SpotifyPlaylist {

    public static void main(String[] args) throws IOException {
        List<Track> playlist = new ArrayList<Track>();

        int totalLengthWanted = 10 * 60;

        new SpotifyPlaylist().findPlaylist(playlist, 10 * 60, "love", 5);

        System.out.println("Tracks: " + playlist);
        System.out.printf("Expected:%s Actual:%s\n", totalLengthWanted, length(playlist));
    }

    private void findPlaylist(List<Track> playlist, int totalLengthWanted, String searchTerm, int limit) throws IOException {

        List<Track> spotifyTracks = getTracksFromSpotify(searchTerm, limit);

        for (Track newTrack : spotifyTracks) {
            if (combinedLength(playlist, newTrack) <= totalLengthWanted) {
                playlist.add(newTrack);
            } else {
                substituteIfBetterFit(totalLengthWanted, playlist, newTrack);
            }
        }
    }

    private List<Track> getTracksFromSpotify(String searchTerm, int limit) throws IOException {
        List<Track> spotifyTracks = new ArrayList<Track>();
        int page = 0;
        while (page < limit) {
            Map json = null;
            try {
                json = fromJson(wget("http://ws.spotify.com/search/1/track.json?q=" + searchTerm + "&page=" + page));
            } catch (Exception happensSometimes) {
                break;
            }
            List<Track> pageData = tracks(json);
            spotifyTracks.addAll(pageData);
            System.out.println(pageData);
            page++;
        }
        System.out.printf("Downloaded %s tracks from spotify\n", spotifyTracks.size());
        return spotifyTracks;
    }

    private void substituteIfBetterFit(int totalLengthWanted, List<Track> playlist, Track newtrack) {
        for (int i = 0; i < playlist.size(); i++) {
            if (newtrack.length > playlist.get(i).length
                    && length(playlist) - playlist.get(i).length + newtrack.length < totalLengthWanted) {
                replace(playlist, playlist.get(i), newtrack);
            }
        }
    }

    private void replace(List<Track> playlist, Track toRemove, Track toAdd) {
        playlist.remove(toRemove);
        playlist.add(toAdd);
        System.out.printf("Substituted %s with %s and length is now %s\n", toRemove, toAdd, length(playlist));
    }

    private static double length(List<Track> playlist) {
        return combinedLength(playlist, null);
    }

    private static double combinedLength(List<Track> playlist, Track anotherTrack) {
        double out = 0d;
        for (Track d : playlist) {
            out += d.length;
        }
        if (anotherTrack != null)
            out += anotherTrack.length;
        return out;
    }

    class Track {
        private String name;
        private double length;

        Track(String name, double length) {
            this.name = name;
            this.length = length;
        }
    }

    private List<Track> tracks(Map json) {
        List<Track> out = new ArrayList<Track>();
        List tracks = (List) json.get("tracks");
        for (Object t : tracks) {
            Map track = (Map) t;
            String name = (String) track.get("name");
            Double length = Double.valueOf((String) track.get("length"));
            out.add(new Track(name, length));
        }
        return out;
    }
}
