package unittests.encryption;

import framework.utilities.SecurityUtil;

import java.util.Scanner;

public class GAuthTokenTest {

      /**
       * Gets the Google authentication token, based on the secret token
       *
       * Note: Make sure you set up the AES_KEY in the environment variables before
       * running this and restart your IDE (if you just set up the environment variable)
       *
       * @param args The command line arguments
       */
      public static void main(String[] args) {
            String repeat = "y";

            // initiate the scanner to read the data from console
            Scanner scanner = new Scanner(System.in);

            // Initialize SecurityUtil class
            SecurityUtil secUtil = new SecurityUtil();

            // iterate until the user want to exit
            while (repeat.equalsIgnoreCase("y")) {

                  // print message in the console for the user
                  System.out.println("Enter the encrypted google auth token below:");

                  // get user input
                  String originalText = scanner.nextLine();

                  // write the google auth token information in the console
                  System.out.println(secUtil.getToken(originalText).trim());

                  // check with user if he wants continue
                  System.out.println("Do you want to continue : y/n");
                  // get the first char (making sure the script does not fail if
                  // user send Yes/NO
                  repeat = scanner.nextLine().substring(0, 1);
            }
            // close the scanner
            scanner.close();
      }

}
