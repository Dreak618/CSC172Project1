public class tests {
    public static void runTests() {
        encryptBloc(); // all ones, all ones
        encryptBloc(); // all zeros, all ones
        encryptBloc(); // all zeros, zeros
        encryptBloc(null, null); // block, input key (both given)
        decryptBlock(); // all ones, all ones
        decryptBlock();// all zeros, all ones
        decryptBlock(); // all zeros, zeros
        decryptBlock(null, null); // use given
        decryptBlock(null, null); // use given
    }

    public static void encryptBloc() {}// temp to avoid errors

    public static void encryptBloc(String block, String inputkey) {// all ones, all ones

    }

    public static void decryptBlock() {

    }

    public static void decryptBlock(String block, String inputkey) {

    }
}
