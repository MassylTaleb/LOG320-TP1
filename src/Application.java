import Compressor.*;
import Converter.*;

public class Application {

    public static void main(String[] args) {

        System.out.println("Welcome to Team 2's compressor/decompressor !");

        // Algorithm method selected by the user
        String methodSelected = args[0];

        // Action between compression or decompression
        String actionOnFile = args[1];

        // Declare input file path
        byte[] fileInputAsByteArray = FileInputToByteArrayConverter.convert(args[2]);
        System.out.println(new String(fileInputAsByteArray));

        // Declare output file path
        String dstPath = args[3];

        ICompressor method;

        switch(methodSelected) {
            case(Constants.HUFFMAN):

                method = new HuffmanCompressor(fileInputAsByteArray, dstPath);

                if(actionOnFile.equals(Constants.COMPRESS)) {
                    method.compress();
                } else if (actionOnFile.equals(Constants.DECOMPRESS)) {
                    method.decompress();
                } else {
                    System.out.println("Sorry, the argument received are incorrect. Use -c for compression or -d for decompression.");
                }
                break;

            case(Constants.LZW):

                method = new LZWCompressor(fileInputAsByteArray, dstPath);

                if(actionOnFile.equals(Constants.COMPRESS)) {
                    method.compress();
                } else if (actionOnFile.equals(Constants.DECOMPRESS)) {
                    method.decompress();
                } else {
                    System.out.println("Sorry, the argument received are incorrect. Use -c for compression or -d for decompression.");
                }
                break;

            case(Constants.OPTMIZED):
                break;

            default:
                System.out.println("Sorry, the argument received are incorrect. Use -huff, -lzw or -opt to select the algorithm to apply for the selected action.");
                break;
        }
    }
}