package huffman;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCoder {

    private PriorityQueue<Node> tree;

    abstract class Node implements Comparable {
        int freq;
        Node(int f) {
            freq = f;
        }

        @Override
        public int compareTo(Object o) {
            Node other = ((Node) o);
            return Integer.valueOf(this.freq).compareTo(other.freq);
        }
    }

    class Leaf extends Node {
        char val;
        Leaf(int freq, char c) {
            super(freq);
            val = c;
        }
    }

    class Internal extends Node {
        Node left, right;
        Internal(Node l, Node r) {
            super(l.freq + r.freq);
            left = l;
            right = r;
        }
    }

    public String encode(String s) {
        Node tree = createTree(s);

        Map<Character, String> dictionary = createDictionaryFrom(tree);

        return encodeFromDictionary(s, dictionary);
    }

    private Map<Character, String> createDictionaryFrom(Node tree) {
        return createDictionaryFrom(tree, "", new HashMap());
    }

    private String encodeFromDictionary(String s, Map<Character, String> table) {
        StringBuffer out = new StringBuffer();
        for (char c : s.toCharArray()) {
            out.append(table.get(c));
        }
        return out.toString();
    }

    private Map<Character, String> createDictionaryFrom(Node node, String value, Map<Character, String> dictionary) {
        if (node instanceof Leaf) {
            Leaf leaf = (Leaf) node;
            dictionary.put(leaf.val, value);
            print(value, leaf);
        } else {
            Internal iNode = (Internal) node;
            createDictionaryFrom(iNode.left, value + '0', dictionary);
            createDictionaryFrom(iNode.right, value + '1', dictionary);
        }
        return dictionary;
    }

    private void print(String value, Leaf leaf) {
        System.out.println(leaf.val + "-" + leaf.freq + " - " + value);
    }

    private Node createTree(String s) {
        int[] frequencies = new int[256]; //assume ascii only

        for (char c : s.toCharArray())
            frequencies[c]++;

        tree = new PriorityQueue<>();

        //populate priority queue
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                tree.add(new Leaf(frequencies[i], (char) i));
            }
        }
        while (tree.size() > 1) {
            Node left = tree.poll();
            Node right = tree.poll();
            tree.offer(new Internal(left, right));
        }

        for (Node node : tree) {
            System.out.println(node);
        }
        return tree.iterator().next();
    }

    public String decode(String encoded) {
        StringBuffer decoded = new StringBuffer();

        while (encoded.length() > 0) {
            String[] next = decode(tree.iterator().next(), encoded);
            decoded.append(next[0]);
            encoded = next[1];
        }
        System.out.println("Decoded: "+decoded.toString());
        return decoded.toString();
    }

    private String[] decode(Node node, String value) {
        if (node instanceof Leaf) {
            Leaf leaf = (Leaf) node;
            System.out.println("Found "+leaf.val + " remainder "+ value);
            return new String[]{String.valueOf(leaf.val), value};
        } else {
            Internal iNode = (Internal) node;
            if (value.startsWith("0")) {
                return decode(iNode.left, value.substring(1, value.length()));
            } else {
                return decode(iNode.right, value.substring(1, value.length()));
            }
        }
    }
}
