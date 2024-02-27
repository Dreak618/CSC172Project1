
//Nicholas Krein / Benjamin Levy 
//File, reader, writer
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// REMEBER TO REMOVE PACKAGES BEFORE SUBMISSION
public class ProjectOne {
    private String inputFilePath, encryptPath, decryptPath;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // Test cases here
        tests.runTests();

        Scanner s = new Scanner(System.in);
        System.out.print("Do you want to encrypt or decrypt (E/D): ");
        String ED = s.nextLine();
        System.out.print("Filename: ");
        String inputFileName = s.nextLine();
        System.out.println("Output file: ");
        String outputFileName = s.nextLine();
        File outputFile = new File(outputFileName);
        // FileReader inputReader = new FileReader(inputFileName);
        FileWriter fw = new FileWriter(outputFileName);
        // doEncrypt()
        // printf("Secret key: %s \n", secretKey);
        // printf("Output file: %s", outputFileName)
    }

    public ProjectOne(String filePath) {
        inputFilePath = filePath;
        Encrypter encrypter = new Encrypter();
        Decrypter decrypter = new Decrypter();
    }

    protected class Encrypter {
        protected Encrypter() {
            createEncryptedFile();
        }

        public void createEncryptedFile() {
            File encryptedFile = new File("encrypted." + inputFilePath);
            encryptPath = encryptedFile.getAbsolutePath();
        }

    }

    protected class Decrypter {
        protected Decrypter() {
            createDecryptedFile();
        }

        protected void createDecryptedFile() {
            File decryptedFile = new File("decrypted." + inputFilePath);
            encryptPath = decryptedFile.getAbsolutePath();
        }

    }
}
