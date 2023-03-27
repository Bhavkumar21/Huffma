// Bhavesh Kumar
// 15 Feburary, 2023
// CSE 123 BC with Ton
// Programming Assignment 3 - Huffman
import java.util.*;
import java.io.*;

// The HuffmanCode class represents a Binary Tree that contains the HuffmanCode. This 
// class contains methods that work out the inner logistics of the Huffman Code Tree and
// allow users to compress and decompress text using the Huffman Code.
public class HuffmanCode {
    private HuffmanNode root;

    // The constructor method constructs the huffman tree using the huffman algorithm. 
    // This creates the tree using the frequencies of each charecter, making the charecters
    // with the highest frequencies have a shorter binary code.
    // @params an integer array of frequencies that represent all possible charecters in the 
    // ASCII system and the number of times they appeared in the text.
    // @returns a HuffmanCode object which is the bianry tree representing a new code for each
    // of the charecters used in the text.
    public HuffmanCode(int[] frequencies){
        Queue<HuffmanNode> pq = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++){
            if (frequencies[i] != 0){
                HuffmanNode node = new HuffmanNode(i, frequencies[i], null, null);
                pq.add(node);
            }
        }
        while (pq.size() > 1){
            HuffmanNode nodeOne = pq.remove();
            HuffmanNode nodeTwo = pq.remove();
            HuffmanNode tree = new HuffmanNode(-1, nodeOne.freq + nodeTwo.freq, 
                                                    nodeOne, nodeTwo);
            pq.add(tree);
        }
        this.root = pq.remove();
    }

    // The HuffmanCode constructor creates a binary tree using the code layed
    // out by the Scanner input. 
    // @params A Scanner object which represents the file that has the Huffman Code.
    // @returns A Huffman Code object which can be used to represent a text in huffman code.
    public HuffmanCode(Scanner input){
        this.root = new HuffmanNode();
        while (input.hasNextLine()){
            String ascii = input.nextLine();
            String binary = input.nextLine();
            HuffmanCodeHelper(Integer.parseInt(ascii), binary, this.root);
        } 
    }

    // The HuffmanCodeHelper is used to help create and set the ascii values in the
    // HuffmanCode object. This method uses the given huffman to store the ascii value
    // in the correct spot. 
    // @params an integer representing the Ascii charecter that needs to be stored
    // @params A string representing the Huffman Code binary of where it should be stored. 
    // @params HuffmanNode object representing the current node the method's examining.
    private void HuffmanCodeHelper(int ascii, String binary, HuffmanNode node){
        if (binary.length() == 0){
            node.ascii = ascii;
        }
        else{
            if (binary.substring(0,1).equals("1") ){
                if (node.right == null){
                    node.right = new HuffmanNode();
                }
                HuffmanCodeHelper(ascii, binary.substring(1,binary.length()), node.right);
            }
            else{
                if (node.left == null){
                    node.left = new HuffmanNode();
                }
                HuffmanCodeHelper(ascii, binary.substring(1,binary.length()), node.left);
            }
        }
    }

    // The save method is used to store the current Huffman Code to the given 
    // output stream in the standard format.
    // @params A printstream object which represents where the code is to be stored.
    public void save(PrintStream output){
        save(output, this.root, "");
    }

    // The save method is used to store the current Huffman Code to the given 
    // output stream in the standard format.
    // @params A printstream object which represents where the code is to be stored.
    // @params A HuffmanNode which represents the current node this method is storing.
    // @binary A string representing the Huffman Code binary of a given ASCII charecter.
    private void save(PrintStream output, HuffmanNode node, String binary){
        if (node.ascii != -1){
            output.println(node.ascii);
            output.println(binary);
        }
        else{
            save(output, node.left, binary+"0");
            save(output, node.right, binary+"1");
        }
    }

    // The method translates the input bits which are in huffman code to their 
    // repective ASCII charecters and writes it out in the given output file.
    // @params A BitInputStream object which represents the input file containing bits.
    // @params A PrintStream object which represents the output file where the ascii charecters
    // will be writen to.
    public void translate(BitInputStream input, PrintStream output){
        String fileByte = "";
        while (input.hasNextBit()){
            fileByte += input.nextBit();
            int ascii = findAscii(fileByte, this.root);
            if (ascii != -1){
                output.write((char) ascii);
                fileByte = "";
            }
        }
        
    }

    // The findAscii method finds the ascii value of the given huffman binary and 
    // returns that int value.
    // @params A string representing the Huffman Binary hashCode
    // @params A HuffmanNode representing the current node we are looing at.
    // @returns An int value which is the Ascii value that is produced by the Huffman Code.
    private int findAscii(String binary, HuffmanNode node){
        if (binary.length() == 0){
            return node.ascii;

        }
        else if (node.right != null && node.left != null){
            if (binary.substring(0,1).equals("1") ){
                return findAscii(binary.substring(1,binary.length()), node.right);
            }
            else{
                return findAscii(binary.substring(1,binary.length()), node.left);
            }
        }
        return -1;
    }

    // The HuffmanNode class creates a node that can be used to construct the 
    // HuffmanCode object. This class implements the Comparable interface inorder 
    // to allow for HuffmanNode comparisions.
    static class HuffmanNode implements Comparable<HuffmanNode>{
        public int ascii; 
        public int freq;
        public HuffmanNode left; 
        public HuffmanNode right; 

        // The constructor is used to create an empty node.
        // @returns A HuffmanNode object
        public HuffmanNode(){
            this(-1, -1 , null, null);
        }

        // The constructor is used to create and assign values to the node object.
        // @params an integer value representing an ascii charecter
        // @params an integer representing the number of frequencies that charecter
        // was used in a given text.
        // @params A huffmanNode object representing the left child node
        // @params A huffmanNode object representing the right child node
        // @returns A HuffmanNode object
        public HuffmanNode(int ascii, int freq, HuffmanNode left, HuffmanNode right) {
            this.ascii = ascii;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // The compareTo method is used to compare different HuffmanNodes. These nodes
        // are compared based on the frequencies with which their ascii charecters are used
        // in a text. The highest frequencies of the two nodes is greater. 
        // @params A HuffmanNode which is being compared.
        public int compareTo(HuffmanNode node){
            if (this.freq > node.freq){
                return 1;
            }
            else if (this.freq < node.freq){
                return -1;
            }
            else{
                return 0;
            }
        }
    }
}
