package framework.utilities;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.jboss.aerogear.security.otp.Totp;

public class SecurityUtil {

	/**
	 * Gets the token based on the secret key
	 * 
	 * @param encryptedSecretKey Secret Key <br>
	 *                           <font color='orange'><i>Note : Make sure you the
	 *                           secretKey is encrypted. Did not provided the sample
	 *                           encrypted keys as the encrypted text will be based
	 *                           on the AES_KEY. Please look into
	 *                           {@link #getToken(String, boolean)} method if you
	 *                           want to learn more about this. </font> <br>
	 * 
	 * @return String current token
	 * 
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
	 * 
	 */
	public String getToken(String secretKey, boolean isSecretKeyEncrypted) {
		String token = null;
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
	 * Encrypts the text <br>
	 * <i><font color='blue'>Note : </i> Make sure to add
	 * </font><b>AES_KEY</b><font color='blue'> in the User Environment
	 * Variables<br>
	 * Please use the same AES_KEY across all the team members, <b>otherwise tests
	 * will</font> <font color='red'>fail</font></b>.
	 * 
	 * @param textToEncrypt Text to encrypt
	 * @return Encrypted text
	 */
	public String encrypt(String textToEncrypt) {
		String encryptedtext = null;
		try {
			// Get the AES_KEY from user environment variables
			String key = System.getenv("AES_KEY");
			// Create the key
			SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
			// Create cipher instance
			Cipher cipher = Cipher.getInstance("AES");
			// initialize with encryption mode
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			// Get the encypted value
			byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());
			// Convert the value from bytes to standard text
			encryptedtext = DatatypeConverter.printBase64Binary(encrypted);
			if (!decrypt(encryptedtext).equals(textToEncrypt)) {
				throw new Exception("unable to decrypt the encrypted text");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return the encrypted value
		return encryptedtext;

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
			// Get the AES_KEY from user environment variables
			String key = System.getenv("AES_KEY");
			// Create the key
			SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
			// Create cipher instance
			Cipher cipher = Cipher.getInstance("AES");
			// initialize with decryption mode
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			// Get the decypted value
			byte[] byteText = DatatypeConverter.parseBase64Binary(textToDecrypt);
			// Convert the value from bytes to standard text
			decrypted = new String(cipher.doFinal(byteText));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return the decrypted value
		return decrypted;
	}
}
