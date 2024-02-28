
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
        tests.runTests(project);
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
        int mode = 0;

        if (mode == 0) {// if mode is 0 encrypts
            encryption(inputFilePath, inputKey);
            // for texting
            new Decrypter(inputFilePath + ".encrypted");
        } else if (mode == 1) { // if mode is 1 decrypts
            new Decrypter(inputFilePath);
        }
    }

    public int modeChooser() {
        Scanner s = new Scanner(System.in);
        System.out.print("Do you want to encrypt or decrypt (E/D): ");
        String ED = s.nextLine();
        s.close();
        if (ED.equals("E")) {
            return 0;
        } else if (ED.equals("ED")) {
            return 1;
        } else {
            System.out.println("Invalid mode, run again and type E to Encrypt or D to Decrypt");
            return -1;
        }
    }

    public void encryption(String inputFilePath, String inputKey) {
        new Encrypter(inputFilePath, inputKey);
    }
}
