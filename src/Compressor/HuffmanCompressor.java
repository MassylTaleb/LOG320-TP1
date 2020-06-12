package Compressor;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HuffmanCompressor implements ICompressor {

    private byte[] fileInputAsByteArray;
    private String outputFile;

    public HuffmanCompressor(byte[] fileInputAsByteArray, String outputFile) {
        this.fileInputAsByteArray = fileInputAsByteArray;
        this.outputFile = outputFile;
    }

    @Override
    public void compress() {
        createFrequencyTable();
        createHuffmanTree();
        encode();
    }

    private void createFrequencyTable() {

        ArrayList<FrequencyNode> frequencyTable = new ArrayList<FrequencyNode>();

        for(byte value : this.fileInputAsByteArray) {
            if(existInFrequencyTable(value, frequencyTable)) {

            }
        }
    }

    private boolean existInFrequencyTable(byte value, ArrayList<FrequencyNode> frequencyTable) {

        for(int i = 0; i < frequencyTable.size(); i++) {
            if(Byte.compare(value, frequencyTable.get(i).getValue()) == 0) {
                return true;
            }
        }
        return false;
    }

    private void createHuffmanTree() {


    }

    private void encode() {

    }

    @Override
    public void decompress() {

    }
}
