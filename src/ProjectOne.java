//Nicholas Krein / Benjamin Levy 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProjectOne {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        runTests(); // run test cases

        // get file path, desired mode, and key from user using scanner
        Scanner s = new Scanner(System.in);
        System.out.print("Enter File Name: ");
        String inputFile = s.nextLine();
        System.out.println("enter key");
        String inputKey = s.nextLine();
        System.out.print("Do you want to encrypt or decrypt (E/D): ");
        String ED = s.next();

        // runs method to encrypt/decrypt based on user input
        if (ED.equals("E")) {
            encryptFile(inputFile, inputKey);
        } else if (ED.equals("D")) {
            decryptFile(inputFile, inputKey);
        }
        s.close();
    }

    /**
     * Runs test cases
     * Variables declared at top and tests below
     */
    private static void runTests() {
        String allOnes = "1111111111111111111111111111111111111111111111111111111111111111"; // 64 bit
        String allOnesKey = "11111111111111111111111111111111111111111111111111111111"; // 56 bit
        String allZeros = "0000000000000000000000000000000000000000000000000000000000000000"; // 64 bit
        String allZerosKey = "00000000000000000000000000000000000000000000000000000000"; // 56 bit
        String block1 = "1100110010000000000001110101111100010001100101111010001001001100"; // 64 bit
        String block2 = "0101011010001110111001000111100001001110010001100110000011110101"; // 64 bit
        String block3 = "0011000101110111011100100101001001001101011010100110011111010111"; // 64 bit

        // Test Cases
        System.out.println("Running Tests:");
        System.out.println("Output for: encryption (all ones, all ones)");
        System.out.println(encryptBlock(allOnes, allOnesKey));

        System.out.println("Output for: encryption (all zeros, all ones)");
        System.out.println(encryptBlock(allZeros, allOnesKey));

        System.out.println("Output for: encryption (all zeros, all zeros)");
        System.out.println(encryptBlock(allZeros, allZerosKey));

        System.out.println("Output for: encryption (block, all zeros), where: \n block = " + block1);
        System.out.println(encryptBlock(block1, allZerosKey));

        System.out.println("Output for: decryption (all ones, all ones)");
        System.out.println(decryptBlock(allOnes, allOnesKey));

        System.out.println("Output for: decryption(all zeros, all ones)");
        System.out.println(decryptBlock(allZeros, allOnesKey));

        System.out.println("Output for: decryption(all zeros, all zeros)");
        System.out.println(decryptBlock(allZeros, allZerosKey));

        System.out.println("Output for: decryption(block, all ones), where: \n block = " + block2);
        System.out.println(decryptBlock(block2, allOnesKey));

        System.out.println("Output for decryption(block, all zeros), where: \n block = " + block3);
        System.out.println(decryptBlock(block3, allZerosKey));
    }

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
        writeText(inputFile, binaryAsText, "D");
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
     * - Apply {@code functionF} to one half of the block
     * - xOr both halfs {@code xorIt} and set the left half to that value
     * - Adjust key using {@code keyScheduleTransformUndo},
     * Then combines 2 halfs together to form the decrypted block
     * 
     * @param block    64 bit String being decryption
     * @param inputKey key used for decryption
     * @return decrypted block
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
        }
        return L + R;
    }

    /**
     * Reads a file line by line and converts the text to string
     * Maintains the line breaks if {@code keepLineBreak} is true
     * 
     * @param inputFile     file path of file being encrypted
     * @param keepLineBreak determines if line breaks from file should be maintained
     * @return String of file text
     */
    private static String fileToString(String inputFile, boolean keepLineBreak) {
        String fileAsString = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(
                inputFile))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                fileAsString += line;
                // if line is not empty ,and maintaining line breaks, adds line to string
                if (keepLineBreak && line.length() > 0) {
                    fileAsString += "\n";
                }

            }
        } catch (IOException e) {
            System.out.println("Error while reading file");
        }
        return fileAsString;
    }

    /**
     * Converts a plain text {@code text} String into a binary String,
     * does this by converting each char in the String to its binary equivilant
     * and then adding the {@code charBinary} to {@code binaryString}
     * 
     * Some chars have a leading 0 which is removed by
     * {@code Integer.toBinaryString} To fix this, leading 0s are re-added while the
     * length of the chars binary is less than 8.
     * 
     * Uses {@code textScanner} to read the text line by line and then adds the
     * {@String "00001010"} (new line in unicode) to the binary text when
     * - The current line being read ends, and
     * - There are more lines remaining in {@code text}
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
            for (int i = 0; i < line.length(); i++) {
                String charBinary = Integer.toBinaryString(line.charAt(i));

                while (charBinary.length() < 8) {
                    charBinary = "0" + charBinary; // re-add leading 0's
                }

                binaryString += charBinary;
            }
            if (textScanner.hasNextLine()) {
                binaryString += "00001010"; // add new line binary
                line = textScanner.nextLine();
            } else {
                line = null;
            }
        }
        textScanner.close();
        return binaryString;
    }

    /**
     * Converts a binary String into plain text
     * Gets 8 bit substrings {@code currentChar}
     * from the front of {@code binaryText}
     * Checks to see if {@code currentChar} is padding and if so removed it
     * Otherwise converts the binary of that string into a char
     * using {@code (char) Integer.parseInt(currentChar, 2)}
     * Adds the char to {@code plainText}
     * Repeats until all plain text is decoded the returns the {@code plainText}
     * 
     * @param binaryText binary String being converted to chars
     * @return plain text String
     */
    private static String binaryToText(String binaryText) {
        String plainText = "";
        while (binaryText.length() > 7) {
            String currentChar = binaryText.substring(0, 8);

            if (currentChar.equals("00000000")) {// check for padding
                binaryText = binaryText.substring(8);
            } else {
                char nextCharacter = (char) Integer.parseInt(currentChar, 2);
                plainText += nextCharacter;

                binaryText = binaryText.substring(8); // cut out that chunk from binaryText
            }
        }
        return plainText;
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

    /**
     * Takes a 32 bit {@code String binaryInput}, breaks it into 4 8 bit substrings
     * {@code s1,s2,s3,s4}.
     * For each substring:
     * - Use {@code Integer.parseInt} to get the integer value of each half
     * - Sets {@code row} to be the integer value of the first half
     * - Sets {@code column} to be the integer value of the second half
     * - use the lookup table {@code sTable} and replaces the initial substring with
     * the string in position {@code row},{@code column}
     * - after replacing the substring with its corresponding value in
     * {@code sTable} uses {@code StringBuilder result} to concatinate the new
     * string with the others.
     * Returns this concatinated string
     * 
     * @param binaryInput String that will be replaced with values in {@code sTable}
     * @return replaces String
     */
    private static String substitutionS(String binaryInput) {
        int l = binaryInput.length();

        StringBuilder result = new StringBuilder(binaryInput.length());
        String s1 = binaryInput.substring(0, l / 4), s2 = binaryInput.substring(l / 4, l / 2),
                s3 = binaryInput.substring(l / 2, 3 * l / 4), s4 = binaryInput.substring(3 * l / 4, l);
        String[] splits = { s1, s2, s3, s4 };
        for (String s : splits) { // computes output of each string individually by looking up row/column
                                  // indices
            int row = Integer.parseInt(s.substring(0, 4), 2);
            int column = Integer.parseInt(s.substring(4, 8), 2);
            result.append(sTable[row][column]); // adds output of each string to result
        }
        return result.toString(); // returns outputs (concatenated)
    }

    /**
     * Split {@code input} into 2 substrings of equal size
     * Returns an array of both halfs
     * 
     * @param input String being split
     * @return Array with position 0 being the left half of {@code input}
     *         and position 1 being the right
     */
    private static String[] splitIt(String input) {
        int length = input.length();
        // break string into 2 equal parts
        String L = input.substring(0, length / 2);
        String R = input.substring(length / 2, length);
        // put 2 parts into array
        String[] split = { L, R };
        // return array
        return split;
    }

    /**
     * xOr's {@code binary1} and {@code binary2} one char at a time
     * If the char in {@code binary1} and {@code binary2} is the same
     * adds a 0 to result string
     * Otherise adds a 1 to the result string
     * 
     * @param binary1 first binary String
     * @param binary2 second binary String
     * @return String created by xOr of 2 strings
     */
    private static String xorIt(String binary1, String binary2) {
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

    /**
     * Takes an 32 bit input {@code String binaryInput} and creates a new String
     * getting the values in the {@code p} box, subtracting 1 (since p starts at one
     * but positions start at 0) and the adding the char in that position in
     * {@code binaryInput} to a new {@code StringBuilder sb}, finaly returns
     * rearranged String {@code sb.toString}
     * 
     * @param binaryInput String being read
     * @return permuted String
     */
    private static String permuteIt(String binaryInput) {
        int[] p = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15,
                23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27,
                3, 9, 19, 13, 30, 6, 22, 11, 4, 25 }; // The given P-box
        StringBuilder sb = new StringBuilder(binaryInput.length());
        for (int i = 0; i < binaryInput.length(); i++) {
            sb.append(binaryInput.charAt(p[i] - 1));
        }
        return sb.toString();
    }

    /**
     * Does the bulk of the encryping/decrypting of the binary text each round of
     * encryption/decryption
     * 
     * Takes a 32 bit binary String {@code binaryInput} and a 56 bit binary key
     * {@code key}, sets {@code result} to the {@code xorIt(binaryInput,key)},
     * Then applies {@code subsitutionS} to {@code result}
     * Then applies {@code permuteIt} to {@code result} and returns the result
     * 
     * @param binaryInput the binary text being encrypted by {@code functionF}
     * @param key         key used in the {@code xorIt}
     * @return the alterned version of {@code binaryInput}
     */
    private static String functionF(String binaryInput, String key) {
        String result = xorIt(binaryInput, key);
        result = substitutionS(result);
        result = permuteIt(result);
        return result;
    }

    /**
     * Breaks {@code inputKey} in half and applies {@code shiftIt} to each half
     * Combines the shifted Strings and returns them
     * 
     * @param inputkey String being split and shifted
     * @return shifted input
     */
    private static String keyScheduleTransform(String inputkey) {
        String C = shiftIt(splitIt(inputkey)[0]);
        String D = shiftIt(splitIt(inputkey)[1]);
        return C + D;
    }

    /**
     * Breaks {@code inputKey} in half and applies {@code shiftItUndo} to each half
     * Combines the shifted Strings and returns them
     * 
     * @param inputkey String being split and shifted
     * @return shifted input
     */
    private static String keyScheduleTransformUndo(String inputkey) {
        String C = shiftItUndo(splitIt(inputkey)[0]);
        String D = shiftItUndo(splitIt(inputkey)[1]);
        return C + D;
    }

    /**
     * Takes a String {@code input} and shifts the char at the beginning to the
     * end and moves all the other chars forward 1 spot
     * 
     * Does shift by creating a {@code StringBuilder sb} appending all but the first
     * char in {@code input} to {@code sb} and then appending the first
     * char {@code input.charAt(0)} to {@code sb}
     * 
     * @param input String being shifted
     * @return shifted input
     */
    private static String shiftIt(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 1; i < input.length(); i++) {
            sb.append(input.charAt(i));
        }
        sb.append(input.charAt(0));
        return sb.toString();
    }

    /**
     * Takes a String {@code input} and shifts the char at the end to the
     * end and moves all the other chars back 1 spot
     * 
     * Does shift by creating a {@code StringBuilder sb} appending the last char in
     * {@code input} to {@code sb} and then appending the rest of the chars in
     * {@code input} to {@code sb}
     * 
     * @param input String being shifted
     * @return shifted input
     */
    private static String shiftItUndo(String input) { //
        StringBuilder sb = new StringBuilder(input.length());
        sb.append(input.charAt(input.length() - 1));
        for (int i = 0; i < input.length() - 1; i++) {
            sb.append(input.charAt(i));
        }
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
