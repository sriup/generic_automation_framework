package unittests.encryption;

import framework.utilities.SecurityUtil;

import java.util.Scanner;
public class EncryptTest {

	/**
	 * Encrypts the given text using AES encryption. The user will be asked to enter the text to encrypt, and
	 * the encrypted text will be printed in the console.
	 * <p>
	 * Note: Make sure you set up the AES_KEY in the environment variables before
	 * running this and restart your IDE (if you just set up the environment variable)
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		String continueEncrypting = "y";
		// initiate the scanner to read the data from console
		Scanner scanner = new Scanner(System.in);
		// Initialize SecurityUtil class
		SecurityUtil secUtil = new SecurityUtil();

		String encryptedText = null;
		// iterate until the user want to exit
		while (continueEncrypting.equalsIgnoreCase("y")) {
			// print message in the console for the user
			System.out.println("Enter text to encrypt, use comma separator to convert multiple items :");
			// get user input
			String originalText = scanner.nextLine();
			// split the input text with comma and then encrypt all the items
			for (String text : originalText.split(",")) {
				if (!text.trim().isEmpty()) {
					// encrypt the text
					encryptedText = secUtil.encrypt(text.trim());
					// write the decrypted information in the console
					System.out.println(text.trim() + "\t" + encryptedText + "\n");
				}
			}
			// check with user if he wants continue decrypting
			System.out.println("Do you want to continue encrypting : y/n");
			// get the first char (making sure the script does not fail if
			// user send Yes/NO
			continueEncrypting = scanner.nextLine().trim().toLowerCase();
		}
		// close the scanner
		scanner.close();
	}

}
