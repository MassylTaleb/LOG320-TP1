package Model;

import Compressor.FrequencyNode;

import java.io.Serializable;
import java.util.ArrayList;

public class HuffmanData implements Serializable {

    private ArrayList<FrequencyNode> frequencyTable;
    private byte[] fileContentCompressed;
    private int extraBits;

    public HuffmanData(ArrayList<FrequencyNode> frequencyTable, byte[] fileContentCompressed, int extraBits) {
        this.frequencyTable = frequencyTable;
        this.fileContentCompressed = fileContentCompressed;
        this.extraBits = extraBits;
    }

    public ArrayList<FrequencyNode> getFrequencyTable() {
        return frequencyTable;
    }

    public byte[] getFileContentCompressed() {
        return fileContentCompressed;
    }

    public int getExtraBits() {
        return extraBits;
    }
}
