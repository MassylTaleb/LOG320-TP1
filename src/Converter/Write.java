package Converter;

import Model.HuffmanData;
import Model.LZWData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class Write {

    public static void saveDataToFile(HuffmanData huffmanData, String destinationPath) {

        File file = new File(destinationPath);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(huffmanData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDataToFile(LZWData lzwData, String destinationPath) {

        File file = new File(destinationPath);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lzwData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDataToFile(List<String> decompressedData, String destinationPath) {

        File file = new File(destinationPath);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(decompressedData);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveByteArrayToFile(byte[] decompressedData, String destinationPath) {

        File file = new File(destinationPath);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decompressedData);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
