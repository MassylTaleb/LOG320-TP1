package Main;

import Encodage.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String algorithmeDeCompression = args[0];
        String CompOuDecomp = args[1];
        String inputFile = args[2];
        String outputFile = args[3];
        List<Character> uncompressedTextFileChar = new ArrayList<Character>();
        List<Integer> compressedTextFileInt = new ArrayList<Integer>();


        if(algorithmeDeCompression.contentEquals("-lzw")) {

            if(CompOuDecomp.contentEquals("-c")) {
                uncompressedTextFileChar=readCharList(inputFile);
                List<Integer> compressedTextFile = LempelZivWelch.compress(uncompressedTextFileChar);

                FileWriter writer = new FileWriter(outputFile);
                for(Integer str: compressedTextFile) {
                    writer.write(str.toString());
                }
                writer.close();

                System.out.println(compressedTextFile);

            }else if(CompOuDecomp.contentEquals("-d")) {
                compressedTextFileInt=readIntList(inputFile);
                List<String> decompressedTextFile = LempelZivWelch.decompress(compressedTextFileInt);

                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                writer.write(decompressedTextFile.toString());
                writer.close();

                System.out.println(decompressedTextFile);

            }

        }else if(algorithmeDeCompression.contentEquals("-huff")) {

            if(CompOuDecomp.contentEquals("-c")) {

                uncompressedTextFileChar=readCharList(inputFile);
                Huffman.buildHuffmanTree(uncompressedTextFileChar, 0);

            }else if(CompOuDecomp.contentEquals("-d")) {
                compressedTextFileInt=readIntList(inputFile);
                List<Character> compressedTextFileChar = new ArrayList<Character>();
                for (int i = 0; i < compressedTextFileInt.size(); i++) {
                    compressedTextFileChar.add((char)(int)compressedTextFileInt.get(i));
                }
                Huffman.buildHuffmanTree(compressedTextFileChar, 1);

            }

        }else if(algorithmeDeCompression.contentEquals("-opt")) {

            if(CompOuDecomp.contentEquals("-c")) {
                uncompressedTextFileChar=readCharList(inputFile);
                List<Integer> compressedTextFile = LempelZivWelch.compress(uncompressedTextFileChar);
                List<Character> compressedTextFileChar = new ArrayList<Character>();
                for (int i = 0; i < compressedTextFile.size(); i++) {
                    compressedTextFileChar.add((char)(int)compressedTextFile.get(i));
                }
                Huffman.buildHuffmanTree(compressedTextFileChar, 0);

            }else if(CompOuDecomp.contentEquals("-d")) {
                compressedTextFileInt=readIntList(inputFile);
                // List<String> decompressedTextFile = Opt.callMethod

                compressedTextFileInt=readIntList(inputFile);
                List<Character> compressedTextFileChar = new ArrayList<Character>();
                for (int i = 0; i < compressedTextFileInt.size(); i++) {
                    compressedTextFileChar.add((char)(int)compressedTextFileInt.get(i));
                }
                Huffman.buildHuffmanTree(compressedTextFileChar, 1);

            }
        }
    }



    public static List<Character> readCharList(String fichierE) throws IOException{
        List<Character> uncompressedTextFileChar = new ArrayList<Character>();
        FileReader inputStream = null;
        try {
            inputStream = new FileReader(fichierE);
            int c;
            while ((c = inputStream.read()) != -1) {
                uncompressedTextFileChar.add((char) c);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not Found!");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return uncompressedTextFileChar;
    }

    public static List<Integer> readIntList(String fichierE) throws IOException{
        List<Integer> compressedTextFileInt = new ArrayList<Integer>();
        FileReader inputStream = null;
        try {
            inputStream = new FileReader(fichierE);
            int c;
            while ((c = inputStream.read()) != -1) {
                compressedTextFileInt.add((int) c);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not Found!");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return compressedTextFileInt;
    }
}
