public class TheRoundFunction {
    public static void splitIt(String s) { // return long later
        int m = s.length() / 2;
        StringBuilder L = new StringBuilder(m);
        StringBuilder R = new StringBuilder(m);
        for (char c : s.toCharArray()) {
            if (s.indexOf(c) < m) {
                L.append(c);
            } else { // first half into L, second half into R
                R.append(c);
            }

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

    }

}
