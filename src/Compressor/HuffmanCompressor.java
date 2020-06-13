package Compressor;

import Model.HuffmanData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
        encode(frequencyTable, rootNode);
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

    private void encode(ArrayList<FrequencyNode> frequencyTable, FrequencyNode currentNode) {

        Map<String, String> codeValueMap = new HashMap<>();
        createCodeValueMap(codeValueMap, currentNode, "");
        String encodedValue = createEncodedValue(codeValueMap);
        int extraBitsToAdd = encodedValue.length() % 8;
        byte[] resultValueInByteArray = encodeValueInByteArray(encodedValue, extraBitsToAdd);
        saveEncodedValueToDestinationFile(resultValueInByteArray, frequencyTable, extraBitsToAdd);
    }

    private void createCodeValueMap(Map<String, String> codeValueMap, FrequencyNode currentNode, String codeCurrentNode) {

        if(currentNode.getLeftNode() != null && currentNode.getRightNode() != null) {

            createCodeValueMap(codeValueMap, currentNode.getLeftNode(),  "0" + codeCurrentNode);
            createCodeValueMap(codeValueMap, currentNode.getRightNode(), "1" + codeCurrentNode);
        }

        codeValueMap.put(new String(new byte[] {currentNode.getValue()}), codeCurrentNode);
    }

    private String createEncodedValue(Map<String, String> codeValueMap) {

        ArrayList<String> valueOfEachCharacter = new ArrayList<>();
        StringBuilder codedFileInBinary = new StringBuilder();

        for(byte byteValue : this.fileInputAsByteArray) {
            valueOfEachCharacter.add(codeValueMap.get(new String(new byte[] {byteValue})));
        }

        for(String value : valueOfEachCharacter) {
            codedFileInBinary.append(value);
        }

        return codedFileInBinary.toString();
    }

    private byte[] encodeValueInByteArray(String encodedValue, int extraBitsToAdd) {
        int encodedValueLength = encodedValue.length();
        int numberOfByte = encodedValueLength / 8;

        if(extraBitsToAdd != 0) {
            encodedValue = encodedValue + "0".repeat(extraBitsToAdd);
            encodedValueLength = encodedValue.length();
            numberOfByte++;
        }

        byte[] encodedInByte = new byte[numberOfByte];

        for(int j = 0; j < encodedInByte.length; j++) {
            if((j * 8) < encodedValueLength) {
                int valueOfByte = Integer.parseInt(encodedValue.substring((j * 8), (j * 8) + 8), 2);
                encodedInByte[j] = (byte) valueOfByte;
            }
        }

        return encodedInByte;
    }

    private void saveEncodedValueToDestinationFile(byte[] encodedInByte, ArrayList<FrequencyNode> frequencyTable, int extraBitsToAdd) {

        try {
            FileOutputStream fos = new FileOutputStream(this.outputFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            HuffmanData dataToSave = new HuffmanData(frequencyTable, encodedInByte, extraBitsToAdd);
            oos.writeObject(dataToSave);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decompress() {

    }
}
