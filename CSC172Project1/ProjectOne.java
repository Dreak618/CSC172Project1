
//Nicholas Krein / Benjamin Levy 
//File, reader, writer
import java.io.FileNotFoundException;
import java.io.IOException;

// REMEBER TO REMOVE PACKAGES BEFORE SUBMISSION
public class ProjectOne {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        // new ProjectOne();

        runTests();
        // Scanner s = new Scanner(System.in);

        // // get file path and key
        // // System.out.print("Enter File Name: ");
        // // String inputFile = s.nextLine();
        // // System.out.println("enter key");
        // // String inputKey = s.nextLine();

        // // rn hardcoded mode and path for texting
        String inputFile = "data.txt";
        String inputKey = "00011101010011010110100010101011010010110100011100100101";

        // // System.out.print("Do you want to encrypt or decrypt (E/D): ");
        // // String ED = s.next();
        // // rn hardcoded
        String ED = "E";

        if (ED.equals("E")) {
            encryption(inputFile, inputKey);
            // for testing
            decryption(inputFile + ".encrypted", inputKey);
        } else if (ED.equals("D")) {
            new Decrypter(inputFile, inputKey);
        }
        // // s.close();

    }

    public ProjectOne() {
        String allOnes = "1111111111111111111111111111111111111111111111111111111111111111"; // 64 bit
        String allOnesKey = "11111111111111111111111111111111111111111111111111111111"; // 56 bit
        String allZeros = "0000000000000000000000000000000000000000000000000000000000000000"; // 64 bit
        String allZerosKey = "00000000000000000000000000000000000000000000000000000000"; // 56 bit
        String block = "1100110010000000000001110101111100010001100101111010001001001100";
        String block2 = "0101011010001110111001000111100001001110010001100110000011110101";
        String block3 = "0011000101110111011100100101001001001101011010100110011111010111";

        // System.out.println("Output for: encryption (all ones, all ones)");

        String string = block;
        String key = allZerosKey;

        System.out.println("Start");
        splitIt(string);
        // System.out.println(string);
        String encrypt = Encrypter.encryptBlock(string, key);
        System.out.println("encrypted");
        splitIt(encrypt);
        String decrypt = Decrypter.decryptBlock(encrypt, key);
        System.out.println(" decrypted ");
        splitIt(decrypt);
        System.out.println("matching: " + (string.equals(decrypt)));
        // System.out.println("Output for: decryption (all ones, all ones)");
    }

    public void splitIt(String s) {
        int l = s.length();
        System.out.println(s.substring(0, l / 2) + " first half");
        System.out.println(s.substring(l / 2, l) + " second half");

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
        String block = "1100110010000000000001110101111100010001100101111010001001001100";
        String block2 = "0101011010001110111001000111100001001110010001100110000011110101";
        String block3 = "0011000101110111011100100101001001001101011010100110011111010111";

        String string = block;
        String key = allZerosKey;
        String encrypt = Encrypter.encryptBlock(string, key);
        String decrypt = Decrypter.decryptBlock(encrypt, key);
        System.out.println("matching: " + (string.equals(decrypt)));

        System.out.println("Running Tests:");
        System.out.println("Output for: encryption (all ones, all ones)");
        System.out.println(Encrypter.encryptBlock(allOnes, allOnesKey));
        System.out.println("Should be:");
        System.out.println("0101011010001110111001000111100001001110010001100110000011110101");

        System.out.println("Output for: encryption (all zeros, all ones)");
        System.out.println(Encrypter.encryptBlock(allZeros, allOnesKey));
        System.out.println("Should be:");
        System.out.println("1100111010001000100011011010110110110010100101011001100000101000");

        System.out.println("Output for: encryption (all zeros, all zeros)");
        System.out.println(Encrypter.encryptBlock(allZeros, allZerosKey));
        System.out.println("Should be:");
        System.out.println("1010100101110001000110111000011110110001101110011001111100001010");

        System.out.println("Output for: encryption (block, all zeros), where: \n block = " + block);
        System.out.println(Encrypter.encryptBlock(block, allZerosKey));
        System.out.println("Should be:");
        System.out.println("0010101110011011010001010111000010110110101011111010000101100101");

        System.out.println("Output for: decryption (all ones, all ones)");
        System.out.println(Decrypter.decryptBlock(allOnes, allOnesKey));
        System.out.println("Should be:");
        System.out.println("0100111001000110011000001111010101010110100011101110010001111000");

        System.out.println("Output for: decryption(all zeros, all ones)");
        System.out.println(Decrypter.decryptBlock(allZeros, allOnesKey));
        System.out.println("Should be:");
        System.out.println("1011001010010101100110000010100011001110100010001000110110101101");

        System.out.println("Output for: decryption(all zeros, all zeros)");
        System.out.println(Decrypter.decryptBlock(allZeros, allZerosKey));
        System.out.println("Should be:");
        System.out.println("1011000110111001100111110000101010101001011100010001101110000111");

        System.out.println("Output for: decryption(block, all ones), where: \n block = " + block2);
        System.out.println(Decrypter.decryptBlock(block2, allOnesKey));
        System.out.println("Should be:");
        System.out.println("1111111111111111111111111111111111111111111111111111111111111111");

        System.out.println("Output for decryption(block, all zeros), where: \n block = " + block3);
        System.out.println(Decrypter.decryptBlock(block3, allZerosKey));
        System.out.println("Should be:");
        System.out.println("1111111111111111111111111111111111111111111111111111111111111111");

    }
}
