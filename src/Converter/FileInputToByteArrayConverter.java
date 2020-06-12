package Converter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FileInputToByteArrayConverter {

    public static byte[] convert(String inputPath) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            FileInputStream fis = new FileInputStream(inputPath);
            int i= 0;
            while((i=fis.read()) != -1) {
                bos.write(i);
            }
        } catch (IOException e) {
            System.out.println("Sorry, this file doesn't exist.");
        }

        System.out.println(bos);
        return bos.toByteArray();
    }
}