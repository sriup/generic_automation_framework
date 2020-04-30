package framework.utilities;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.jboss.aerogear.security.otp.Totp;

public class SecurityUtil {

	/**
	 * Gets the token based on the secret key
	 * 
	 * @param secretKey Secret Key <br>
	 *                  <i>Note : Here are couple of sample secret keys that you can
	 *                  try <br>
	 *                  QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVK <br>
	 *                  TY3ZX2YMUJSPTN6Z</i>
	 * @return String current token
	 * 
	 */
	public String getToken(String secretKey) {
		String token = null;
		Totp otp = new Totp(secretKey);
		token = otp.now();
		return token;

	}

	public String encrypt(String textToEncrypt) {
		String encryptedtext = null;
		try {
			String key = System.getenv("AES_KEY");
			SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());
			encryptedtext = DatatypeConverter.printBase64Binary(encrypted);
			if (!decrypt(encryptedtext).equals(textToEncrypt)) {
				throw new Exception("unable to decrypt the encrypted text");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedtext;

	}

	public String decrypt(String textToDecrypt) {
		String decrypted = null;
		try {

			String key = System.getenv("AES_KEY");
			SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] byteText = DatatypeConverter.parseBase64Binary(textToDecrypt);
			decrypted = new String(cipher.doFinal(byteText));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decrypted;
	}
}
