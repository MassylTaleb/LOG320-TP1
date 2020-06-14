package Compressor;

import java.io.Serializable;

public class FrequencyNode implements Comparable<FrequencyNode>, Serializable {

    private int frequency;
    private byte value;
    private FrequencyNode leftNode;
    private FrequencyNode rightNode;

    public FrequencyNode(byte value) {
        this.frequency = 1;
        this.value = value;
    }

    public FrequencyNode(FrequencyNode leftNode, FrequencyNode rightNode) {
        this.frequency = leftNode.frequency + rightNode.frequency;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public void incrementValue() {
        this.frequency++;
    }

    @Override
    public int compareTo(FrequencyNode node) {

        return this.frequency - node.getFrequency();
    }

    public byte getValue() {
        return this.value;
    }

    public int getFrequency() { return frequency; }

    public FrequencyNode getLeftNode() {
        return leftNode;
    }

    public FrequencyNode getRightNode() {
        return rightNode;
    }

    public String toString() {
        return this.frequency + " - " + this.value;
    }
}
