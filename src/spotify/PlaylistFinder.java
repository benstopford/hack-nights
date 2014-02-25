package spotify;

import util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaylistFinder {

    private double LENGTH = 3600;
    private String SEARCH_TERM = "plants";


    public static void main(String[] args) throws Exception {
        new PlaylistFinder();
    }

    class Track {
        private String name;
        private double length;

        public Track(String name, double length) {
            this.name = name;
            this.length = length;
        }

        @Override
        public String toString() {
            return "Track{" +
                    "name='" + name + '\'' +
                    ", length=" + length +
                    '}';
        }
    }

    public PlaylistFinder() throws Exception {
        List<Track> tracks = getTracks();


        List<Track> playlist = new ArrayList<Track>();

        for (Track t : tracks) {
            double playlistLength = playlistLength(playlist);
            if (playlistLength + t.length < LENGTH) {
                playlist.add(t);
            } else {
                List<Track> playlist1 = new ArrayList(playlist);
                for (Track playlistTrack : playlist1) {
                    if (playlistTrack.length < t.length && (playlistLength - playlistTrack.length + t.length) < LENGTH) {
                        playlist.remove(playlistTrack);
                        playlist.add(t);
                        System.out.println(String.format("Removing %s for %s", playlistTrack.length, t.length));
                        break;
                    }
                }
            }
        }
        System.out.println("Playlist = " + playlist);
        System.out.println(playlist.size());
        System.out.printf("Searching for length %s and found %s\n", LENGTH, playlistLength(playlist));
    }

    private List<Track> getTracks() throws IOException {
        List<Track> allTracks = new ArrayList<Track>();
        int pages = 0;
        int max = 20;
        while (pages < max) {
            try {
                Map json = Utils.fromJson(Utils.wget("http://ws.spotify.com/search/1/track.json?q=" + SEARCH_TERM + "&page="+pages));
                List<Track> tracks = fromMap(json);
                allTracks.addAll(tracks);
            } catch (Exception e) {
            }
            pages++;
        }
        return allTracks;
    }

    private List<Track> fromMap(Map json) {
        List<Track> returnList = new ArrayList<Track>();
        List tracks = (List) json.get("tracks");
        if (tracks != null) {
            for (Object track : tracks) {
                String name = (String) ((Map) track).get("name");
                Object lenValue = ((Map) track).get("length");
                String lenValue1 = (String) lenValue;
                double length = Double.valueOf(lenValue1);
                Track t = new Track(name, length);
                returnList.add(t);
            }
        }
        return returnList;
    }

    private double playlistLength(List<Track> playlist) {
        double length = 0;
        for (Track t : playlist) {
            length += t.length;
        }
        return length;
    }
}
