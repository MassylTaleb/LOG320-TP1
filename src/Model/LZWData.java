package Model;

import java.io.Serializable;
import java.util.List;

public class LZWData implements Serializable {

    private List<Integer> compressedChain;

    public LZWData(List<Integer> compressedChain) {
        this.compressedChain = compressedChain;
    }

    public List<Integer> getCompressedChain() {
        return compressedChain;
    }
}
