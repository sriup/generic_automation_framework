package framework.utilities;

import com.github.javafaker.Faker;

import framework.logs.LogAccess;

/**
 * This class helps in getting the fake data which can be used as part of your
 * test.
 *
 */
public class FakeDataUtil {

	/** The Faker object. */
	Faker faker;

	/** The Log Access object */
	private LogAccess logAccess;

	/**
	 * Instantiates Faker object
	 * 
	 * @param logAccess LogAccess instance
	 */
	public FakeDataUtil(LogAccess logAccess) {
		this.logAccess = logAccess;
		this.faker = new Faker();
	}

	/**
	 * Gets the first name
	 * 
	 * @return First Name
	 */
	public String firstName() {

		String firstName = this.faker.name().firstName();

		this.logAccess.getLogger().debug("Generating firstName '" + firstName + "' from faker");

		return firstName;
	}

	/**
	 * Gets the last name
	 * 
	 * @return Last Name
	 */
	public String lastName() {

		String lastName = this.faker.name().lastName();

		this.logAccess.getLogger().debug("Generating lastName '" + lastName + "' from faker");

		return lastName;
	}

	/**
	 * Gets the funny name
	 * 
	 * @return Funny Name
	 */
	public String funnyName() {

		String funnyName = this.faker.funnyName().name();

		this.logAccess.getLogger().debug("Generating funnyName '" + funnyName + "' from faker");

		return funnyName;
	}

	/**
	 * Gets the full address
	 * 
	 * @return Full Address
	 */
	public String fullAddress() {

		String fullAddress = this.faker.address().fullAddress();

		this.logAccess.getLogger().debug("Generating fullAddress '" + fullAddress + "' from faker");

		return fullAddress;
	}

	/**
	 * Gets the street address
	 * 
	 * @return Street Address
	 */
	public String streetAddress() {

		String streetAddress = this.faker.address().streetAddress();

		this.logAccess.getLogger().debug("Generating streetAddress '" + streetAddress + "' from faker");

		return streetAddress;
	}

	/**
	 * Gets the zip code with in the specified state
	 * 
	 * @param stateAbbr State in which you want to get the zip code
	 * @return Zip Code
	 */
	public String zipCode(String stateAbbr) {

		String zipCode = this.faker.address().zipCodeByState(stateAbbr);

		this.logAccess.getLogger().debug("Generating zipCode '" + zipCode + "' from faker");

		return zipCode;
	}

	/**
	 * Gets the business name
	 * 
	 * @return Business Name
	 */
	public String businessName() {

		String businessName = this.faker.company().name();

		this.logAccess.getLogger().debug("Generating businessName '" + businessName + "' from faker");

		return businessName;
	}

}
