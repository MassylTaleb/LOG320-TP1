package Converter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

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
        } catch (IOException e) {
            System.out.println("Sorry, this file doesn't exist.");
        }

        return bos.toByteArray();
    }
}
