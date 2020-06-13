package Encodage;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

// créé un node tree
class Node
{
    char ch;
    int freq;
    Node left = null, right = null;

    Node(char ch, int freq)
    {
        this.ch = ch;
        this.freq = freq;
    }

    public Node(char ch, int freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
};

public class Huffman {
    // traverse le Huffman Tree et l'enregistre dans un map
    public static void encode(Node racine, String str,
                              Map<Character, String> huffmanCode)
    {
        if (racine == null)
            return;

        // si la feuille du node est trouvé
        if (racine.left == null && racine.right == null) {
            huffmanCode.put(racine.ch, str);
        }

        encode(racine.left, str + "0", huffmanCode);
        encode(racine.right, str + "1", huffmanCode);
    }

    // traverse le Huffman Tree et decode
    public static int decode(Node racine, int index, StringBuilder sb)
    {
        if (racine == null)
            return index;

        // si la feuille du node est trouvé
        if (racine.left == null && racine.right == null)
        {
            System.out.print(racine.ch);
            return index;
        }

        index++;

        if (sb.charAt(index) == '0')
            index = decode(racine.left, index, sb);
        else
            index = decode(racine.right, index, sb);

        return index;
    }

    // construit Huffman Tree et encode ou decode selon le choix
    public static void buildHuffmanTree(List<Character> uncompressedTextFile, int choix) throws IOException
    {

        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0 ; i < uncompressedTextFile.size(); i++) {
            if (!freq.containsKey(uncompressedTextFile.get(i))) {
                freq.put(uncompressedTextFile.get(i), 0);
            }
            freq.put(uncompressedTextFile.get(i), freq.get(uncompressedTextFile.get(i)) + 1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(
                (l, r) -> l.freq - r.freq);

        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() != 1)
        {

            Node left = pq.poll();
            Node right = pq.poll();

            int sum = left.freq + right.freq;
            pq.add(new Node('\0', sum, left, right));
        }


        Node root = pq.peek();

        Map<Character, String> huffmanCode = new HashMap<>();
        encode(root, "", huffmanCode);

        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < uncompressedTextFile.size(); i++) {
            sb.append(huffmanCode.get(uncompressedTextFile.get(i)));
        }
        File file = new File("ppp.txt");

        if(choix == 0) {
            System.out.println(sb);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(sb.toString());
            }
        }else if(choix == 1) {
            // traverse the Huffman Tree again and this time
            // decode the encoded string
            int index = -1;
            System.out.println("\n");
            while (index < sb.length() - 2) {
                index = decode(root, index, sb);
            }
        }

    }

}
