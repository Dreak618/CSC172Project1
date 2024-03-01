
//Nicholas Krein / Benjamin Levy 
//File, reader, writer
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// REMEBER TO REMOVE PACKAGES BEFORE SUBMISSION
public class ProjectOne {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        runTests();
        Scanner s = new Scanner(System.in);

        // get file path and key
        // System.out.print("Enter File Name: ");
        // String inputFile = s.nextLine();
        // System.out.println("enter key");
        // String inputKey = s.nextLine();

        // rn hardcoded mode and path for texting
        String inputFile = "data.txt";
        String inputKey = "00000000000000000000000000000000";

        // System.out.print("Do you want to encrypt or decrypt (E/D): ");
        // String ED = s.next();
        // rn hardcoded
        String ED = "E";

        if (ED.equals("E")) {
            encryption(inputFile, inputKey);
            // for testing
            decryption(inputFile + ".encrypted", inputKey);
        } else if (ED.equals("D")) {
            new Decrypter(inputFile);
        }
        s.close();

    }

    public static void encryption(String inputFilePath, String inputKey) {
        new Encrypter(inputFilePath, inputKey);
    }

    public static void decryption(String inputFilePath, String inputKey) {
        new Decrypter(inputFilePath);
    }

    public static void runTests() {
        // TODO: Make test cases, also may need to rework stuff to get test case format
        // to work but do after encryption and decryption actually work
        // Removed tests.java

        // encryptBlock(); // all ones, all ones
        // encryptBlock(); // all zeros, all ones
        // encryptBlock(); // all zeros, zeros
        // encryptBlock(null, null); // block, input key (both given)
        // decryptBlock(); // all ones, all ones
        // decryptBlock();// all zeros, all ones
        // decryptBlock(); // all zeros, zeros
        // decryptBlock(null, null); // use given
        // decryptBlock(null, null); // use given
    }
}
