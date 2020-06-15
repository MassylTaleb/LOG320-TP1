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

        //ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] fileInputAsByteArray = null;

        try {
            /*FileInputStream fis = new FileInputStream(inputPath);
            int i= 0;
            while((i=fis.read()) != -1) {
                bos.write(i);
            }
            fis.close();
            bos.close();*/
            fileInputAsByteArray = Files.readAllBytes(Paths.get(inputPath));
        } catch (IOException e) {
            System.out.println("Sorry, this file doesn't exist.");
        }

        //return bos.toByteArray();
        return fileInputAsByteArray;
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
