package framework.utilities;

import com.github.javafaker.Faker;

/**
 * This class helps in getting the fake data which can be used as part of your
 * test.
 *
 */
public class FakeDataUtil {
	Faker faker;

	public FakeDataUtil() {
		this.faker = new Faker();
	}

	/**
	 * Gets the first name
	 * 
	 * @return First Name
	 */
	public String firstName() {
		return this.faker.name().firstName();
	}

	/**
	 * Gets the last name
	 * 
	 * @return Last Name
	 */
	public String lastName() {
		return this.faker.name().lastName();
	}

	/**
	 * Gets the funny name
	 * 
	 * @return Funny Name
	 */
	public String funnyName() {
		return this.faker.funnyName().name();
	}
	
	/**
	 * Gets the full address
	 * @return Full Address
	 */
	public String fullAddress() {
		return this.faker.address().fullAddress();
	}

	/**
	 * Gets the street address
	 * @return Street Address
	 */
	public String streetAddress() {
		return this.faker.address().streetAddress();
	}
	
	/**
	 * Gets the zip code with in the specified state
	 * @param stateAbbr  State in which you want to get the zip code
	 * @return Zip Code
	 */
	public String zipCode(String stateAbbr) {
		return this.faker.address().zipCodeByState(stateAbbr);
	}

	/**
	 * Gets the business name
	 * 
	 * @return Business Name
	 */
	public String businessName() {
		return this.faker.company().name();
	}

}
