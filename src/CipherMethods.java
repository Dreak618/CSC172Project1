
//Nicbolas Krein / Benjamin Levy
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class containing all methods used to encrypt and decrypt
 */
class CipherMethods {

        /**
         * Encrypts a provided file by:
         * - Reading the file and converting the text to a string {@code fileToString},
         * - Turning the plain text into binary {@code textToBinary}
         * - Encrypting the binary {@code encryption}
         * Then writes the encrypted binary to a new file {@code writeText}
         * 
         * @param inputFile file being encrypted
         * @param inputKey  key used for encryption
         */
        public static void encryptFile(String inputFile, String inputKey) {
                String fileAsString = fileToString(inputFile, true);
                String fileAsBinaryString = textToBinary(fileAsString);
                String encryptedBinary = encryption(fileAsBinaryString, inputKey);
                writeText(inputFile, encryptedBinary, "E"); // write the encrypted blocks to the output file
        }

        /**
         * Encrypts a binary string by:
         * - Breaking it into 64 bit blocks {@code createBlocks}
         * - Encrypting the blocks using {@code encryptBlock}
         * Then combines all blocks together and returns them
         * 
         * @param longBinaryInput binary string being encrypted
         * @param inputKey        key used for encryption
         * @return encrypted text
         */
        public static String encryption(String longBinaryInput, String inputKey) {
                ArrayList<String> Blocks = createBlocks(longBinaryInput); // List of 64 bit Strings

                Blocks.replaceAll(n -> encryptBlock(n, inputKey)); // Encrypt all blocks

                // Combine the blocks
                String encrypedBinary = "";
                for (String b : Blocks) {
                        encrypedBinary += b;
                }

                return encrypedBinary;
        }

        /**
         * Encrypts a block which is a 64 bit binary String
         * First splits the block into 2 using {@code splitIt}
         * 
         * Then does the following 10 times:
         * - Adjust key using {@code keyScheduleTransform},
         * - Apply {@code functionF} to one half of the block
         * - xOr both halfs {@code xorIt} and set the left half to that value
         * Swaps the 2 halfs
         * 
         * Finaly, combines 2 halfs together to form the encrypted block
         * 
         * @param block    64 bit String being encrypted
         * @param inputKey key used for encryption
         * @return encrypted block
         */
        public static String encryptBlock(String block, String inputKey) {
                // Split Block into 2
                String[] split = splitIt(block);
                String L = split[0];
                String R = split[1];
                String temp; // temp variable used to swap L and R

                for (int i = 0; i < 10; i++) {
                        inputKey = keyScheduleTransform(inputKey);
                        temp = functionF(R, inputKey.substring(0, 32));
                        L = xorIt(temp, L);

                        temp = L;
                        L = R;
                        R = temp;
                }

                return L + R;
        }

        /**
         * Decrypts a provided file by:
         * - Reading the file and converting the text to a string {@code fileToString},
         * - Decrypting {@code fileAsBinaryString} using {@code decryption}
         * - Converts {@code decryptedString} to plain text using {@code binaryToText}
         * Then writes the decrypted binary to a new file {@code writeText}
         * 
         * @param inputFile file being decrypted
         * @param inputKey  key used for decryption
         */
        public static void decryptFile(String inputFile, String inputKey) {
                String fileAsBinaryString = fileToString(inputFile, false);
                String decryptedString = decryption(fileAsBinaryString, inputKey);
                String binaryAsText = binaryToText(decryptedString);
                writeDecryptedBlocks(binaryAsText, inputFile);
        }

        /**
         * Decrypts a binary string by:
         * - Breaking it into 64 bit blocks {@code createBlocks}
         * - Decryting the blocks using {@code decryptBlock}
         * Then combines all blocks together and returns them
         * 
         * @param longBinaryInput binary string being decrypted
         * @param inputKey        key used for decryption
         * @return decrypted text
         */
        public static String decryption(String longBinaryInput, String inputKey) {
                ArrayList<String> Blocks = createBlocks(longBinaryInput); // List of 64 bit Strings

                Blocks.replaceAll(n -> decryptBlock(n, inputKey)); // Decrypt all blocks

                // Combine the blocks
                String decrypted = "";
                for (String b : Blocks) {
                        decrypted += b;
                }

                return decrypted;
        }

