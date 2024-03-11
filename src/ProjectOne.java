//Nicholas Krein / Benjamin Levy 

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ProjectOne {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        testRun();
    }

    public static void testRun() {
        String ED = "E";
        String inputKey = "00000000000000000000011000000000000000000000000000000000";
        String inputFile = "data.txt";

        // runs method to encrypt/decrypt based on user input
        if (ED.equals("E")) {
            CipherMethods.encryptFile(inputFile, inputKey);

        } else if (ED.equals("D")) {
            CipherMethods.decryptFile(inputFile, inputKey);
        }
        CipherMethods.decryptFile("dataE.txt", inputKey);
    }

    public static void nonTest() {
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
            CipherMethods.encryptFile(inputFile, inputKey);
        } else if (ED.equals("D")) {
            CipherMethods.decryptFile(inputFile, inputKey);
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
        System.out.println(CipherMethods.encryptBlock(allOnes, allOnesKey));

        System.out.println("Output for: encryption (all zeros, all ones)");
        System.out.println(CipherMethods.encryptBlock(allZeros, allOnesKey));

        System.out.println("Output for: encryption (all zeros, all zeros)");
        System.out.println(CipherMethods.encryptBlock(allZeros, allZerosKey));

        System.out.println("Output for: encryption (block, all zeros), where: \n block = " + block1);
        System.out.println(CipherMethods.encryptBlock(block1, allZerosKey));

        System.out.println("Output for: decryption (all ones, all ones)");
        System.out.println(CipherMethods.decryptBlock(allOnes, allOnesKey));

        System.out.println("Output for: decryption(all zeros, all ones)");
        System.out.println(CipherMethods.decryptBlock(allZeros, allOnesKey));

        System.out.println("Output for: decryption(all zeros, all zeros)");
        System.out.println(CipherMethods.decryptBlock(allZeros, allZerosKey));

        System.out.println("Output for: decryption(block, all ones), where: \n block = " + block2);
        System.out.println(CipherMethods.decryptBlock(block2, allOnesKey));

        System.out.println("Output for decryption(block, all zeros), where: \n block = " + block3);
        System.out.println(CipherMethods.decryptBlock(block3, allZerosKey));
    }
}
