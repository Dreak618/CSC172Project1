//Nicholas Krein / Benjamin Levy 
import java.io.*; //File, reader, writer
import java.util.Scanner;

// REMEBER TO REMOVE PACKAGES BEFORE SUBMISSION
public class ProjectOne {
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
        //FileReader inputReader = new FileReader(inputFileName);
        FileWriter fw = new FileWriter(outputFileName);
        //doEncrypt()
        //printf("Secret key: %s \n", secretKey);
        //printf("Output file: %s", outputFileName)
    }


}
