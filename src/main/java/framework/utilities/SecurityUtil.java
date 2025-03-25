package framework.utilities;

import org.apache.commons.lang.RandomStringUtils;
import org.jboss.aerogear.security.otp.Totp;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.util.Base64;

public class SecurityUtil {

    /**
     * Gets the token based on the secret key
     *
     * @param encryptedSecretKey Secret Key <br>
     *                           <font color='orange'><i>Note : Make sure you the
     *                           secretKey is encrypted. Did not provide the sample
     *                           encrypted keys as the encrypted text will be based
     *                           on the AES_KEY. Please look into
     *                           {@link #getToken(String, boolean)} method if you
     *                           want to learn more about this. </font> <br>
     * @return String current token
     */
    public String getToken(String encryptedSecretKey) {
        // create the instance of the TOTP with the security key
        Totp otp = new Totp(decrypt(encryptedSecretKey));
        // Get the latest token
        return otp.now();
    }

    /**
     * Gets the token based on the secret key
     *
     * @param secretKey            Secret Key <br>
     *                             <font color='orange'><i>Note : secret key (either
     *                             encrypted/decrypted) Here are couple of sample
     *                             decrypted secret keys that you can try</i> <br>
     *                             <ul>
     *                             <li>QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVK</li>
     *                             <li>TY3ZX2YMUJSPTN6Z</li>
     *                             </ul>
     *                             </font> <br>
     * @param isSecretKeyEncrypted specify if secret key encrypted
     * @return String current token
     */
    public String getToken(String secretKey, boolean isSecretKeyEncrypted) {
        String token;
        // create the instance of the TOTP with the security key
        if (isSecretKeyEncrypted) {
            token = getToken(secretKey);
        } else {
            token = getToken(encrypt(secretKey));
        }

        // Get the latest token
        return token;
    }

    /**
     * Generate 32 bytes random AES_Key<br>
     * <font color='red'>Please don't use the separate password than your team, this
     * will result in failing all the tests as we have to use the same AES_KEY used
     * for encryption.<br>
     * <b>If you feel the AES_KEY is compromised please generate a new key and ask
     * the entire team to use the new key.</b></font><br>
     * <font color='blue'>If you have any password encrypted using an older AES_KEY
     * you have to update all of them using the new AES_KEY.</font>
     * <font color='brown'>Make sure to update the Environment Variable "AES_KEY",
     * if you decided to use new key generated.</font>
     *
     * @return dynamically generated AES_KEY string
     * @throws Exception the exception
     */
    public String generateKey() throws Exception {
        String keyBase = RandomStringUtils.random(64, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        byte[] salt = "1234567890".getBytes();
        int iterationCount = 40000;
        int keyLength = 192; // generating 32 bytes key
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpecification = new PBEKeySpec(keyBase.toCharArray(), salt, iterationCount, keyLength);
        SecretKey tempKey = keyFactory.generateSecret(keySpecification);
        SecretKeySpec key = new SecretKeySpec(tempKey.getEncoded(), "AES");
        return base64Encode(key.getEncoded());
    }

    /**
     * Encrypts the text <br>
     * <font color='blue'>Note : Make sure to add
     * </font><b>AES_KEY</b><font color='blue'> in the User Environment
     * Variables<br>
     * Please use the same AES_KEY across all the team members, <b>otherwise tests
     * will</font> <font color='red'>fail</font></b>.
     *
     * @param textToEncrypt Text to encrypt
     * @return Encrypted text
     */
    public String encrypt(String textToEncrypt) {
        String encryptedText = null;
        try {
            // Get the AES_KEY from user environment variables
            String key = (System.getenv("AES_KEY") != null) ? System.getenv("AES_KEY") : System.getProperty("AES_KEY");
            // Create the key
            SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
            // Create cipher instance
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // initialize with encryption mode
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            // initiate the algorithm parameters
            AlgorithmParameters parameters = cipher.getParameters();
            IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
            // Get the encrypted value
            byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());
            // append the paramConvert the value from bytes to standard text
            encryptedText = base64Encode(ivParameterSpec.getIV()) + base64Encode(encrypted);
            if (!decrypt(encryptedText).equals(textToEncrypt)) {
                throw new Exception("unable to decrypt the encrypted text");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return the encrypted value
        return encryptedText;
    }

    /**
     * Decrypts the encrypted text <i>Note : </i> Make sure to add <b>AES_KEY</b> in
     * the User Environment Variables<br>
     * Please use the same AES_KEY across all the team members, <b>otherwise tests
     * will fail</b>.
     *
     * @param textToDecrypt Text to decrypt
     * @return Decrypted text
     */
    public String decrypt(String textToDecrypt) {
        String decrypted = null;
        try {
            // split the sting to get the Iv and the text
            String ivValue = textToDecrypt.substring(0, 24);
            String text = textToDecrypt.substring(24);
            // Get the AES_KEY from user environment variables
            String key = (System.getenv("AES_KEY") != null) ? System.getenv("AES_KEY") : System.getProperty("AES_KEY");
            // Create the key
            SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
            // Create cipher instance
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // initialize with decryption mode
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(base64Decode(ivValue)));
            // Get the decrypted value
            byte[] byteText = base64Decode(text);
            // Convert the value from bytes to standard text
            decrypted = new String(cipher.doFinal(byteText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return the decrypted value
        return decrypted;
    }

    /**
     * convert the bytes to base 64 encoding.
     *
     * @param bytesToEncode the bytes
     * @return the encoded string
     */
    private String base64Encode(byte[] bytesToEncode) {
        return Base64.getEncoder().encodeToString(bytesToEncode);
    }

    /**
     * decodes the string to the byte[]
     *
     * @param textToDecode the text to decode
     * @return the byte[]
     */
    private byte[] base64Decode(String textToDecode) {
        return Base64.getDecoder().decode(textToDecode);
    }
}
