package Converter;

import Model.HuffmanData;
import Model.LZWData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.*;


public class Read {

    public static byte[] convertFileToByteArray(String inputPath) {

        File file = new File(inputPath);
        byte[] byteArray = new byte[(int) file.length()];

        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(byteArray);
        } catch (IOException e) {
            System.out.println("Sorry, this file doesn't exist.");
        }

        return byteArray;
    }

    public static HuffmanData convertFileToHuffmanData(String inputPath) {

        HuffmanData huffmanData = null;
        File file = new File(inputPath);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            huffmanData = (HuffmanData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return huffmanData;
    }

    public static LZWData readCompressedChain(String inputPath) {

        LZWData lzwData = null;
        try {
            FileInputStream fis = new FileInputStream(inputPath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            lzwData = (LZWData) ois.readObject();
            fis.close();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return lzwData;
    }
}
