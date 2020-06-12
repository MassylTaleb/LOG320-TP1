package Compressor;

public class FrequencyNode implements Comparable<FrequencyNode>, Cloneable {

    private int frequency;
    private byte value;

    public FrequencyNode() {

    }

    @Override
    public int compareTo(FrequencyNode o) {
        return 0;
    }

    public byte getValue() {
        return this.value;
    }
}
