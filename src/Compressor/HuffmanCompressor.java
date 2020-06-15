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

        TreeMap<Integer, ArrayList<FrequencyNode>> frequencyMap = createFrequencyMap(fileInputAsByteArray);

        FrequencyNode rootNode = createHuffmanTree(frequencyMap);
        encode(rootNode, fileInputAsByteArray);
    }

    private TreeMap<Integer, ArrayList<FrequencyNode>> createFrequencyMap(byte[] fileInputAsByteArray) {

        HashMap<Byte, Integer> frequenciesByByte = new HashMap<>();

        for(byte value : fileInputAsByteArray) {
            if(!frequenciesByByte.containsKey(value)) {
                frequenciesByByte.put(value, 1);
            } else {
                frequenciesByByte.replace(value, frequenciesByByte.get(value) + 1);
            }
        }

        TreeMap<Integer, ArrayList<FrequencyNode>> frequencyNodesByFrequency = new TreeMap<>();

        for(Map.Entry<Byte, Integer> entry : frequenciesByByte.entrySet()) {
            if(!frequencyNodesByFrequency.containsKey(entry.getValue())){
                ArrayList<FrequencyNode> frequencyNodes = new ArrayList<>();
                frequencyNodes.add(new FrequencyNode(entry.getKey(), entry.getValue()));
                frequencyNodesByFrequency.put(entry.getValue(), frequencyNodes);
            } else {
                frequencyNodesByFrequency.get(entry.getValue()).add(new FrequencyNode(entry.getKey(), entry.getValue()));
            }
        }

        return frequencyNodesByFrequency;
    }

    private FrequencyNode createHuffmanTree(TreeMap<Integer, ArrayList<FrequencyNode>> frequencyMap) {

        // Stop condition until there's only one FrequencyNode inside the list
        // This node is the root
        if(frequencyMap.size() == 1 && frequencyMap.firstEntry().getValue().size() == 1) {
            return frequencyMap.firstEntry().getValue().get(0);
        }

        // Remove the two lowest frequencies in the frequency table
        if(frequencyMap.firstEntry().getValue().size() == 0 && frequencyMap.size() >= 1) {
            frequencyMap.remove(frequencyMap.firstEntry().getKey());
        }

        FrequencyNode firstLowestFrequency = frequencyMap.firstEntry().getValue().remove(0);

        if(frequencyMap.firstEntry().getValue().size() == 0 && frequencyMap.size() >= 1) {
            frequencyMap.remove(frequencyMap.firstEntry().getKey());
        }

        if(!frequencyMap.isEmpty()) {

            FrequencyNode secondLowestFrequency = frequencyMap.firstEntry().getValue().remove(0);

            // Create parent
            FrequencyNode parentNode = new FrequencyNode(firstLowestFrequency, secondLowestFrequency);

            if(frequencyMap.containsKey(parentNode.getFrequency())) {

                frequencyMap.get(parentNode.getFrequency()).add(parentNode);

            } else {
                ArrayList<FrequencyNode> frequencyNodes = new ArrayList<>();
                frequencyNodes.add(parentNode);
                frequencyMap.put(parentNode.getFrequency(), frequencyNodes);
            }

        } else {
            ArrayList<FrequencyNode> frequencyNodes = new ArrayList<>();
            frequencyNodes.add(firstLowestFrequency);
            frequencyMap.put(firstLowestFrequency.getFrequency(), frequencyNodes);
            return firstLowestFrequency;
        }

        // Recursive
        createHuffmanTree(frequencyMap);

        return frequencyMap.firstEntry().getValue().get(0);
    }

    private void encode(FrequencyNode rootNode, byte[] fileInputAsByteArray) {

        Map<Integer, String> codeValueMap = new HashMap<>();
        createCodeValueMap(codeValueMap, rootNode, "");
        String encodedValue = createEncodedValue(codeValueMap, fileInputAsByteArray);
        System.out.println(encodedValue);

        int extraBitsToAdd = 8 - (encodedValue.length() % 8);
        byte[] resultValueInByteArray = encodeValueToByteArray(encodedValue, extraBitsToAdd);

        System.out.println(Arrays.toString(resultValueInByteArray));

        HuffmanData dataToSave = new HuffmanData(rootNode, resultValueInByteArray, extraBitsToAdd);
        Write.saveDataToFile(dataToSave, this.outputFile);
    }

    private void createCodeValueMap(Map<Integer, String> codeValueMap, FrequencyNode currentNode, String codeCurrentNode) {

        if(currentNode.getLeftNode() != null && currentNode.getRightNode() != null) {

            createCodeValueMap(codeValueMap, currentNode.getLeftNode(),  codeCurrentNode + "0");
            createCodeValueMap(codeValueMap, currentNode.getRightNode(),  codeCurrentNode + "1");
        } else {
            int byteToInt = currentNode.getValue();
            codeValueMap.put(byteToInt, codeCurrentNode);
        }
    }

    private String createEncodedValue(Map<Integer, String> codeValueMap, byte[] fileInputAsByteArray) {

        ArrayList<String> valueOfEachCharacter = new ArrayList<>();
        StringBuilder codedFileInBinary = new StringBuilder();

        for(int byteValue : fileInputAsByteArray) {
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

        System.out.println(Arrays.toString(huffmanData.getFileContentCompressed()));

        byte[] decodedValue = decodeValueToString(huffmanData.getFileContentCompressed(), huffmanData.getExtraBits(), huffmanData.getRootNode());
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
