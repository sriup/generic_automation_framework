package framework.utilities;

import java.security.SecureRandom;

import org.apache.commons.lang.RandomStringUtils;

public class GenericUtil {

	/**
	 * Generates random string with Alphabet chars
	 * 
	 * @param stringLength Number Of chars in the string
	 * @return Random Alpha string
	 */
	public String generateRandomString(int stringLength) {
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return RandomStringUtils.random(stringLength, allowedChars);
	}

	/**
	 * Generates random alphanumeric string
	 * 
	 * @param length Number of chars in the string
	 * @return Random AlphaNumeric String
	 */
	public String generateAlphaNumericString(int length) {
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		return RandomStringUtils.random(length, allowedChars);
	}

	/**
	 * Generates random number
	 * 
	 * @param length Length of the digit number
	 * @return Random long number
	 */
	public long generateRandomNumber(int length) {
		String allowedChars = "0123456789";
		String randomNumber = "";
		String temp = RandomStringUtils.random(length, allowedChars);
		randomNumber = temp.substring(0, temp.length());

		return Long.valueOf(randomNumber);
	}

	/**
	 * Generates number in the given minimum and maximum range
	 * 
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
