//Nicholas Krein / Benjamin Levy 

import java.io.FileNotFoundException;
import java.io.IOException;
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
            encryption(inputFile, inputKey);
        } else if (ED.equals("D")) {
            decryption(inputFile, inputKey);
        }
        s.close();
    }

    /**
     * Creates a {@code CipherMethods.Encrypter} to encrypt a file
     * 
     * @param inputFilePath file path
     * @param inputKey      encryption key
     * 
     */
    private static void encryption(String inputFilePath, String inputKey) {
        new CipherMethods.Encrypter(inputFilePath, inputKey);
    }

    /**
     * Creates a {@code CipherMethods.Decrypter()} to decrypt a file
     * 
     * @param inputFilePath file path
     * @param inputKey      decryption key
     */
    private static void decryption(String inputFilePath, String inputKey) {
        new CipherMethods.Decrypter(inputFilePath, inputKey);
    }

    /**
     * Runs test cases
     * variables declared at top
     * then code to output/run each test
     */

    private static void runTests() {
        String allOnes = "1111111111111111111111111111111111111111111111111111111111111111"; // 64 bit
        String allOnesKey = "11111111111111111111111111111111111111111111111111111111"; // 56 bit
        String allZeros = "0000000000000000000000000000000000000000000000000000000000000000"; // 64 bit
        String allZerosKey = "00000000000000000000000000000000000000000000000000000000"; // 56 bit
        String block1 = "1100110010000000000001110101111100010001100101111010001001001100"; // 64 bit
        String block2 = "0101011010001110111001000111100001001110010001100110000011110101"; // 64 bit
        String block3 = "0011000101110111011100100101001001001101011010100110011111010111"; // 64 bit

        /*
         * 192 bit long binary input not used currently in test but can be used by
         * CipherMethods.Encrypter.encryption(longBinaryInput, inputKey); or
         * CipherMethods.Decrypter.decryption(longBinaryInput, inputKey);
         * to encrypt a long binary input rather than just encrypting one block or a
         * whole file at once
         */
        String longBinaryInput = "110011001000000000000111010111110001000110010111101000100100110001010110100011101110010001111000010011100100011001100000111101010011000101110111011100100101001001001101011010100110011111010111";

        // Test Cases
        System.out.println("Running Tests:");
        System.out.println("Output for: encryption (all ones, all ones)");
        System.out.println(CipherMethods.Encrypter.encryptBlock(allOnes, allOnesKey));

        System.out.println("Output for: encryption (all zeros, all ones)");
        System.out.println(CipherMethods.Encrypter.encryptBlock(allZeros, allOnesKey));

        System.out.println("Output for: encryption (all zeros, all zeros)");
        System.out.println(CipherMethods.Encrypter.encryptBlock(allZeros, allZerosKey));

        System.out.println("Output for: encryption (block, all zeros), where: \n block = " + block1);
        System.out.println(CipherMethods.Encrypter.encryptBlock(block1, allZerosKey));

        System.out.println("Output for: decryption (all ones, all ones)");
        System.out.println(CipherMethods.Decrypter.decryptBlock(allOnes, allOnesKey));

        System.out.println("Output for: decryption(all zeros, all ones)");
        System.out.println(CipherMethods.Decrypter.decryptBlock(allZeros, allOnesKey));

        System.out.println("Output for: decryption(all zeros, all zeros)");
        System.out.println(CipherMethods.Decrypter.decryptBlock(allZeros, allZerosKey));

        System.out.println("Output for: decryption(block, all ones), where: \n block = " + block2);
        System.out.println(CipherMethods.Decrypter.decryptBlock(block2, allOnesKey));

        System.out.println("Output for decryption(block, all zeros), where: \n block = " + block3);
        System.out.println(CipherMethods.Decrypter.decryptBlock(block3, allZerosKey));
    }
}
