public class CipherMethods {
        private String[][] sTable = new String[][] { // the S-Table
                        { "01100011", "01111100", "01110111", "01111011", "11110010", "01101011", "01101111",
                                        "11000101",
                                        "00110000", "00000001", "01100111", "00101011", "11111110", "11010111",
                                        "10101011", "01110110" },
                        { "11001010", "10000010", "11001001", "01111101", "11111010", "01011001", "01000111",
                                        "11110000",
                                        "10101101", "11010100", "10100010", "10101111", "10011100", "10100100",
                                        "01110010", "11000000" },
                        { "10110111", "11111101", "10010011", "00100110", "00110110", "00111111", "11110111",
                                        "11001100",
                                        "00110100", "10100101", "11100101", "11110001", "01110001", "11011000",
                                        "00110001", "00010101" },
                        { "00000100", "11000111", "00100011", "11000011", "00011000", "10010110", "00000101",
                                        "10011010",
                                        "00000111", "00010010", "10000000", "11100010", "11101011", "00100111",
                                        "10110010", "01110101" },
                        { "00001001", "10000011", "00101100", "00011010", "00011011", "01101110", "01011010",
                                        "10100000",
                                        "01010010", "00111011", "11010110", "10110011", "00101001", "11100011",
                                        "00101111", "10000100" },
                        { "01010011", "11010001", "00000000", "11101101", "00100000", "11111100", "10110001",
                                        "01011011",
                                        "01101010", "11001011", "10111110", "00111001", "01001010", "01001100",
                                        "01011000", "11001111" },
                        { "11010000", "11101111", "10101010", "11111011", "01000011", "01001101", "00110011",
                                        "10000101",
                                        "01000101", "11111001", "00000010", "01111111", "01010000", "00111100",
                                        "10011111", "10101000" },
                        { "01010001", "10100011", "01000000", "10001111", "10010010", "10011101", "00111000",
                                        "11110101",
                                        "10111100", "10110110", "11011010", "00100001", "00010000", "11111111",
                                        "11110011", "11010010" },
                        { "11001101", "00001100", "00010011", "11101100", "01011111", "10010111", "01000100",
                                        "00010111",
                                        "11000100", "10100111", "01111110", "00111101", "01100100", "01011101",
                                        "00011001", "01110011" },
                        { "01100000", "10000001", "01001111", "11011100", "00100010", "00101010", "10010000",
                                        "10001000",
                                        "01000110", "11101110", "10111000", "00010100", "11011110", "01011110",
                                        "00001011", "11011011" },
                        { "11100000", "00110010", "00111010", "00001010", "01001001", "00000110", "00100100",
                                        "01011100",
                                        "11000010", "11010011", "10101100", "01100010", "10010001", "10010101",
                                        "11100100", "01111001" },
                        { "11100111", "11001000", "00110111", "01101101", "10001101", "11010101", "01001110",
                                        "10101001",
                                        "01101100", "01010110", "11110100", "11101010", "01100101", "01111010",
                                        "10101110", "00001000" },
                        { "10111010", "01111000", "00100101", "00101110", "00011100", "10100110", "10110100",
                                        "11000110",
                                        "11101000", "11011101", "01110100", "00011111", "01001011", "10111101",
                                        "10001011", "10001010" },
                        { "01110000", "00111110", "10110101", "01100110", "01001000", "00000011", "11110110",
                                        "00001110",
                                        "01100001", "00110101", "01010111", "10111001", "10000110", "11000001",
                                        "00011101", "10011110" },
                        { "11100001", "11111000", "10011000", "00010001", "01101001", "11011001", "10001110",
                                        "10010100",
                                        "10011011", "00011110", "10000111", "11101001", "11001110", "01010101",
                                        "00101000", "11011111" },
                        { "10001100", "10100001", "10001001", "00001101", "10111111", "11100110", "01000010",
                                        "01101000",
                                        "01000001", "10011001", "00101101", "00001111", "10110000", "01010100",
                                        "10111011", "00010110" }
        };

        // public String roundFunction(String R, String inputKey) {
        // THIS IS FUNCTIONF NOW - because that's a req'd method name
        // }

        public String substitutionS(String binaryInput) {

                StringBuilder result = new StringBuilder(binaryInput.length());
                String[] sp2 = splitIt(binaryInput);
                String[][] splits = { splitIt(sp2[0]), splitIt(sp2[1]) }; // split string into 4 parts (8bit binary
                                                                          // each)
                for (int i = 0; i < splits.length; i++) {
                        for (int j = 0; j < (splits[0].length); j++) { // for each string, turn it into 2 binary numbers
                                                                       // (by substring), then use to lookup value in
                                                                       // S-Table
                                splits[i][j] = sTable[Integer.parseInt(splits[i][j].substring(0, 03), 2)][Integer // then
                                                                                                                  // replace
                                                                                                                  // with
                                                                                                                  // S-table
                                                                                                                  // value
                                                .parseInt(splits[i][j].substring(4, 8), 2)];
                                result.append(splits[i][j]);
                        }
                }
                return result.toString();
        }

        // split block into 2
        public static String[] splitIt(String block) {
                int length = block.length();
                // break string into 2 equal parts
                String L = block.substring(0, length / 2);
                String R = block.substring(length / 2, length);
                // put 2 parts into array
                String[] split = { L, R };
                // return array
                return split;
        }

        public static String xorIt(String binary1, String binary2) { // binary2 is the round key ki
                StringBuilder xOr = new StringBuilder(binary1.length());
                for (int i = 0; (i < binary1.length() && i < binary2.length()); i++) {
                        if (binary1.charAt(i) == binary2.charAt(i)) { // if same value, then XOR is false
                                xOr.append(0);
                        } else {
                                xOr.append(1); // otherwise XOR is true
                        }
                }
                return xOr.toString();
        }

        public String shiftIt(String binaryinput) {
                char[] b = binaryinput.toCharArray();
                char e1 = binaryinput.toCharArray()[0]; // hold 1st element
                for (int i = 1; i < b.length - 1; i++) {
                        b[i - 1] = b[i]; // shift every element left one
                }
                b[b.length - 1] = e1; // place first element at end to finish shift
                return b.toString();
        }

        public String permuteIt(String binaryinput) {
                int[] p = { 16, 7, 20, 21, 29, 12, 29, 17, 1, 15,
                                23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27,
                                3, 9, 19, 13, 30, 6, 22, 11, 4, 25 }; // The given P-box
                StringBuilder sb = new StringBuilder(binaryinput.length());
                char[] b = binaryinput.toCharArray();
                for (int i = 0; i < b.length; i++) {
                        sb.append(b[p[i] - 1]);
                        // adds the digit of b located at (the value of p[i] - 1)
                } // -1 so we don't get array out of bounds error
                return sb.toString();
        }

        public String functionF(String rightHalf, String subkey) {

                subkey = keyScheduleTransform(subkey); // do this first to create this iteration's round key
                return permuteIt(substitutionS(xorIt(rightHalf, subkey.substring(0, 32)))); // round key must be 32 bits

        }

        public String keyScheduleTransform(String inputkey) {
                String[] CD = splitIt(inputkey);
                String C = shiftIt(CD[0]);
                String D = shiftIt(CD[1]);
                return C + D;
        }

}
