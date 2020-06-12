package Compressor;

public class FrequencyNode implements Comparable<FrequencyNode>, Cloneable {

    private int frequency;
    private byte value;

    public FrequencyNode(byte value) {
        this.frequency = 1;
        this.value = value;
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

    public String toString() {
        return String.valueOf(this.frequency) + " - " + new String(new byte[] {this.value});
    }
}
