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

        Map<Byte, String> codeValueMap = new HashMap<>();
        createCodeValueMap(codeValueMap, rootNode, "");
        String encodedValue = createEncodedValue(codeValueMap, fileInputAsByteArray);

        int extraBitsToAdd = 8 - (encodedValue.length() % 8);
        byte[] resultValueInByteArray = encodeValueToByteArray(encodedValue, extraBitsToAdd);

        HuffmanData dataToSave = new HuffmanData(frequencyTable, resultValueInByteArray, extraBitsToAdd);
        Write.saveDataToFile(dataToSave, this.outputFile);
    }

    private void createCodeValueMap(Map<Byte, String> codeValueMap, FrequencyNode currentNode, String codeCurrentNode) {

        if(currentNode.getLeftNode() != null && currentNode.getRightNode() != null) {

            createCodeValueMap(codeValueMap, currentNode.getLeftNode(),  codeCurrentNode + "0");
            createCodeValueMap(codeValueMap, currentNode.getRightNode(),  codeCurrentNode + "1");
        }

        codeValueMap.put(currentNode.getValue(), codeCurrentNode);
    }

    private String createEncodedValue(Map<Byte, String> codeValueMap, byte[] fileInputAsByteArray) {

        ArrayList<String> valueOfEachCharacter = new ArrayList<>();
        StringBuilder codedFileInBinary = new StringBuilder();

        for(byte byteValue : fileInputAsByteArray) {
            valueOfEachCharacter.add(codeValueMap.get(byteValue));
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
        byte[] encodedInByte = new byte[encodedValueLength / 8];

        for(int i = 0; i < encodedInByte.length; i++) {
            if((i * 8) < encodedValueLength) {
                int valueOfByte = Integer.parseInt(encodedValue.substring((i * 8), (i * 8) + 8), 2);
                encodedInByte[i] = (byte) valueOfByte;
            }
        }

        return encodedInByte;
    }

    @Override
    public void decompress() {

        HuffmanData huffmanData = Read.convertFileToHuffmanData(this.inputFile);
        FrequencyNode rootNode = createHuffmanTree(new ArrayList<>(huffmanData.getFrequencyTable()));
        byte[] decodedValue = decodeValueToString(huffmanData.getFileContentCompressed(), huffmanData.getExtraBits(), rootNode);
        Write.saveByteArrayToFile(decodedValue, this.outputFile);
    }

    private byte[] decodeValueToString(byte[] fileContentCompressed, int extraBits, FrequencyNode rootNode) {

        int numberOfBits = (fileContentCompressed.length * 8) - extraBits;
        FrequencyNode currentNode = rootNode;
        ByteArrayOutputStream decompressedValue = new ByteArrayOutputStream();

        for(int i = 0; i < numberOfBits; i++) {
            int bitValue = getBit(fileContentCompressed, i);

            if(bitValue == Constants.LEFT) {
                currentNode = currentNode.getLeftNode();
            } else if(bitValue == Constants.RIGHT) {
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

    /**
     * Retrieve the bit inside a byte from an array of byte.
     * Code provided from : http://www.herongyang.com/Java/Bit-String-Get-Bit-from-Byte-Array.html
     * @param data array of byte
     * @param pos the bit needed
     * @return the bit
     */
    private static int getBit(byte[] data, int pos) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte valByte = data[posByte];
        return valByte>>(8-(posBit+1)) & 0x0001;
    }
}
