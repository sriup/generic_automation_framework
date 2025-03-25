package unittests.encryption;

import framework.utilities.SecurityUtil;

public class GenerateAesKeyTest {
    /**
     * Generate a 32 bytes random AES key. This is used for encryption of the passwords.
     *
     * @param args Command line arguments
     * @throws Exception if there is an issue while generating the key
     */
    public static void main(String[] args) throws Exception {
        SecurityUtil secUtil = new SecurityUtil();
        System.out.println(secUtil.generateKey());
    }
}
