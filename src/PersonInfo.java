
public class PersonInfo {
	private String personString;
	private Person person;

	public PersonInfo(String personString, Person person) {
		this.personString = personString;
		this.person = person;
	}

	public String getPersonString() {
		return personString;
	}

	public Person getPerson() {
		return person;
	}
}
