
//Nicholas Krein / Benjamin Levy 
//File, reader, writer
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@SuppressWarnings({ "unused", "resource" })

// REMEBER TO REMOVE PACKAGES BEFORE SUBMISSION
public class ProjectOne {
    private String inputFilePath, encryptPath, decryptPath;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // Test cases here
        ProjectOne project = new ProjectOne();
        tests.runTests();

        // FileReader inputReader = new FileReader(inputFileName);
        // FileWriter fw = new FileWriter(outputFileName);
        // doEncrypt()
        // printf("Secret key: %s \n", secretKey);
        // printf("Output file: %s", outputFileName)
    }

    public ProjectOne() {
        // int mode = modeChooser();
        // inputFilePath = getFilePath();
        inputFilePath = "CSC172Project1/CSC172Project1/testText.txt"; // rn hardcoded mode and path
        int mode = 0;

        if (mode == 0) {// if mode is 0 encrypts
            Encrypter encrypter = new Encrypter(inputFilePath);
        } else if (mode == 1) { // if mode is 1 decrypts
            Decrypter decrypter = new Decrypter(inputFilePath);
        }
    }

    public int modeChooser() {
        Scanner s = new Scanner(System.in);
        System.out.print("Do you want to encrypt or decrypt (E/D): ");
        String ED = s.nextLine();
        if (ED.equals("E")) {
            return 0;
        } else if (ED.equals("ED")) {
            return 1;
        } else {
            System.out.println("Invalid mode, run again and type E to Encrypt or D to Decrypt");
            return -1;
        }
    }

    public String getFilePath() {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter Filename: ");
        return s.nextLine();
    }
}

class Decrypter {
    private String decryptPath, inputFilePath;

    protected Decrypter(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        createDecryptedFile();
    }

    private void createDecryptedFile() {
        File decryptedFile = new File(inputFilePath + ".decrypted");
        decryptPath = decryptedFile.getAbsolutePath();
        try (FileWriter writer = new FileWriter(decryptPath)) {
            writer.write("File Written" + "\n");

        } catch (IOException e) {
            System.out.println("no file found");
        }
    }

}
