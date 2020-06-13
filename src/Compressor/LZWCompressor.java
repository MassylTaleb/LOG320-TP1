package Compressor;

import Converter.Read;

import java.util.*;

public class LZWCompressor implements ICompressor {

    private String inputFile;
    private String outputFile;
    private byte[] fileInputAsByteArray;

    public LZWCompressor(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.fileInputAsByteArray = Read.convertFileToByteArray(this.inputFile);
        System.out.println(new String(fileInputAsByteArray));
    }

    /*
    s = T[1]
    pour i = 2 à N
        c = T[i]
        si s+c În dict
            s = s+c
        sinon
            sortir le code de s
            dict = dict+{s+c}
            s = c
    sortir le code de s
    */
    @Override
    public void compress() {
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        List<Integer> compressedChain = new ArrayList<Integer>();
        int dictionarySize = 256;
        byte c;
        String s;

        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put("" + (char) i, i);
        }

        s = "" + fileInputAsByteArray[0];
        for (int i = 1; i < fileInputAsByteArray.length; i++) {
            c = fileInputAsByteArray[i];
            String sc = s + c;
            if (dictionary.containsKey(sc)) {
                s = sc;
            } else {
                compressedChain.add(dictionary.get(s));
                dictionary.put(sc, dictionarySize++);
                s = "" + c;
            }
        }

        if (!s.equals("")) {
            compressedChain.add(dictionary.get(s));
        }

    }

        /*
        s = NULL
        pour i = 1 à N
            k = T[i]
            seq = dict[k]
            si seq == NULL
                seq = s + s[0]
            sortir seq
            si s != NULL
                dict = dict+{s+seq[0]}
            s ← seq
        */
    @Override
    public void decompress() {
        Map<Integer,String> dictionary = new HashMap<Integer,String>();
        List<String> decompressedChain = new ArrayList<String>();
        int dictionarySize = 256;
        String seq, s="";
        int k;

        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put(i, "" + (char) i);
        }

        for (int i = 0; i < fileInputAsByteArray.length; i++) {
            k = fileInputAsByteArray[i];
            seq=dictionary.get(k);
            if(seq.equals("")) {
                seq=s + s.charAt(0);
            }
            decompressedChain.add(seq);
            if(!s.equals("")) {
                dictionary.put(dictionarySize++, s+seq.charAt(0));
            }
            s=""+seq;
        }

    }
}