        /**
         * Decrypts a block which is a 64 bit binary String
         * First splits the block into 2 using {@code splitIt}
         * Then runs {@code keyScheduleTransform} 10 times to get the key used at the
         * end of encryption
         * 
         * Then does the following 10 times:
         * - Swaps the 2 halfs
         * - xOr both halfs {@code xorIt} and set the left half to that value
         * - Adjust key using {@code keyScheduleTransformUndo},
         * - Apply {@code functionF} to one half of the block
         * -
         * -
         * 
         * Finaly, combines 2 halfs together to form the decrypted block
         * 
         * @param block    64 bit String being decypted
         * @param inputKey key used for encryption
         * @return encrypted block
         */
        public static String decryptBlock(String block, String inputKey) {
                String[] split = splitIt(block);
                String L = split[0];
                String R = split[1];
                String temp; // temp variable used to swap L and R

                for (int i = 0; i < 10; i++) {
                        inputKey = keyScheduleTransform(inputKey); // initial key adjustment
                }
                // Decryption
                for (int i = 0; i < 10; i++) {
                        temp = L;
                        L = R;
                        R = temp;

                        L = xorIt(functionF(R, inputKey.substring(0, 32)), L);
                        inputKey = keyScheduleTransformUndo(inputKey); // transforms key for next round
                } // specifically uses Decrypter's KST here
                return L + R;
        }

        // converts binary into plain-text
        private static String binaryToText(String binaryText) {
                String plainText = "";
                // chars are in 8 bit chunks and loops until there are no chunks left
                while (binaryText.length() > 7) {
                        // gets current 8 bit chunck of binary text
                        String currentChar = binaryText.substring(0, 8);
                        // checks if current chunk is padding, if it is cuts it out
                        if (currentChar.equals("00000000")) {
                                binaryText = binaryText.substring(8);
                        } else {
                                // convert binrary chunk to char and add it to plainText String
                                char nextCharacter = (char) Integer.parseInt(currentChar, 2);
                                plainText += nextCharacter;

                                // cut out that chunk from the binary string
                                binaryText = binaryText.substring(8);
                        }
                }
                return plainText;
        }

        // write decrypted text to a new file
        private static void writeDecryptedBlocks(String text, String inputFile) {
                // create file for decrypted text
                File decryptedFile = new File(inputFile.substring(0, inputFile.length() - 5) + "D.txt");
                String decryptPath = decryptedFile.getAbsolutePath();

                // write decrypted text to file
                try (FileWriter writer = new FileWriter(decryptPath)) {
                        writer.write(text);
                        writer.close();
                } catch (IOException e) {
                        System.out.println("no file found");
                }
        }

