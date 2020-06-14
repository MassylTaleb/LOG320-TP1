package Converter;

import Model.HuffmanData;

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

    public static void saveDataToFileCOMP(List<Integer> compressedchain, String destinationPath) {

        System.out.println(compressedchain);
    }

    public static void saveDataToFileDECO(List<String> decompressedchain, String destinationPath) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath));
            for(String str: decompressedchain) {
                writer.write(str.toString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
