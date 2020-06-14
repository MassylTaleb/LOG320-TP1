package Model;

import java.io.Serializable;
import java.util.List;

public class LZWData implements Serializable {

    private List<String> chain;

    public LZWData(List<?> chain) {
        this.chain = (List<String>) chain;
    }

    public List<String> getChain() {
        return chain;
    }
}
