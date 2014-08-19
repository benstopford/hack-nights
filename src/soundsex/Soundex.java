package soundsex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Soundex {
    List<String> song = Arrays.asList(("You were working as a waitress in a cocktail bar\n" +
            "When I met you\n" +
            "I picked you out, I shook you up\n" +
            "And turned you around\n" +
            "Turned you into someone new\n" +
            "Now five years later on you've got the world at your feet\n" +
            "Success has been so easy for you\n" +
            "But don't forget it's me who put you where you are now\n" +
            "And I can put you back down too.\n" +
            "\n" +
            "Don't. Don't you want me?\n" +
            "You know I can't believe it when I hear that you won't see me\n" +
            "Don't. Don't you want me?\n" +
            "You know I don't believe you when you say that you don't need me\n" +
            "\n" +
            "It's much too late to find\n" +
            "When you think you've changed your mind\n" +
            "You'd better change it back or we will both be sorry\n" +
            "\n" +
            "Don't you want me, baby?\n" +
            "Don't you want me? Oh!\n" +
            "Don't you want me, baby?\n" +
            "Don't you want me? Oh!\n" +
            "\n" +
            "I was working as a waitress in a cocktail bar\n" +
            "That much is true\n" +
            "But even then I knew I'd find a much better place\n" +
            "Either with or without you\n" +
            "The five years we have had have been such good at times\n" +
            "I still love you\n" +
            "But now I think it's time I lived my life on my own\n" +
            "I guess it's just what I must do\n" +
            "\n" +
            "Don't. Don't you want me?\n" +
            "You know I can't believe it when I hear that you won't see me\n" +
            "Don't. Don't you want me?\n" +
            "You know I don't believe you when you say that you don't need me\n" +
            "\n" +
            "It's much too late find\n" +
            "When you think you've changed your mind\n" +
            "You'd better change it back or we will both be sorry\n" +
            "\n" +
            "Don't you want me, baby?\n" +
            "Don't you want me? Oh!\n" +
            "Don't you want me, baby?\n" +
            "Don't you want me? Oh!").toLowerCase().replaceAll("\n", " \n").split(" "));

    private Map<Character, Integer> encodings = new HashMap() {{
        put('b', 1);
        put('f', 1);
        put('p', 1);
        put('v', 1);
        put('c', 2);
        put('g', 2);
        put('j', 2);
        put('k', 2);
        put('q', 2);
        put('s', 2);
        put('x', 2);
        put('z', 2);
        put('d', 3);
        put('t', 3);
        put('l', 4);
        put('m', 5);
        put('n', 5);
        put('r', 6);
    }};

    public static void main(String[] args) throws IOException {
        new Soundex();
    }

    public Soundex() throws IOException {
        Map<String, List<String>> encodedDict = encode(loadShareDictWords());

        StringBuffer soundexed = new StringBuffer();

        for (String word : song) {
            List<String> replacements = encodedDict.get(encode(word));

            if (word.length() > 4 && replacements != null && replacements.size() > 0) {
                soundexed.append(randomOne(replacements, word));
            } else {
                soundexed.append(word);
            }
            if(!word.endsWith("\n"))
                soundexed.append(" ");
        }

        System.out.println(soundexed);
    }

    private Map<String, List<String>> encode(List<String> dictionary) {
        Map<String, List<String>> encodedDict = new TreeMap();

        for (String word : dictionary) {
            String encoded = encode(word);
            List<String> wordChoices = encodedDict.get(encoded);
            if (wordChoices == null) wordChoices = new ArrayList();
            wordChoices.add(word);
            encodedDict.put(encoded, wordChoices);
        }
        return encodedDict;
    }

    private List<String> loadShareDictWords() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("/usr/share/dict/words")));
        StringBuffer dict = new StringBuffer();
        while (true) {
            String s = reader.readLine();
            if (s == null) break;
            dict.append(s).append("\n");
        }
        String[] split = dict.toString().toLowerCase().split("\n");
        return Arrays.asList(split);
    }

    private String encode(String word) {
        String encoded = "";
        if(word.endsWith(","))
            word = word.substring(0, word.length()-1);

        char[] letters = word.toCharArray();
        for (int i = 0; i < letters.length; i++) {
            char c = letters[i];
            if (i == 0) {
                encoded += c;
            } else {
                encoded += getEcodingFor(c);
            }
            char[] trans = encoded.toCharArray();
            if (encoded.length() >= 2 && trans[trans.length - 1] == trans[trans.length - 2]) {
                encoded = encoded.substring(0, encoded.length() - 1);
            }

            if (encoded.length() > 4) {
                encoded = encoded.substring(0, 4);
            }
        }
        return encoded;
    }


    private String randomOne(List<String> replacements, String original) {
        if (replacements.isEmpty()) return null;

        //pick the first one of the same length
        for(String s: replacements){
            if(s.length() == original.length())
                return s;
        }
        //else just pick a random one
        int round = (int) Math.ceil(new Random().nextDouble() * replacements.size());
        return replacements.get(round - 1);
    }

    private String getEcodingFor(char c) {
        Integer encoding = encodings.get(Character.valueOf(c));
        return encoding == null ? "" : encoding.toString();
    }
}
