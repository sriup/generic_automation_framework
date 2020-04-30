package framework.utilities;

import com.github.javafaker.Faker;

public class FakeDataUtil {
	Faker faker;
	public FakeDataUtil() {
		this.faker = new Faker();
	}


	public String firstName() {
		return this.faker.name().firstName();
	}
	public String lastName() {
		return this.faker.name().lastName();
	}
	public String funnyName() {
		return this.faker.funnyName().name();
	}

	public String fullAddress() {
		return this.faker.address().fullAddress();
	}
	public String streetAddress() {
		return this.faker.address().streetAddress();
	}
	public String zipCode(String stateAbbr) {
		return this.faker.address().zipCodeByState(stateAbbr);
	}
	public String businessName() {
		return this.faker.company().name();
	}

}
