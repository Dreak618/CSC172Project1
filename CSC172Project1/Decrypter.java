import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Decrypter {

        private String currentBlock = "";

        public Decrypter(String inputFilePath, String inputKey) {
                ArrayList<String> Blocks = createBlocks(inputFilePath);
                // String binaryText = decryptText(inputFilePath); // NYI
                String outputText = "";
                createBlocks(inputFilePath);
                for (String block : Blocks) {
                        System.out.println(block + " pre decrypt");
                        block = decryptBlock(block, inputKey);
                        System.out.println(block + " post decrypt");
                        block = binaryToText(block);
                        System.out.println(block + " to text \n");
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

        private ArrayList<String> createBlocks(String inputFilePath) {
                ArrayList<String> Blocks = new ArrayList<String>();
                // Create a buffered file reader
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
                        String line = "";
                        String blockLine = "";
                        // Takes every line in the file and converts the plain-text into 64bit binary
                        // blocks
                        while ((line = reader.readLine()) != null) {
                                blockLine+=line;
                                // for (int i = 0; i < line.length(); i++) {
                                //         currentBlock += line.charAt(i);
                                //         if (currentBlock.length() % 64 == 0 && currentBlock.length() != 0) {
                                //                 Blocks.add(currentBlock);
                                //                 currentBlock = "";
                                //         }
                                // } // assuming no need to pad in this direction
                        }
                        while (blockLine.length() > 0){
                                Blocks.add(blockLine.substring(0,64));
                                blockLine = blockLine.substring(64);
                        }

                        reader.close();
                } catch (IOException e) {
                        System.out.println("Error reading file while encrypting");
                }
                return Blocks;
        }

        public static String decryptBlock(String block, String inputKey) {
                String[] split = CipherMethods.splitIt(block);
                String L = split[0];
                String R = split[1];
                String temp; // temp variable used to swap L,R halves after each iteration
                for (int i = 0; i < 10; i++) {

                        R = CipherMethods.xorIt(R, L);
                        R = functionF(R, inputKey);
                        inputKey = keyScheduleTransform(inputKey);
                        temp = L; // swap L, R
                        L = R;
                        R = temp;
                }

                return L + R;

        }

        // converts binary into plain-text
        public static String binaryToText(String binaryText) {
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
                        } else  {
                                // if not, get the character associated with the given binary value
                                // System.out.println("next char binary " + currentChar);
                                char nextCharacter = (char) Integer.parseInt(currentChar, 2);
                                // System.out.println("next char " + nextCharacter);
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
                int l = binaryInput.length();
                StringBuilder result = new StringBuilder(binaryInput.length());
                String s1 = binaryInput.substring(0, l / 4), s2 = binaryInput.substring(l / 4, l / 2),
                                s3 = binaryInput.substring(l / 2, 3 * l / 4), s4 = binaryInput.substring(3 * l / 4, l);
                String[] splits = { s1, s2, s3, s4 };
                for (String s : splits) {
                        int row = Integer.parseInt(s.substring(0, 4), 2);
                        int column = Integer.parseInt(s.substring(4), 2);
                        result.append(inverseSTable[row][column]);
                }
                return result.toString();

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

                // iteration's round key
                String result = rightHalf;

                result = permuteIt(result); // round key must be 32
                result = substitutionS(result);

                result = CipherMethods.xorIt(result, subkey.substring(0, 32));
                // bits
                return result;
        }

        private static String keyScheduleTransform(String inputkey) {
                String C = shiftIt(CipherMethods.splitIt(inputkey)[0]);
                String D = shiftIt(CipherMethods.splitIt(inputkey)[1]);
                return C + D;
        }

        private static String[][] sTable = new String[][] {
                        { "01100011", "01111100", "01110111", "01111011", "11110010", "01101011", "01101111",
                                        "11000101", "00110000", "00000001", "01100111", "00101011", "11111110",
                                        "11010111", "10101011", "01110110" },
                        { "11001010", "10000010", "11001001", "01111101", "11111010", "01011001", "01000111",
                                        "11110000", "10101101", "11010100", "10100010", "10101111", "10011100",
                                        "10100100", "01110010", "11000000" },
                        { "10110111", "11111101", "10010011", "00100110", "00110110", "00111111", "11110111",
                                        "11001100", "00110100", "10100101", "11100101", "11110001", "01110001",
                                        "11011000", "00110001", "00010101" },
                        { "00000100", "11000111", "00100011", "11000011", "00011000", "10010110", "00000101",
                                        "10011010", "00000111", "00010010", "10000000", "11100010", "11101011",
                                        "00100111", "10110010", "01110101" },
                        { "00001001", "10000011", "00101100", "00011010", "00011011", "01101110", "01011010",
                                        "10100000", "01010010", "00111011", "11010110", "10110011", "00101001",
                                        "11100011", "00101111", "10000100" },
                        { "01010011", "11010001", "00000000", "11101101", "00100000", "11111100", "10110001",
                                        "01011011", "01101010", "11001011", "10111110", "00111001", "01001010",
                                        "01001100", "01011000", "11001111" },
                        { "11010000", "11101111", "10101010", "11111011", "01000011", "01001101", "00110011",
                                        "10000101", "01000101", "11111001", "00000010", "01111111", "01010000",
                                        "00111100", "10011111", "10101000" },
                        { "01010001", "10100011", "01000000", "10001111", "10010010", "10011101", "00111000",
                                        "11110101", "10111100", "10110110", "11011010", "00100001", "00010000",
                                        "11111111", "11110011", "11010010" },
                        { "11001101", "00001100", "00010011", "11101100", "01011111", "10010111", "01000100",
                                        "00010111", "11000100", "10100111", "01111110", "00111101", "01100100",
                                        "01011101", "00011001", "01110011" },
                        { "01100000", "10000001", "01001111", "11011100", "00100010", "00101010", "10010000",
                                        "10001000", "01000110", "11101110", "10111000", "00010100", "11011110",
                                        "01011110", "00001011", "11011011" },
                        { "11100000", "00110010", "00111010", "00001010", "01001001", "00000110", "00100100",
                                        "01011100", "11000010", "11010011", "10101100", "01100010", "10010001",
                                        "10010101", "11100100", "01111001" },
                        { "11100111", "11001000", "00110111", "01101101", "10001101", "11010101", "01001110",
                                        "10101001", "01101100", "01010110", "11110100", "11101010", "01100101",
                                        "01111010", "10101110", "00001000" },
                        { "10111010", "01111000", "00100101", "00101110", "00011100", "10100110", "10110100",
                                        "11000110", "11101000", "11011101", "01110100", "00011111", "01001011",
                                        "10111101", "10001011", "10001010" },
                        { "01110000", "00111110", "10110101", "01100110", "01001000", "00000011", "11110110",
                                        "00001110", "01100001", "00110101", "01010111", "10111001", "10000110",
                                        "11000001", "00011101", "10011110" },
                        { "11100001", "11111000", "10011000", "00010001", "01101001", "11011001", "10001110",
                                        "10010100", "10011011", "00011110", "10000111", "11101001", "11001110",
                                        "01010101", "00101000", "11011111" },
                        { "10001100", "10100001", "10001001", "00001101", "10111111", "11100110", "01000010",
                                        "01101000", "01000001", "10011001", "00101101", "00001111", "10110000",
                                        "01010100", "10111011", "00010110" }
        };
        private static String[][] inverseSTable = new String[][] { // I did this inverse by hand, and I hate myself so
                                                                   // much right now
                        { "01010010", "00001001", "01101010", "11010101", "00110000", "00110110", "10100101",
                                        "00111000", "10111111", "01000000", "10100011", "10011110", "10000001",
                                        "11110011", "11010111", "11111011" },
                        { "01111100", "11100011", "00111001", "10000010", "10011011", "00101111", "11111111",
                                        "10000111", "00110100", "10001110", "01000011", "01000100", "11000100",
                                        "11011110", "11101001", "11001011" },
                        { "01010100", "01111011", "10010100", "00110010", "10100110", "11000010", "00100011",
                                        "00111101", "11101110", "01001100", "10010101", "00001011", "01000010",
                                        "11111010", "11000011", "01001110" },
                        { "00001000", "00101110", "10100001", "01100110", "00101000", "11011001", "00100100",
                                        "10110010", "01110110", "01011011", "10100010", "01001001", "01101101",
                                        "10001011", "11010001", "00100101" },
                        { "01110010", "11111000", "11110110", "01100100", "10000110", "01101000", "10011000",
                                        "00010110", "11010100", "10100100", "01011100", "11001100", "01011101",
                                        "01100101", "10110110", "10010010" },
                        { "01101100", "01110000", "01001000", "01010000", "11111101", "11101101", "10111001",
                                        "01010111", "01011110", "00010101", "01000110", "01010111", "10100111",
                                        "10001101", "10011101", "10000100" },
                        { "10010000", "11011000", "10101011", "00000000", "10001100", "10111100", "11010011",
                                        "00001010", "11110111", "11100100", "01011000", "00000101", "10111000",
                                        "10110011", "01000101", "00000110" },
                        { "11010000", "00101100", "00011110", "10001111", "11001010", "00111111", "00001111",
                                        "00000010", "11000001", "10101111", "10111101", "00000011", "00000001",
                                        "00010011", "10001010", "01101011" },
                        { "00111010", "10010001", "00010001", "01000001", "01001111", "01100111", "11011100",
                                        "11101010", "10010111", "11110010", "11001111", "11001110", "11110000",
                                        "10110100", "11100110", "01110011" },
                        { "10010110", "10101100", "01110100", "00100010", "11100111", "10101101", "00110101",
                                        "10000101", "11100010", "11111001", "00110111", "11101000", "00011100",
                                        "01110101", "11011111", "01101110" },
                        { "01000111", "11110001", "00011010", "01110001", "00011101", "00101001", "11000101",
                                        "10001001", "01101111", "10110111", "01100010", "00001110", "10101010",
                                        "00011000", "10111110", "00011011" },
                        { "11111100", "01010110", "00111110", "01001011", "11000110", "11010010", "01111001",
                                        "00100000", "10011010", "11011011", "11000000", "11111110", "01111000",
                                        "11001101", "01011010", "11110100" },
                        { "00011111", "11011101", "10101000", "00110011", "10001000", "00000111", "11000111",
                                        "00110001", "10110001", "00010010", "00010000", "01011001", "00100111",
                                        "10000000", "11101100", "01011111" },
                        { "01100000", "01010001", "01111111", "10101001", "00011001", "10110101", "01001010",
                                        "00001101", "00101101", "11100101", "01111010", "10011111", "10010011",
                                        "11001001", "10011100", "11101111" },
                        { "10100000", "11100000", "00111011", "01001101", "10101110", "00101010", "11110101",
                                        "10110000", "11001000", "11101011", "10111011", "00111100", "10000011",
                                        "01010011", "10011001", "01100001" },
                        { "00010111", "00101011", "00000100", "01111110", "10111010", "01110111", "11010110",
                                        "00100110", "11100001", "01101001", "00010100", "01100011", "01010101",
                                        "00100001", "00001100", "01111101" },
        };
}