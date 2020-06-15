package Converter;

import Model.HuffmanData;

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
}
