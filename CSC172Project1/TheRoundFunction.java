public class TheRoundFunction {

    public static void roundFunction(String L, String R){  // give this both halves
        String swappy = "";
        for (int i = 0; i < 10; i ++){
            xorIt(null, null);
            swappy = L;
            L = R;
            R = swappy;
        }
    }
    public static void splitIt(String s) { // return long later
        int m = s.length() / 2;
        StringBuilder L = new StringBuilder(m);
        StringBuilder R = new StringBuilder(m);
        for (char c : s.toCharArray()) {
            if (s.indexOf(c) < m) {
                L.append(c);
            } else { // first half into L, second half into R
                R.append(c);
            } // TODO: Make this also work for splitting 32bit String
             // into four 8-bit Strings (can probably if/else length)
        }
    }

    public static String xorIt(String binary1, String binary2) { // binary2 is the round key ki
        StringBuilder xOr = new StringBuilder(binary1.length());
        char[] b1 = binary1.toCharArray();
        char[] b2 = binary2.toCharArray();
        for (int i = 0; (i < binary1.length() && i < binary2.length()); i++) {
            if (b1[i] == b2[i]) {
                xOr.append(0);
            } else {
                xOr.append(1);
            }
        }
        return xOr.toString();
    }

    public static void shiftIt(String binaryinput) {

    }

    public static void permuteIt(String binaryinput) {
        int[] p = { 16, 7, 20, 21, 29, 12, 29, 17, 1, 15,
                23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27,
                3, 9, 19, 13, 30, 6, 22, 11, 4, 25 };
        StringBuilder sb = new StringBuilder(binaryinput.length());
        char[] b = binaryinput.toCharArray();
        for (int i = 0; i < binaryinput.length(); i++) {
            sb.append(b[p[i] - 1]);
            // adds the digit of b located at (the value of p[i] - 1)
        } // -1 so we don't get array out of bounds error
    }
    public static void functionF(String righthalf, String subkey){

    }

}
