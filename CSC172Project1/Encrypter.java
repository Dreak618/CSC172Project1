import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Encrypter {
    private String encryptPath, inputFilePath;

    public Encrypter(String inputFilePath) {
        this.inputFilePath = inputFilePath;
        createEncryptedFile();
        encryptFile();
    }

    private void encryptFile() {
        ArrayList<String> Blocks = new ArrayList<String>();
        try (FileReader reader = new FileReader(inputFilePath)) {
            String fileToString = reader.toString();
            String blockString = "";
            for (int i = 0; i < fileToString.length(); i++) {
                if (i % 8 == 0 && i != 0) {
                    Blocks.add(blockString);
                    blockString = "";
                }
                blockString += Integer.toBinaryString(fileToString.charAt(i));

                // String block;
                // block = fileToString.substring(startPos, startPos + 4);
                // startPos += 4;
                // byte[] blockBytes = new byte[4];
                // blockBytes = block.getBytes();
                // System.out.println(blockBytes[0] + " block 0");
                // String blockBinaryString = "";
                // for (byte b : blockBytes) {
                // blockBinaryString += b;
                // }
                // System.out.println(blockBinaryString);
            }
            System.out.println(Blocks.get(0));

            // int block2 = reader.read();
            // System.out.println("" + block1 + block2);

        } catch (

        IOException e) {

        }
        try (FileWriter writer = new FileWriter(encryptPath)) {
            for (String s : Blocks) {
                System.out.println(s.length());
                writer.write(s + "\n");
            }
        } catch (IOException e) {

        }

    }
    // private int makeBlock()

    private void createEncryptedFile() {
        File encryptedFile = new File(inputFilePath + ".encrypted");
        encryptPath = encryptedFile.getAbsolutePath();

        try (FileWriter writer = new FileWriter(encryptPath)) {
            writer.write("File Written" + "\n");

        } catch (IOException e) {
            System.out.println("no file found");
        }
    }
}
