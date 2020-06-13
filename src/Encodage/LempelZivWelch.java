package Encodage;

import java.util.*;

public class LempelZivWelch {
    /*
     * s = T[1]
     * pour i = 2 à N
     *  c = T[i]
     *  si s+c În dict
     *   s = s+c
     *  sinon
     *   sortir le code de s
     *   dict = dict+{s+c}
     *   s = c
     * sortir le code de s
     */
    public static List<Integer> compress(List<Character> uncompressedTextFile) {
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        List<Integer> compressedChain = new ArrayList<Integer>();
        int dictionarySize = 256;
        char c;
        String s;

        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put("" + (char) i, i);
        }

        s = "" + uncompressedTextFile.get(0);
        for (int i = 1; i < uncompressedTextFile.size(); i++) {
            c = uncompressedTextFile.get(i);
            String sc = s + c;
            if (dictionary.containsKey(sc)) {
                s = "" + sc;
            } else {
                compressedChain.add(dictionary.get(s));
                dictionary.put(sc, dictionarySize++);
                s = "" + c;
            }
        }

        if (!s.equals("")) {
            compressedChain.add(dictionary.get(s));
        }

        return compressedChain;

    }

    /*
     * s = NULL
     * pour i = 1 à N
     *  k = T[i]
     *  seq = dict[k]
     *  si seq == NULL
     *   seq = s + s[0]
     *  sortir seq
     *  si s != NULL
     *   dict = dict+{s+seq[0]}
     *  s ← seq
     */
    public static List<String> decompress(List<Integer> compressedTextFile) {
        Map<Integer, String> dictionary = new HashMap<Integer, String>();
        List<String> decompressedChain = new ArrayList<String>();
        int dictionarySize = 256;
        String seq = "", s = "";
        int k = 0;

        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put(i, "" + (char) i);
        }

        for (int i = 0; i < compressedTextFile.size(); i++) {
            k = compressedTextFile.get(i);
            seq = dictionary.get(k);
            //System.out.println(seq + " " + k + " " + compressedTextFile);
            if (seq == null) {
                seq = s + s.charAt(0);
            }
            decompressedChain.add(seq);
            if (!s.equals("")) {
                dictionary.put(dictionarySize++, s + seq.charAt(0));
            }
            s = "" + seq;
        }

        return decompressedChain;

    }
}
