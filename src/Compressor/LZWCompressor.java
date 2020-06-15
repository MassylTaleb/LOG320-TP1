package Compressor;

import Converter.Read;
import Converter.Write;
import Model.LZWData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZWCompressor implements ICompressor {

    private String inputFile;
    private String outputFile;


    public LZWCompressor(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
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
        byte[] fileInputAsByteArray = Read.convertFileToByteArray(this.inputFile);
        System.out.println(new String(fileInputAsByteArray));
        System.out.println(fileInputAsByteArray.length);
        System.out.println(Byte.toUnsignedInt(fileInputAsByteArray[0]));
        System.out.println(Byte.toUnsignedInt(fileInputAsByteArray[1]));

        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        List<Integer> compressedChain = new ArrayList<Integer>();
        int dictionarySize = 256;
        char c = 0;
        String s;

        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put(""+(char)i, i);
        }


        System.out.println(dictionary);

        s = "" + (char) Byte.toUnsignedInt(fileInputAsByteArray[0]);
        for (int i = 1; i < fileInputAsByteArray.length; i++) {
           if (fileInputAsByteArray[i]<0){
                c = (char) Byte.toUnsignedInt(fileInputAsByteArray[i]);
            }else{
            c = (char) fileInputAsByteArray[i];}
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
        System.out.println(compressedChain);
        LZWData dataToSave = new LZWData(compressedChain);
        Write.saveDataToFile(dataToSave, this.outputFile);
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
        LZWData lzwData = Read.readCompressedChain(this.inputFile);
        List<Integer> compressedChain = new ArrayList<Integer>(lzwData.getCompressedChain());

        System.out.println("voila  "+lzwData.getCompressedChain());

        Map<Integer,String> dictionary = new HashMap<Integer,String>();
        List<String> decompressedChain = new ArrayList<String>();
        int dictionarySize = 256;
        String seq, s="";
        int k;

        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put(i, "" + (char) i);
        }

        for (int i = 0; i < compressedChain.size(); i++) {
            k = compressedChain.get(i);
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
        System.out.println(new String(String.valueOf(decompressedChain)));
        Write.saveDataToFile(decompressedChain, this.outputFile);
    }

}

