
//Nicholas Krein / Benjamin Levy 
//File, reader, writer
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// REMEBER TO REMOVE PACKAGES BEFORE SUBMISSION
public class ProjectOne {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // Test cases here
        ProjectOne project = new ProjectOne();
        runTests(project);
    }

    public ProjectOne() {
        // commented out for texting, if mode choosing and path providing needed
        // uncomment following 2 lines and comment the 2 after instead
        // int mode = modeChooser();

        // get file path and key
        // Scanner s = new Scanner(System.in);
        // System.out.print("Enter Filename: ");
        // String inputFilePath = s.nextLine();
        // System.out.println("enter key");
        // String inputKey = s.nextLine();
        // s.close();

        // rn hardcoded mode and path for texting
        String inputFilePath = "CSC172Project1/CSC172Project1/testText.txt";
        String inputKey = "";
        boolean encrypt = true; //redid mode with a boolean here

        if (encrypt) {// if true, do encrypt
            encryption(inputFilePath, inputKey);
            // for texting
            new Decrypter(inputFilePath + ".encrypted");
        } else { // else, do decrypt
            new Decrypter(inputFilePath);
        }
    }

    // public int modeChooser() { -- want to preferably do scanner things in main for readability
    //     Scanner s = new Scanner(System.in);
    //     System.out.print("Do you want to encrypt or decrypt (E/D): ");
    //     String ED = s.nextLine();
    //     s.close();
    //     if (ED.equals("E")) {
    //         return 0;
    //     } else if (ED.equals("ED")) {
    //         return 1;
    //     } else {
    //         System.out.println("Invalid mode, run again and type E to Encrypt or D to Decrypt");
    //         return -1;
    //     }
    // }

    public void encryption(String inputFilePath, String inputKey) {
        new Encrypter(inputFilePath, inputKey);
    }

    public static void runTests(ProjectOne project) {
        // TODO: Make test cases
        // Removed tests.java

        // encryptBloc(); // all ones, all ones
        // encryptBloc(); // all zeros, all ones
        // encryptBloc(); // all zeros, zeros
        // encryptBloc(null, null); // block, input key (both given)
        // decryptBlock(); // all ones, all ones
        // decryptBlock();// all zeros, all ones
        // decryptBlock(); // all zeros, zeros
        // decryptBlock(null, null); // use given
        // decryptBlock(null, null); // use given
    }
}
