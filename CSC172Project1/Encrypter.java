import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Encrypter {
    private ArrayList<String> Blocks = new ArrayList<String>();
    private String encryptPath, inputFilePath;
    private int charCount = 0;
    private String currentBlock = "";

    public Encrypter(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        createEncryptedFile();
        createBlocks();
        for (String block : Blocks) {
            encryptBlock(block);
        }
        writeBlocks();
    }

    private void createEncryptedFile() {
        File encryptedFile = new File(inputFilePath + ".encrypted");
        encryptPath = encryptedFile.getAbsolutePath();
    }

    private void createBlocks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineToBinary(line);
            }
            while (currentBlock.length() < 64 && !currentBlock.equals("")) {
                currentBlock += "00000000";
            }
            if (!currentBlock.equals("")) {
                Blocks.add(currentBlock);
            }

        } catch (IOException e) {
            System.out.println("Error reading file while encrypting");
        }
    }

    private void lineToBinary(String line) {
        for (int i = 0; i < line.length(); i++) {
            charCount++;
            String currentCharBinary = Integer.toBinaryString(line.charAt(i));
            if (currentCharBinary.length() == 6) {
                currentCharBinary = "0" + currentCharBinary;
            }
            if (currentCharBinary.length() == 7) {
                currentCharBinary = "0" + currentCharBinary;
            }
            currentBlock += currentCharBinary;
            if (charCount % 8 == 0 && charCount != 0) {
                Blocks.add(currentBlock);
                currentBlock = "";
            }
        }
    }

    public void writeBlocks() {
        try (FileWriter writer = new FileWriter(encryptPath)) {
            for (String block : Blocks) {
                System.out.println(block.length());
                writer.write(block + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file while encrypting");
        }
    }

    private void encryptBlock(String block) {

    }

}
