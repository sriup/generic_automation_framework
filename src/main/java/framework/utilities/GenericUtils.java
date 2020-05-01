package framework.utilities;

import java.security.SecureRandom;

import org.apache.commons.lang.RandomStringUtils;

public class GenericUtils {
	
	/**
	 * Generates random string with Alphabet chars
	 * @param stringLength Number Of chars in the string 
	 * @return random string
	 */
	public String generateRandomString(int stringLength) {
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // alphabets
		return RandomStringUtils.random(stringLength, allowedChars);
	}
	
	/**
	 * Generate random digit number
	 * @param numberLength Length of the digit number
	 * @return random number
	 */
	public int generateDigitNumber(int numberLength) {
		SecureRandom random = new SecureRandom();
		int num = random.nextInt(numberLength);
		return num;
	}
	
	/**
	 * Generate number in the given minimum and maximum range
	 * @param minValue minimum value in the range
	 * @param maxValue maximum value in the range
	 * @return random number
	 */
	public int generateRandomNumber(int minValue, int maxValue) {
		SecureRandom random = new SecureRandom();
		int randomNumber = random.nextInt(maxValue + 1 - minValue) + minValue;
		return randomNumber;
	}

	
}
