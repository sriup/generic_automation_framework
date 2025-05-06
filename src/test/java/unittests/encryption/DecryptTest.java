package unittests.encryption;

import framework.utilities.SecurityUtil;

import java.util.Scanner;


public class DecryptTest {

	/**
	 * Decrypts the text. It takes the input from the console, decrypts the text
	 * and prints the decrypted text to the console.
	 *
	 * Note: Make sure you set up the AES_KEY in the environment variables before
	 *  running this and restart eclipse (if you just set up the environment variable)
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		String continueEncrypting = "y";
		// initiate the scanner to read the data from console
		Scanner scanner = new Scanner(System.in);
		String decryptedText = null;
		// Initialize SecurityUtil class
		SecurityUtil secUtil = new SecurityUtil();
		// iterate until the user want to exit
		while (continueEncrypting.equalsIgnoreCase("y")) {
			// print message in the console for the user
			System.out.println("Enter text to decrypt, use comma separator to convert multiple items :");
			// get user input
			String originalText = scanner.nextLine();
			// split the input text with comma and then decrypt all the items
			for (String text : originalText.split(",")) {
				if (!text.trim().isEmpty()) {
					// decrypt the text
					decryptedText = secUtil.decrypt(text.trim());
					// write the decrypted information in the console
					System.out.println(text.trim() + "\t" + decryptedText + "\n");
				}
			}
			// check with user if he wants continue decrypting
			System.out.println("Do you want to continue decrypting : y/n");
			// get the first char (making sure the script does not fail if
			// user send Yes/NO
			continueEncrypting = scanner.nextLine().substring(0, 1);
		}
		// close the scanner
		scanner.close();
	}

}
