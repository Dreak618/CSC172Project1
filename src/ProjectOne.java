//Nicholas Krein / Benjamin Levy 

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// REMEBER TO REMOVE PACKAGES BEFORE SUBMISSION
public class ProjectOne {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        runTests(); // run test cases
        Scanner s = new Scanner(System.in);

        // get file path and key from user using scanner
        System.out.print("Enter File Name: ");
        String inputFile = s.nextLine();
        System.out.println("enter key");
        String inputKey = s.nextLine();

        System.out.print("Do you want to encrypt or decrypt (E/D): ");
        String ED = s.next();

        if (ED.equals("E")) {
            encryption(inputFile, inputKey);
        } else if (ED.equals("D")) {
            decryption(inputFile, inputKey);
        }
        s.close();

    }

    public static void encryption(String inputFilePath, String inputKey) {
        new Encrypter(inputFilePath, inputKey);
    }

    public static void decryption(String inputFilePath, String inputKey) {
        new Decrypter(inputFilePath, inputKey);
    }

    public static void runTests() {
        String allOnes = "1111111111111111111111111111111111111111111111111111111111111111"; // 64 bit
        String allOnesKey = "11111111111111111111111111111111111111111111111111111111"; // 56 bit
        String allZeros = "0000000000000000000000000000000000000000000000000000000000000000"; // 64 bit
        String allZerosKey = "00000000000000000000000000000000000000000000000000000000"; // 56 bit
        String block1 = "1100110010000000000001110101111100010001100101111010001001001100"; // 64 bit
        String block2 = "0101011010001110111001000111100001001110010001100110000011110101"; // 64 bit
        String block3 = "0011000101110111011100100101001001001101011010100110011111010111"; // 64 bit

        System.out.println("Running Tests:");
        System.out.println("Output for: encryption (all ones, all ones)");
        System.out.println(Encrypter.encryptBlock(allOnes, allOnesKey));

        System.out.println("Output for: encryption (all zeros, all ones)");
        System.out.println(Encrypter.encryptBlock(allZeros, allOnesKey));

        System.out.println("Output for: encryption (all zeros, all zeros)");
        System.out.println(Encrypter.encryptBlock(allZeros, allZerosKey));

        System.out.println("Output for: encryption (block, all zeros), where: \n block = " + block1);
        System.out.println(Encrypter.encryptBlock(block1, allZerosKey));

        System.out.println("Output for: decryption (all ones, all ones)");
        System.out.println(Decrypter.decryptBlock(allOnes, allOnesKey));

        System.out.println("Output for: decryption(all zeros, all ones)");
        System.out.println(Decrypter.decryptBlock(allZeros, allOnesKey));

        System.out.println("Output for: decryption(all zeros, all zeros)");
        System.out.println(Decrypter.decryptBlock(allZeros, allZerosKey));

        System.out.println("Output for: decryption(block, all ones), where: \n block = " + block2);
        System.out.println(Decrypter.decryptBlock(block2, allOnesKey));

        System.out.println("Output for decryption(block, all zeros), where: \n block = " + block3);
        System.out.println(Decrypter.decryptBlock(block3, allZerosKey));

    }
}
