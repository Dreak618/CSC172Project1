import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Encrypter {
    private ArrayList<String> Blocks = new ArrayList<String>(); // List of blocks
    private int charCount = 0; // used to keep track of how many chars in blocks
    private String currentBlock = "";
    private CypherMethods cypherMethods = new CypherMethods();

    public Encrypter(String inputFilePath, String inputKey) {
        // take the file and break it into blocks
        createBlocks(inputFilePath);
        // encrypt the blocks
        for (String block : Blocks) {
            block = encryptBlock(block, inputKey);
        }
        // write encrypted blocks to file
        writeBlocks(inputFilePath);
    }

    // takes plain text and breaks it into 64 bit blocks
    private void createBlocks(String inputFilePath) {
        // Create a buffered file reader
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            // Takes every line in the file and converts the plain-text into 64bit binary
            // blocks
            while ((line = reader.readLine()) != null) {
                lineToBinary(line);
            }
            // Pad final block with 0s if not full
            if (charCount % 8 != 0) {
                while (charCount % 8 != 0) {
                    charCount++;
                    currentBlock += "00000000";
                }
                Blocks.add(currentBlock);
            }
        } catch (IOException e) {
            System.out.println("Error reading file while encrypting");
        }
    }

    // convert chars in line to 8 bit binary strings
    private void lineToBinary(String line) {
        // Loops through all chars in line
        for (int i = 0; i < line.length(); i++) {
            // increase the count of chars written, used to keeping track of block size
            charCount++;
            // Convert char to binary
            String currentCharBinary = Integer.toBinaryString(line.charAt(i));

            // add leading 0s to char binary string if needed to make it 8 bytes
            while (currentCharBinary.length() < 8) {
                currentCharBinary = "0" + currentCharBinary;
            }

            // adds the binary value of that char to current block
            currentBlock += currentCharBinary;

            // checks if there are 8 chars in the current block, if there are adds it to the
            // list of blocks and starts a new block
            if (charCount % 8 == 0 && charCount != 0) {
                Blocks.add(currentBlock);
                currentBlock = "";
            }
        }
    }

    // TODO: method will call all encryption of blocks
    public String encryptBlock(String block, String inputKey) {
        // Split Block into 2 32 bit strings
        String[] split = cypherMethods.splitIt(block);
        String L = split[0];
        String R = split[1];

        // round function and swap
        for (int i = 0; i < 10; i++) {
            cypherMethods.roundFunction(L, R);

            // swap
            String swappy = "";
            swappy = L;
            L = R;
            R = swappy;
        }
        // return ecrypted block
        return L + R;
    }

    // writes blocks to file
    private void writeBlocks(String inputFilePath) {
        // Create a file for encrypted text
        File encryptedFile = new File(inputFilePath + ".encrypted");
        String encryptPath = encryptedFile.getAbsolutePath();

        // Write the encrypted blocks to the file
        try (FileWriter writer = new FileWriter(encryptPath)) {
            for (String block : Blocks) {
                writer.write(block + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file while encrypting");
        }
    }
}
