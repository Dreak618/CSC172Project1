import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//import javax.swing.InputMap;

public class Encrypter {

    private static int charCount = 0; // used to keep track of how many chars in blocks

    public Encrypter(String inputFilePath, String inputKey) {
        ArrayList<String> BlocksPreEncypt = createBlocks(inputFilePath);
        ArrayList<String> Blocks = new ArrayList<>();
        // take the file and break it into blocks
        // encrypt the blocks
        for (String block : BlocksPreEncypt) {
            Blocks.add(encryptBlock(block, inputKey));
        }
        // write encrypted blocks to file
        writeBlocks(inputFilePath, Blocks);
    }

    // takes plain text and breaks it into 64 bit blocks
    private static ArrayList<String> createBlocks(String inputFilePath) {
        ArrayList<String> Blocks = new ArrayList<String>();
        // Create a buffered file reader
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String binaryLine = "";
            String line = "";
            // Takes every line in the file and converts the plain-text into 64bit binary
            // blocks
            while ((line = reader.readLine()) != null) {
                binaryLine += lineToBinary(line);
            }
            // Pad final block with 0s if not full
            if (charCount % 8 != 0) {
                while (charCount % 8 != 0) {
                    charCount++;
                    binaryLine += "00000000";
                }
            }
            while (binaryLine.length() > 0) {
                Blocks.add(binaryLine.substring(0, 64));
                binaryLine = binaryLine.substring(64);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file while encrypting");
        }
        return Blocks;
    }

    // convert chars in line to 8 bit binary strings
    private static String lineToBinary(String line) {
        // Loops through all chars in line
        String binary = "";
        for (int i = 0; i < line.length(); i++) {
            // increase the count of chars written, used to keeping track of block size
            charCount++;
            // Convert char to binary
            String currentCharBinary = Integer.toBinaryString(line.charAt(i));

            // add leading 0s to char binary string if needed to make it 8 bytes
            while (currentCharBinary.length() < 8) {
                currentCharBinary = "0" + currentCharBinary;
            }
            binary += currentCharBinary;
        }
        return binary;
    }

    // encyrpts 1 block at a time
    public static String encryptBlock(String block, String inputKey) {
        // Split Block into 2 32 bit strings
        String[] split = CipherMethods.splitIt(block);
        String L = split[0];
        String R = split[1];
        String temp; // temp variable used to swap L,R halves after each iteration
        // Steps to encrypt a block: Done 10 times to encrypt
        for (int i = 0; i < 10; i++) {
            // // swap L and R
            temp = L;
            L = R;
            R = temp;
            // do round function to R
            inputKey = keyScheduleTransform(inputKey); // do this first to create this
            // iteration's round key
            R = functionF(R, inputKey); // updated to use the cipher methods within
            // Encrypter, getting rid of
            // CipherMethods class
            // make R equal R xOR L
            R = CipherMethods.xorIt(R, L);
            System.out.println(L + R + " L+R");

        }

        // return ecrypted block
        return L + R;

    }

