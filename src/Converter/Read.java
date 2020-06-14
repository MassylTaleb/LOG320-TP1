package Converter;

import Model.HuffmanData;

import java.io.*;

public class Read {

    public static byte[] convertFileToByteArray(String inputPath) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            FileInputStream fis = new FileInputStream(inputPath);
            int i= 0;
            while((i=fis.read()) != -1) {
                bos.write(i);
            }
            fis.close();
            bos.close();
        } catch (IOException e) {
            System.out.println("Sorry, this file doesn't exist.");
        }

        return bos.toByteArray();
    }

    public static HuffmanData convertFileToHuffmanData(String inputPath) {

        HuffmanData huffmanData = null;
        try {
            FileInputStream fis = new FileInputStream(inputPath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            huffmanData = (HuffmanData) ois.readObject();
            fis.close();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return huffmanData;
    }
}
