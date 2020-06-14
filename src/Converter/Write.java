package Converter;

import Model.HuffmanData;
import Model.LZWData;

import java.io.*;
import java.util.List;

public class Write {

    public static void saveDataToFile(HuffmanData huffmanData, String destinationPath) {

        try {
            FileOutputStream fos = new FileOutputStream(destinationPath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(huffmanData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    public static void saveDataToFile(LZWData lzwData, String destinationPath) {

        System.out.println(lzwData);
        try {
            FileOutputStream fos = new FileOutputStream(destinationPath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lzwData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDataToFile(List<String> decompressedChain, String destinationPath) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath));
            for(String str: decompressedChain) {
                writer.write(str.toString());
            }
            writer.close();
=======
    public static void saveByteArrayToFile(byte[] decompressedData, String destinationPath) {

        try {
            FileOutputStream fos = new FileOutputStream(destinationPath);
            fos.write(decompressedData);
            fos.close();
>>>>>>> 03123d31e3d1c88cd51118aa47302892a3ada6d2
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
