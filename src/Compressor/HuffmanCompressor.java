package Compressor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HuffmanCompressor implements ICompressor {

    private byte[] fileInputAsByteArray;
    private String outputFile;
    private ArrayList<FrequencyNode> frequencyTable;

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

        this.frequencyTable = new ArrayList<>();

        for(byte value : this.fileInputAsByteArray) {
            if(!existInFrequencyTable(value, frequencyTable)) {
                frequencyTable.add(new FrequencyNode(value));
            }
        }

        Collections.sort(this.frequencyTable);
    }

    private boolean existInFrequencyTable(byte newValue, ArrayList<FrequencyNode> frequencyTable) {

        for (FrequencyNode currentNode : frequencyTable) {

            if (Byte.compare(newValue, currentNode.getValue()) == 0) {
                currentNode.incrementValue();
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
