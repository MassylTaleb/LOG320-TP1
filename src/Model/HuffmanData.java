package Model;

import Compressor.FrequencyNode;

import java.io.Serializable;

public class HuffmanData implements Serializable {

    private FrequencyNode rootNode;
    private byte[] fileContentCompressed;
    private int extraBits;

    public HuffmanData(FrequencyNode rootNode, byte[] fileContentCompressed, int extraBits) {
        this.rootNode = rootNode;
        this.fileContentCompressed = fileContentCompressed;
        this.extraBits = extraBits;
    }

    public FrequencyNode getRootNode() {
        return rootNode;
    }

    public byte[] getFileContentCompressed() {
        return fileContentCompressed;
    }

    public int getExtraBits() {
        return extraBits;
    }
}
