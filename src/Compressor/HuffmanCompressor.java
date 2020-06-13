package Compressor;

import Converter.Read;
import Converter.Write;
import Model.HuffmanData;

import java.io.*;
import java.util.*;

public class HuffmanCompressor implements ICompressor, Serializable {

    private String inputFile;
    private String outputFile;

    public HuffmanCompressor(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    @Override
    public void compress() {

        byte[] fileInputAsByteArray = Read.convertFileToByteArray(this.inputFile);
        System.out.println(new String(fileInputAsByteArray));

        ArrayList<FrequencyNode> frequencyTable = createFrequencyTable(fileInputAsByteArray);
        FrequencyNode rootNode = createHuffmanTree(new ArrayList<>(frequencyTable));
        encode(frequencyTable, rootNode, fileInputAsByteArray);
    }

    private ArrayList<FrequencyNode> createFrequencyTable(byte[] fileInputAsByteArray) {

        ArrayList<FrequencyNode> frequencyTable = new ArrayList<>();

        for(byte value : fileInputAsByteArray) {
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

    private void encode(ArrayList<FrequencyNode> frequencyTable, FrequencyNode currentNode, byte[] fileInputAsByteArray) {

        Map<String, String> codeValueMap = new HashMap<>();
        createCodeValueMap(codeValueMap, currentNode, "");
        String encodedValue = createEncodedValue(codeValueMap, fileInputAsByteArray);

        int extraBitsToAdd = encodedValue.length() % 8;
        byte[] resultValueInByteArray = encodeValueInByteArray(encodedValue, extraBitsToAdd);

        HuffmanData dataToSave = new HuffmanData(frequencyTable, resultValueInByteArray, extraBitsToAdd);
        Write.saveDataToFile(dataToSave, this.outputFile);
    }

    private void createCodeValueMap(Map<String, String> codeValueMap, FrequencyNode currentNode, String codeCurrentNode) {

        if(currentNode.getLeftNode() != null && currentNode.getRightNode() != null) {

            createCodeValueMap(codeValueMap, currentNode.getLeftNode(),  "0" + codeCurrentNode);
            createCodeValueMap(codeValueMap, currentNode.getRightNode(), "1" + codeCurrentNode);
        }

        codeValueMap.put(new String(new byte[] {currentNode.getValue()}), codeCurrentNode);
    }

    private String createEncodedValue(Map<String, String> codeValueMap, byte[] fileInputAsByteArray) {

        ArrayList<String> valueOfEachCharacter = new ArrayList<>();
        StringBuilder codedFileInBinary = new StringBuilder();

        for(byte byteValue : fileInputAsByteArray) {
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

    @Override
    public void decompress() {

//        try {
//            FileInputStream fis = new FileInputStream(this.inputFile);
//            ObjectInputStream oos = new ObjectInputStream(fis);
//            HuffmanData huffmanData = (HuffmanData) oos.readObject();
//            System.out.println(huffmanData.getFrequencyTable());
//            System.out.println(Arrays.toString(huffmanData.getFileContentCompressed()));
//            System.out.println(huffmanData.getExtraBitsToAdd());
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}