    // writes blocks to file
    private static void writeBlocks(String inputFilePath, ArrayList<String> Blocks) {
        // Create a file for encrypted text
        File encryptedFile = new File(inputFilePath + ".encrypted");
        String encryptPath = encryptedFile.getAbsolutePath();
        // Write the encrypted blocks to the file
        try (FileWriter writer = new FileWriter(encryptPath)) {
            for (String block : Blocks) {
                writer.write(block + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file while encrypting");
        }
    }

    private static String substitutionS(String binaryInput) {
        int l = binaryInput.length();

        StringBuilder result = new StringBuilder(binaryInput.length());
        String s1 = binaryInput.substring(0, l / 4), s2 = binaryInput.substring(l / 4, l / 2),
                s3 = binaryInput.substring(l / 2, 3 * l / 4), s4 = binaryInput.substring(3 * l / 4, l); // I'm using a
                                                                                                        // 2d
        String[] splits = { s1, s2, s3, s4 };
        for (String s : splits) {
            int row = Integer.parseInt(s.substring(0, 4), 2);
            int column = Integer.parseInt(s.substring(4, 8), 2);
            result.append(sTable[row][column]);
        }
        return result.toString();
    }

    private static String shiftIt(String binaryinput) {
        StringBuilder sb = new StringBuilder(binaryinput.length());
        char[] b = binaryinput.toCharArray();
        char e1 = b[0];
        for (int i = 1; i < binaryinput.length(); i++) {
            sb.append(b[i]);
        }
        sb.append(e1);
        return sb.toString();
    }

    private static String permuteIt(String binaryinput) {
        int[] p = { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15,
                23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27,
                3, 9, 19, 13, 30, 6, 22, 11, 4, 25 }; // The given P-box
        StringBuilder sb = new StringBuilder(binaryinput.length());
        for (int i = 0; i < binaryinput.length(); i++) {
            sb.append(binaryinput.charAt(p[i] - 1));
            // adds the digit of b located at (the value of p[i] - 1)
        } // -1 so we don't get array out of bounds error
        return sb.toString();
    }

    private static String functionF(String rightHalf, String subkey) {
        // round function: XORs right Half with subkey, then splits it into 4 8-bit
        // pieces, does lookup in s-table for each, then concatenates those, permutes
        // using P
        // TODO: make sure the function works (can't check that until we have decryption
        // lol)
        String result = rightHalf;
        result = CipherMethods.xorIt(rightHalf, subkey.substring(0, 32));
        result = substitutionS(result);
        result = permuteIt(result);
        // round key must be 32 bits
        return result;

    }

    private static String keyScheduleTransform(String inputkey) {
        String C = shiftIt(CipherMethods.splitIt(inputkey)[0]);
        String D = shiftIt(CipherMethods.splitIt(inputkey)[1]);
        return C + D;
    }

    private static String[][] sTable = new String[][] {
            { "01100011", "01111100", "01110111", "01111011", "11110010", "01101011", "01101111", "11000101",
                    "00110000", "00000001", "01100111", "00101011", "11111110", "11010111", "10101011", "01110110" },
            { "11001010", "10000010", "11001001", "01111101", "11111010", "01011001", "01000111", "11110000",
                    "10101101", "11010100", "10100010", "10101111", "10011100", "10100100", "01110010", "11000000" },
            { "10110111", "11111101", "10010011", "00100110", "00110110", "00111111", "11110111", "11001100",
                    "00110100", "10100101", "11100101", "11110001", "01110001", "11011000", "00110001", "00010101" },
            { "00000100", "11000111", "00100011", "11000011", "00011000", "10010110", "00000101", "10011010",
                    "00000111", "00010010", "10000000", "11100010", "11101011", "00100111", "10110010", "01110101" },
            { "00001001", "10000011", "00101100", "00011010", "00011011", "01101110", "01011010", "10100000",
                    "01010010", "00111011", "11010110", "10110011", "00101001", "11100011", "00101111", "10000100" },
            { "01010011", "11010001", "00000000", "11101101", "00100000", "11111100", "10110001", "01011011",
                    "01101010", "11001011", "10111110", "00111001", "01001010", "01001100", "01011000", "11001111" },
            { "11010000", "11101111", "10101010", "11111011", "01000011", "01001101", "00110011", "10000101",
                    "01000101", "11111001", "00000010", "01111111", "01010000", "00111100", "10011111", "10101000" },
            { "01010001", "10100011", "01000000", "10001111", "10010010", "10011101", "00111000", "11110101",
                    "10111100", "10110110", "11011010", "00100001", "00010000", "11111111", "11110011", "11010010" },
            { "11001101", "00001100", "00010011", "11101100", "01011111", "10010111", "01000100", "00010111",
                    "11000100", "10100111", "01111110", "00111101", "01100100", "01011101", "00011001", "01110011" },
            { "01100000", "10000001", "01001111", "11011100", "00100010", "00101010", "10010000", "10001000",
                    "01000110", "11101110", "10111000", "00010100", "11011110", "01011110", "00001011", "11011011" },
            { "11100000", "00110010", "00111010", "00001010", "01001001", "00000110", "00100100", "01011100",
                    "11000010", "11010011", "10101100", "01100010", "10010001", "10010101", "11100100", "01111001" },
            { "11100111", "11001000", "00110111", "01101101", "10001101", "11010101", "01001110", "10101001",
                    "01101100", "01010110", "11110100", "11101010", "01100101", "01111010", "10101110", "00001000" },
            { "10111010", "01111000", "00100101", "00101110", "00011100", "10100110", "10110100", "11000110",
                    "11101000", "11011101", "01110100", "00011111", "01001011", "10111101", "10001011", "10001010" },
            { "01110000", "00111110", "10110101", "01100110", "01001000", "00000011", "11110110", "00001110",
                    "01100001", "00110101", "01010111", "10111001", "10000110", "11000001", "00011101", "10011110" },
            { "11100001", "11111000", "10011000", "00010001", "01101001", "11011001", "10001110", "10010100",
                    "10011011", "00011110", "10000111", "11101001", "11001110", "01010101", "00101000", "11011111" },
            { "10001100", "10100001", "10001001", "00001101", "10111111", "11100110", "01000010", "01101000",
                    "01000001", "10011001", "00101101", "00001111", "10110000", "01010100", "10111011", "00010110" }
    };
}
