import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Decrypter {
    public Decrypter(String inputFilePath) {
        String binaryText = decryptText(inputFilePath); // NYI
        // Convert the decrypted binary into text
        String text = binaryToText(binaryText);
        // Write decrypted binary to file
        writeDecyptedText(text, inputFilePath);
    }

    /*
     * TODO: currently just grabs the binary and adds it to binary string since it
     * is not yet encrypted, when encryption added will need to decrupt before line
     * added to binary text
     */

    private String decryptText(String inputFilePath) {
        String binaryTest = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // TODO: decrypt line here
                binaryTest += line;
            }
        } catch (IOException e) {
            System.out.println("Error reading file while encrypting");
        }
        return binaryTest;
    }

    // converts binary into plain-text
    public String binaryToText(String binaryText) {
        String plainText = "";
        // chars are in 8 bit chunks and loops until there are no chunks left
        while (binaryText.length() > 7) {
            // gets current 8 but chunck of binary text
            String currentChar = binaryText.substring(0, 8);
            // checks if that chunk of binary text is padding
            if (currentChar.equals("00000000")) {
                // if it is cuts out the padding
                binaryText = binaryText.substring(8);
            } else {
                // if not, get the character associated with the given binary value
                char nextCharacter = (char) Integer.parseInt(currentChar, 2);
                // adds that character to plain text string
                plainText += nextCharacter;
                // cut out that character from the binary string
                binaryText = binaryText.substring(8);
            }
        }
        return plainText;
    }

    // write decrypted text to a new file
    private void writeDecyptedText(String text, String inputFilePath) {
        // create file for decrypted text
        File decryptedFile = new File(inputFilePath + ".decrypted");
        String decryptPath = decryptedFile.getAbsolutePath();

        // write decrypted text to file
        try (FileWriter writer = new FileWriter(decryptPath)) {
            writer.write(text);
        } catch (IOException e) {
            System.out.println("no file found");
        }
    }
}