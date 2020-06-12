package Compressor;

import java.util.*;

public class HuffmanCompressor implements ICompressor {

    private byte[] fileInputAsByteArray;
    private String outputFile;

    public HuffmanCompressor(byte[] fileInputAsByteArray, String outputFile) {
        this.fileInputAsByteArray = fileInputAsByteArray;
        this.outputFile = outputFile;
    }

    @Override
    public void compress() {
        ArrayList<FrequencyNode> frequencyTable = createFrequencyTable();
        FrequencyNode rootNode = createHuffmanTree(new ArrayList<>(frequencyTable));
        encode(rootNode, frequencyTable);
    }

    private ArrayList<FrequencyNode> createFrequencyTable() {

        ArrayList<FrequencyNode> frequencyTable = new ArrayList<>();

        for(byte value : this.fileInputAsByteArray) {
            if(!existInFrequencyTable(value, frequencyTable)) {
                frequencyTable.add(new FrequencyNode(value));
            }
        }

        Collections.sort(frequencyTable);
        return frequencyTable;
    }

    private boolean existInFrequencyTable(byte newValue, ArrayList<FrequencyNode> frequencyTable) {

        for (FrequencyNode currentNode : frequencyTable) {
            if (newValue == currentNode.getValue()) {
                currentNode.incrementValue();
                return true;
            }
        }
        return false;
    }

    private FrequencyNode createHuffmanTree(ArrayList<FrequencyNode> frequencyTable) {

        // Stop condition until there's only one FrequencyNode inside the list
        // This node is the root
        if(frequencyTable.size() == 1) {
            return frequencyTable.get(0);
        }

        // Remove the two lowest frequencies in the frequency table
        FrequencyNode firstLowestFrequency = frequencyTable.remove(0);
        FrequencyNode secondLowestFrequency = frequencyTable.remove(0);

        // Create parent
        FrequencyNode parentNode = new FrequencyNode(firstLowestFrequency, secondLowestFrequency);
        frequencyTable.add(parentNode);

        // Make sure the list is always sorted
        Collections.sort(frequencyTable);

        // Recursive
        createHuffmanTree(frequencyTable);

        return frequencyTable.get(0);
    }

    private void encode(FrequencyNode currentNode, ArrayList<FrequencyNode> frequencyTable) {

        Map<String, String> codeValueMap = new HashMap<>();
        createCodeValueMap(codeValueMap, currentNode, "");
    }

    private void createCodeValueMap(Map<String, String> codeValueMap, FrequencyNode currentNode, String codeCurrentNode) {

        if(currentNode.getLeftNode() != null && currentNode.getRightNode() != null) {

            createCodeValueMap(codeValueMap, currentNode.getLeftNode(),  "0" + codeCurrentNode);
            createCodeValueMap(codeValueMap, currentNode.getRightNode(), "1" + codeCurrentNode);
        }

        codeValueMap.put(codeCurrentNode, new String(new byte[] {currentNode.getValue()}));
    }

    @Override
    public void decompress() {

    }
}
