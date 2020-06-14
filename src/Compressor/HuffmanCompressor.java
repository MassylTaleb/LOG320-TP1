package Compressor;

import Converter.Read;
import Converter.Write;
import Model.HuffmanData;
import Tools.Constants;

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
        System.out.println("To compress : " + new String(fileInputAsByteArray));

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

    private void encode(ArrayList<FrequencyNode> frequencyTable, FrequencyNode rootNode, byte[] fileInputAsByteArray) {

        Map<String, String> codeValueMap = new HashMap<>();
        createCodeValueMap(codeValueMap, rootNode, "");
        String encodedValue = createEncodedValue(codeValueMap, fileInputAsByteArray);
        System.out.println(encodedValue);

        int extraBitsToAdd = encodedValue.length() % 8;
        byte[] resultValueInByteArray = encodeValueToByteArray(encodedValue, extraBitsToAdd);

        HuffmanData dataToSave = new HuffmanData(frequencyTable, resultValueInByteArray, extraBitsToAdd);
        Write.saveDataToFile(dataToSave, this.outputFile);
    }

    private void createCodeValueMap(Map<String, String> codeValueMap, FrequencyNode currentNode, String codeCurrentNode) {

        if(currentNode.getLeftNode() != null && currentNode.getRightNode() != null) {

            createCodeValueMap(codeValueMap, currentNode.getLeftNode(),  codeCurrentNode + "0");
            createCodeValueMap(codeValueMap, currentNode.getRightNode(),  codeCurrentNode + "1");
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

    private byte[] encodeValueToByteArray(String encodedValue, int extraBitsToAdd) {

        if(extraBitsToAdd != 0) {
            encodedValue = encodedValue + "0".repeat(extraBitsToAdd);
        }

        int encodedValueLength = encodedValue.length();
        int numberOfByte = encodedValueLength / 8;

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

        HuffmanData huffmanData = Read.convertFileToHuffmanData(this.inputFile);
        FrequencyNode rootNode = createHuffmanTree(new ArrayList<>(huffmanData.getFrequencyTable()));

        String[] decodedValue = decodeValueToString(huffmanData.getFileContentCompressed(), huffmanData.getExtraBits());
        System.out.println(Arrays.toString(decodedValue));
        byte[] decompressedValue = constructOriginalFile(decodedValue, rootNode);
        System.out.println("Decompressed : " + new String(decompressedValue));
    }

    private String[] decodeValueToString(byte[] fileContentCompressed, int extraBits) {

        StringBuilder constructValue = new StringBuilder();

        for(byte value : fileContentCompressed) {
            String valueOfByte = String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0');
            if (value < 0) {
                StringBuilder sb = new StringBuilder(valueOfByte);
                valueOfByte = sb.substring(24, 32);
            }
            if (value == fileContentCompressed[fileContentCompressed.length - 1]) {
                StringBuilder sb = new StringBuilder(valueOfByte);
                valueOfByte = sb.substring(0, 8 - extraBits);
            }
            constructValue.append(valueOfByte);
        }

        return constructValue.toString().split("(?!^)");
    }

    private byte[] constructOriginalFile(String[] decodedValue, FrequencyNode rootNode) {

        ByteArrayOutputStream decompressedValue = new ByteArrayOutputStream();
        FrequencyNode currentNode = rootNode;

        for(String value : decodedValue) {
            if(value.equals(Constants.LEFT)) {
                currentNode = currentNode.getLeftNode();
            } else if(value.equals(Constants.RIGHT)) {
                currentNode = currentNode.getRightNode();
            }

            if(currentNode.getLeftNode() == null && currentNode.getRightNode() == null) {

                try {
                    decompressedValue.write(currentNode.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                currentNode = rootNode;
            }
        }

        return decompressedValue.toByteArray();
    }
}
