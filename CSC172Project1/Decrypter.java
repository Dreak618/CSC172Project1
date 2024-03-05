import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Decrypter {
        private ArrayList<String> Blocks = new ArrayList<String>();
        private String currentBlock = "";

        public Decrypter(String inputFilePath, String inputKey) {
                // String binaryText = decryptText(inputFilePath); // NYI
                String outputText = "";
                createBlocks(inputFilePath);
                for (String block : Blocks) {
                        block = binaryToText(decryptBlock(block, inputKey));
                        outputText += block;
                }
                writeDecryptedBlocks(outputText, inputFilePath);

                // Convert the decrypted binary into text
                // Write decrypted binary to file
        }

        /*
         * TODO: currently just grabs the binary and adds it to binary string since it
         * is not yet encrypted, when encryption added will need to decrupt before line
         * added to binary text
         */

        // private String decryptText(String inputFilePath) { -- OLD
        // String binaryTest = "";
        // try (BufferedReader reader = new BufferedReader(new
        // FileReader(inputFilePath))) {
        // String line;
        // while ((line = reader.readLine()) != null) {
        // // TODO: decrypt line here
        // binaryTest += line;
        // }
        // reader.close();
        // } catch (IOException e) {
        // System.out.println("Error reading file while encrypting");
        // }
        // return binaryTest;
        // }

        private void createBlocks(String inputFilePath) {
                // Create a buffered file reader
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
                        String line = "";
                        // Takes every line in the file and converts the plain-text into 64bit binary
                        // blocks
                        while ((line = reader.readLine()) != null) {
                                for (int i = 0; i < line.length(); i++) {
                                        currentBlock += line.charAt(i);
                                        if (currentBlock.length() % 64 == 0 && currentBlock.length() != 0) {
                                                Blocks.add(currentBlock);
                                                currentBlock = "";
                                        }
                                } // assuming no need to pad in this direction

                        }
                        reader.close();
                } catch (IOException e) {
                        System.out.println("Error reading file while encrypting");
                }
        }

        public static String decryptBlock(String block, String inputKey) {
                String[] split = splitIt(block);
                String L = split[0];
                String R = split[1];
                String temp; // temp variable used to swap L,R halves after each iteration
                // for (int i = 10; i > 0; i--) {

                // R = xorIt(R, L);

                // inputKey = keyScheduleTransform(inputKey);
                R = functionF(R, inputKey);
                temp = L; // swap L, R
                L = R;
                R = temp;
                // }

                return R + L;

        }

        // converts binary into plain-text
        public String binaryToText(String binaryText) {
                String plainText = "";
                // chars are in 8 bit chunks and loops until there are no chunks left
                while (binaryText.length() > 7) {
                        // gets current 8 but chunck of binary text
                        String currentChar = binaryText.substring(0, 8);
                        // checks if that chunk of binary text is padding
                        if (currentChar.equals("00000000")) {
                                // if it is cuts out the padding
                                if (binaryText.length() > 7) { // so it doesnt break if last char is "00000000"
                                        binaryText = binaryText.substring(8);
                                }
                                // 00000000 it doesn't break everything
                        } else {
                                // if not, get the character associated with the given binary value
                                char nextCharacter = (char) Integer.parseInt(currentChar, 2);
                                // adds that character to plain text string
                                plainText += nextCharacter;
                                // cut out that character from the binary string
                                binaryText = binaryText.substring(8);
                        }
                }
                return plainText;
        }

        // write decrypted text to a new file
        private void writeDecryptedBlocks(String text, String inputFilePath) {
                // create file for decrypted text
                File decryptedFile = new File(inputFilePath + ".decrypted");
                String decryptPath = decryptedFile.getAbsolutePath();

                // write decrypted text to file
                try (FileWriter writer = new FileWriter(decryptPath)) {
                        writer.write(text);
                        writer.close();
                } catch (IOException e) {
                        System.out.println("no file found");
                }
        }

        private static String substitutionS(String binaryInput) {

                StringBuilder result = new StringBuilder(binaryInput.length());
                String[] sp2 = splitIt(binaryInput);
                String[][] splits = { splitIt(sp2[0]), splitIt(sp2[1]) }; // split string into 4 parts (8bit binary
                                                                          // each)
                for (int i = 0; i < splits.length; i++) {
                        for (int j = 0; j < (splits[0].length); j++) { // for each string, turn it into 2 binary numbers
                                                                       // (by
                                                                       // substring), then use to lookup value in
                                                                       // S-Table
                                splits[i][j] = sTable[Integer.parseInt(splits[i][j].substring(0, 03), 2)][Integer
                                                .parseInt(splits[i][j].substring(4, 8), 2)];
                                result.append(splits[i][j]);
                        }
                }
                return result.toString();
        }

        // split string into 2 equal length strings
        private static String[] splitIt(String block) {
                int length = block.length();
                // break string into 2 equal parts
                String L = block.substring(0, length / 2);
                String R = block.substring(length / 2, length);
                // put 2 parts into array
                String[] split = { L, R };
                // return array
                return split;
        }

        private static String xorIt(String binary1, String binary2) { // binary2 is the round key ki
                StringBuilder xOr = new StringBuilder(binary1.length());
                for (int i = 0; (i < binary1.length() && i < binary2.length()); i++) {
                        if (binary1.charAt(i) == binary2.charAt(i)) {
                                xOr.append(0);
                        } else {
                                xOr.append(1);
                        }
                }
                return xOr.toString();

        }

        private static String shiftIt(String binaryinput) {
                StringBuilder sb = new StringBuilder(binaryinput.length());
                char[] b = binaryinput.toCharArray();
                sb.append(b[b.length - 1]);
                for (int i = 0; i < binaryinput.length() - 1; i++) {
                        sb.append(b[i]);
                }
                return sb.toString();
        }

        private static String permuteIt(String binaryinput) {
                int[] p = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15,
                                23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27,
                                3, 9, 19, 13, 30, 6, 22, 11, 4, 25 }; // The given P-box

                int[] inverseP = { 9, 17, 23, 31, 13, 28, 2, 18, 24, 16,
                                30, 6, 26, 20, 10, 1, 8, 14, 25, 3, 4, 29,
                                11, 19, 32, 12, 22, 7, 5, 27, 15, 21 }; // 21 in correct spot?
                StringBuilder sb = new StringBuilder(binaryinput.length() + 1);
                for (int i = 0; i < binaryinput.length(); i++) {
                        sb.append(binaryinput.charAt(inverseP[i] - 1));
                        // adds the digit of b located at (the value of p[i] - 1)
                } // -1 so we don't get array out of bounds error
                return sb.toString();
        }

        private static String functionF(String rightHalf, String subkey) {
                // TODO: make sure the function works (can't check that until we have decryption
                // lol)
                // Look at suggestion in substitutionS
                // subkey = keyScheduleTransform(subkey); // do this first to create this
                // iteration's round key
                String result = rightHalf;
                // result = permuteIt(result); // round key must be 32
                result = substitutionS(result);
                // result = xorIt(result, subkey.substring(0, 32));
                // bits
                return result;
        }

        private static String keyScheduleTransform(String inputkey) {
                String C = shiftIt(splitIt(inputkey)[0]);
                String D = shiftIt(splitIt(inputkey)[1]);
                return C + D;
        }

        public static String[][] sTable = new String[][] { // the S-Table - TODO: make private later (both this and
                                                           // inverse)
                        { "01100011", "01111100", "01110111", "01111011", "11110010", "01101011", "01101111",
                                        "11000101",
                                        "00110000", "00000001", "01100111", "00101011", "11111110", "11010111",
                                        "10101011", "01110110" },
                        { "11001010", "10000010", "11001001", "01111101", "11111010", "01011001", "01000111",
                                        "11110000",
                                        "10101101", "11010100", "10100010", "10101111", "10011100", "10100100",
                                        "01110010", "11000000" },
                        { "10110111", "11111101", "10010011", "00100110", "00110110", "00111111", "11110111",
                                        "11001100",
                                        "00110100", "10100101", "11100101", "11110001", "01110001", "11011000",
                                        "00110001", "00010101" },
                        { "00000100", "11000111", "00100011", "11000011", "00011000", "10010110", "00000101",
                                        "10011010",
                                        "00000111", "00010010", "10000000", "11100010", "11101011", "00100111",
                                        "10110010", "01110101" },
                        { "00001001", "10000011", "00101100", "00011010", "00011011", "01101110", "01011010",
                                        "10100000",
                                        "01010010", "00111011", "11010110", "10110011", "00101001", "11100011",
                                        "00101111", "10000100" },
                        { "01010011", "11010001", "00000000", "11101101", "00100000", "11111100", "10110001",
                                        "01011011",
                                        "01101010", "11001011", "10111110", "00111001", "01001010", "01001100",
                                        "01011000", "11001111" },
                        { "11010000", "11101111", "10101010", "11111011", "01000011", "01001101", "00110011",
                                        "10000101",
                                        "01000101", "11111001", "00000010", "01111111", "01010000", "00111100",
                                        "10011111", "10101000" },
                        { "01010001", "10100011", "01000000", "10001111", "10010010", "10011101", "00111000",
                                        "11110101",
                                        "10111100", "10110110", "11011010", "00100001", "00010000", "11111111",
                                        "11110011", "11010010" },
                        { "11001101", "00001100", "00010011", "11101100", "01011111", "10010111", "01000100",
                                        "00010111",
                                        "11000100", "10100111", "01111110", "00111101", "01100100", "01011101",
                                        "00011001", "01110011" },
                        { "01100000", "10000001", "01001111", "11011100", "00100010", "00101010", "10010000",
                                        "10001000",
                                        "01000110", "11101110", "10111000", "00010100", "11011110", "01011110",
                                        "00001011", "11011011" },
                        { "11100000", "00110010", "00111010", "00001010", "01001001", "00000110", "00100100",
                                        "01011100",
                                        "11000010", "11010011", "10101100", "01100010", "10010001", "10010101",
                                        "11100100", "01111001" },
                        { "11100111", "11001000", "00110111", "01101101", "10001101", "11010101", "01001110",
                                        "10101001",
                                        "01101100", "01010110", "11110100", "11101010", "01100101", "01111010",
                                        "10101110", "00001000" },
                        { "10111010", "01111000", "00100101", "00101110", "00011100", "10100110", "10110100",
                                        "11000110",
                                        "11101000", "11011101", "01110100", "00011111", "01001011", "10111101",
                                        "10001011", "10001010" },
                        { "01110000", "00111110", "10110101", "01100110", "01001000", "00000011", "11110110",
                                        "00001110",
                                        "01100001", "00110101", "01010111", "10111001", "10000110", "11000001",
                                        "00011101", "10011110" },
                        { "11100001", "11111000", "10011000", "00010001", "01101001", "11011001", "10001110",
                                        "10010100",
                                        "10011011", "00011110", "10000111", "11101001", "11001110", "01010101",
                                        "00101000", "11011111" },
                        { "10001100", "10100001", "10001001", "00001101", "10111111", "11100110", "01000010",
                                        "01101000",
                                        "01000001", "10011001", "00101101", "00001111", "10110000", "01010100",
                                        "10111011", "00010110" }

        };
        private static String[][] inverseSTable = {
                        { "01100011", "11001010", "10110111", "00000100", "00001001", "01010011", "11010000",
                                        "01010001", "11001101", "01100000", "11100000", "11100111", "10111010",
                                        "01110000", "11100001", "10001100" },
                        { "01111100", "10000010", "11111101", "11000111", "10000011", "11010001", "11101111",
                                        "10100011", "00001100", "10000001", "00110010", "11001000", "01111000",
                                        "00111110", "11111000", "10100001" },
                        { "01110111", "11001001", "10010011", "00100011", "00101100", "00000000", "10101010",
                                        "01000000", "00010011", "01001111", "00111010", "00110111", "00100101",
                                        "10110101", "10011000", "10001001" },
                        { "01111011", "01111101", "00100110", "11000011", "00011010", "11101101", "11111011",
                                        "10001111", "11101100", "11011100", "00001010", "01101101", "00101110",
                                        "01100110", "00010001", "00001101" },
                        { "11110010", "11111010", "00110110", "00011000", "00011011", "00100000", "01000011",
                                        "10010010", "01011111", "00100010", "01001001", "10001101", "00011100",
                                        "01001000", "01101001", "10111111" },
                        { "01101011", "01011001", "00111111", "10010110", "01101110", "11111100", "01001101",
                                        "10011101", "10010111", "00101010", "00000110", "11010101", "10100110",
                                        "00000011", "11011001", "11100110" },
                        { "01101111", "01000111", "11110111", "00000101", "01011010", "10110001", "00110011",
                                        "00111000", "01000100", "10010000", "00100100", "01001110", "10110100",
                                        "11110110", "10001110", "01000010" },
                        { "11000101", "11110000", "11001100", "10011010", "10100000", "01011011", "10000101",
                                        "11110101", "00010111", "10001000", "01011100", "10101001", "11000110",
                                        "00001110", "10010100", "01101000" },
                        { "00110000", "10101101", "00110100", "00000111", "01010010", "01101010", "01000101",
                                        "10111100", "11000100", "01000110", "11000010", "01101100", "11101000",
                                        "01100001", "10011011", "01000001" },
                        { "00000001", "11010100", "10100101", "00010010", "00111011", "11001011", "11111001",
                                        "10110110", "10100111", "11101110", "11010011", "01010110", "11011101",
                                        "00110101", "00011110", "10011001" },
                        { "01100111", "10100010", "11100101", "10000000", "11010110", "10111110", "00000010",
                                        "11011010", "01111110", "10111000", "10101100", "11110100", "01110100",
                                        "01010111", "10000111", "00101101" },
                        { "00101011", "10101111", "11110001", "11100010", "10110011", "00111001", "01111111",
                                        "00100001", "00111101", "00010100", "01100010", "11101010", "00011111",
                                        "10111001", "11101001", "00001111" },
                        { "11111110", "10011100", "01110001", "11101011", "00101001", "01001010", "01010000",
                                        "00010000", "01100100", "11011110", "10010001", "01100101", "01001011",
                                        "10000110", "11001110", "10110000" },
                        { "11010111", "10100100", "11011000", "00100111", "11100011", "01001100", "00111100",
                                        "11111111", "01011101", "01011110", "10010101", "01111010", "10111101",
                                        "11000001", "01010101", "01010100" },
                        { "10101011", "01110010", "00110001", "10110010", "00101111", "01011000", "10011111",
                                        "11110011", "00011001", "00001011", "11100100", "10101110", "10001011",
                                        "00011101", "00101000", "10111011" },
                        { "01110110", "11000000", "00010101", "01110101", "10000100", "11001111", "10101000",
                                        "11010010", "01110011", "11011011", "01111001", "00001000", "10001010",
                                        "10011110", "11011111", "00010110" }
        };
}