        /**
         * Reads a file and converts the text to string
         * 
         * @param inputFile     file path of file being encrypted
         * @param keepLineBreak determines if line breaks from file should be maintained
         * @return String of file text
         */
        private static String fileToString(String inputFile, boolean keepLineBreak) {
                String fileString = "";
                try (BufferedReader reader = new BufferedReader(new FileReader(
                                inputFile))) {
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                                fileString += line;
                                // if line is not empty ,and maintaining line breaks, adds line to string
                                if (keepLineBreak && line.length() > 0) {
                                        fileString += "\n";
                                }

                        }
                } catch (IOException e) {
                        System.out.println("Error while reading file");
                }
                return fileString;
        }

        /**
         * Converts a plain text String into a binary String,
         * does this by converting each {@char} in the String to its binary equivilant.
         * Uses a scanner to read the text line by line and then adds the
         * {@string "00001010"} to the binary text if there is another line which is
         * new line in unicode
         * 
         * @param text String being converted to binary
         * @return Binary equivilant of input
         * 
         */
        private static String textToBinary(String text) {
                String binaryString = "";
                Scanner textScanner = new Scanner(text);
                String line = textScanner.nextLine();
                while (line != null) {
                        // loops through each char in the line
                        for (int i = 0; i < line.length(); i++) {
                                // convert char to binary
                                String currentCharBinary = Integer.toBinaryString(line.charAt(i));

                                // add leading 0s to char binary string if needed to make it 8 bytes
                                while (currentCharBinary.length() < 8) {
                                        currentCharBinary = "0" + currentCharBinary;
                                }
                                binaryString += currentCharBinary; // adds each character to the binary string
                        }
                        // adds next line charachter and sets next line
                        if (textScanner.hasNextLine()) {
                                binaryString += "00001010";
                                line = textScanner.nextLine();
                        } else {
                                line = null;
                        }
                }
                textScanner.close();
                return binaryString;
        }

        /**
         * Breaks a string into blocks of length 64 and then adds blocks to a list.
         * If the final block would be shorter than 64 characters,
         * adds 0s until length is 64
         * 
         * @param binaryString String being broken into blocks
         * @return List of all blocks
         */
        private static ArrayList<String> createBlocks(String binaryString) {
                ArrayList<String> Blocks = new ArrayList<String>();
                while (binaryString.length() > 0) {
                        // Pads final block with 0s if it is smaller than 64 bits
                        if (binaryString.length() < 64) {
                                while (binaryString.length() < 64) {
                                        binaryString += "0";
                                }
                        }
                        // adds each block to list, moves on to next while possible
                        Blocks.add(binaryString.substring(0, 64));
                        binaryString = binaryString.substring(64);
                }
                return Blocks;
        }

        /**
         * Writes text to a new file
         * 
         * @param inputFile the name of the file that was being read from initialy
         * @param text      the text that is being written
         * @param action    the action being done (currently E = encrypt, D = decrypt)
         */
        private static void writeText(String inputFile, String text, String action) {
                // Create a file for encrypted text
                File newFile = new File(inputFile.substring(0, inputFile.length() - 4) + action + ".txt");
                String filePath = newFile.getAbsolutePath();
                // Write the encrypted blocks to the file
                try (FileWriter writer = new FileWriter(filePath)) {
                        writer.write(text);
                        writer.close();
                } catch (IOException e) {
                        System.out.println("Error while writing to file");
                }
        }

        private static String substitutionS(String binaryInput) {
                int l = binaryInput.length();

                StringBuilder result = new StringBuilder(binaryInput.length()); // makes stringbuilder for output
                String s1 = binaryInput.substring(0, l / 4), s2 = binaryInput.substring(l / 4, l / 2),
                                s3 = binaryInput.substring(l / 2, 3 * l / 4), s4 = binaryInput.substring(3 * l / 4, l);
                String[] splits = { s1, s2, s3, s4 }; // splits input into 4 8-bit strings
                for (String s : splits) { // computes output of each string individually by looking up row/column
                                          // indices
                        int row = Integer.parseInt(s.substring(0, 4), 2);
                        int column = Integer.parseInt(s.substring(4, 8), 2);
                        result.append(sTable[row][column]); // adds output of each string to result
                }
                return result.toString(); // returns outputs (concatenated)
        }

        // split block into 2
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
                        if (binary1.charAt(i) == binary2.charAt(i)) { // if same value, then XOR is false
                                xOr.append(0);
                        } else {
                                xOr.append(1); // otherwise XOR is true
                        }
                }
                return xOr.toString();
        }

        private static String permuteIt(String binaryinput) {
                int[] p = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15,
                                23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27,
                                3, 9, 19, 13, 30, 6, 22, 11, 4, 25 }; // The given P-box
                StringBuilder sb = new StringBuilder(binaryinput.length());
                for (int i = 0; i < binaryinput.length(); i++) {
                        sb.append(binaryinput.charAt(p[i] - 1));
                        // adds the digit of b located at (the value of p[i] - 1)
                } // -1 so we don't get array out of bounds error (p is 1-32 not 0-31)
                return sb.toString();
        }

        private static String functionF(String rightHalf, String subkey) {
                // round function: XORs right Half with subkey, then splits it into 4 8-bit
                // pieces, does lookup in s-table for each, then concatenates those, permutes
                // using permutation box P
                String result = rightHalf;
                result = xorIt(rightHalf, subkey.substring(0, 32)); // round key must be 32 bits
                result = substitutionS(result);
                result = permuteIt(result);
                return result;
        }

        private static String keyScheduleTransform(String inputkey) {
                String C = shiftIt(splitIt(inputkey)[0]);
                String D = shiftIt(splitIt(inputkey)[1]);
                return C + D;
        }

        private static String shiftIt(String binaryinput) { // Left shift by 1
                StringBuilder sb = new StringBuilder(binaryinput.length());
                char e1 = binaryinput.charAt(0); // holds on to first character
                for (int i = 1; i < binaryinput.length(); i++) {
                        sb.append(binaryinput.charAt(i)); // adds other chars to result
                }
                sb.append(e1); // adds first char to end (effectively Left shift by one)
                return sb.toString();
        }

        // this one uses a right shift to generate the keys in reverse order, overrides
        // CM.kst()
        private static String keyScheduleTransformUndo(String inputkey) {
                String C = shiftItUndo(splitIt(inputkey)[0]);
                String D = shiftItUndo(splitIt(inputkey)[1]);
                return C + D;
        }

        private static String shiftItUndo(String binaryinput) { //
                StringBuilder sb = new StringBuilder(binaryinput.length());
                sb.append(binaryinput.charAt(binaryinput.length() - 1)); // adds the last character first
                for (int i = 0; i < binaryinput.length() - 1; i++) {
                        sb.append(binaryinput.charAt(i)); // then adds the rest of the chars in order
                } // effectively applies right shift by 1 to string
                return sb.toString();
        }

        private static String[][] sTable = new String[][] { // the S-Table
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

}
