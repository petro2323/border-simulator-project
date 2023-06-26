import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

public class GrestinBorder implements GrestinBorderCodeGenerator { // Ostalo je jos funkcionalnost
	private String inspectorName;
	private String date;
	private int rows;
	private int cols;
	private Person[][] people;
	private Person[][] accepted;
	private Person[][] denied;

	public GrestinBorder(String fileName) {
		File f = new File(fileName);
		try (BufferedReader b = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = b.readLine()) != null) {
				String[] part = line.split(",");
				this.inspectorName = part[0];
				this.date = part[1];
				this.rows = Integer.parseInt(part[2].trim());
				this.cols = Integer.parseInt(part[3].trim());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.people = new Person[this.rows][this.cols];
		this.accepted = new Person[this.rows][this.cols];
		this.denied = new Person[this.rows][this.cols];
	}

	private boolean equals(Person p) {
		for (int i = 0; i < people.length; i++) {
			for (int j = 0; j < people[i].length; j++) {
				if (people[i][j] != null && people[i][j].equals(p)) {
					return true;
				}
			}
		}
		return false;
	}

	public String databaseOfAccepted() {
		StringBuffer s = new StringBuffer("\nAccepted: " + "\n");
		for (int i = 0; i < accepted.length; i++) {
			for (int j = 0; j < accepted[i].length; j++) {
				if (accepted[i][j] != null) {
					s.append(accepted[i][j].getGrestinCode());
				} else {
					s.append("free \t");
				}
			}
			s.append("\n");
		}
		return s.toString();
	}

	public String findPersonWithGrestinCode(String grestinCode) throws GrestinCodeException {
		for (int i = 0; i < accepted.length; i++) {
			for (int j = 0; j < accepted[i].length; j++) {
				if (accepted[i][j] != null && accepted[i][j].getGrestinCode().equalsIgnoreCase(grestinCode)) {
					Person p = accepted[i][j];
					return p.toString();
				}
			}
		}

		for (int i = 0; i < denied.length; i++) {
			for (int j = 0; j < denied[i].length; j++) {
				if (denied[i][j] != null && denied[i][j].getGrestinCode().equalsIgnoreCase(grestinCode)) {
					Person p = denied[i][j];
					return p.toString();
				}
			}
		}
		throw new GrestinCodeException(grestinCode + " does not exist in the database.");
	}

	public String databaseOfRejected() {
		StringBuffer s = new StringBuffer("\nDenied: " + "\n");
		for (int i = 0; i < denied.length; i++) {
			for (int j = 0; j < denied[i].length; j++) {
				if (denied[i][j] != null) {
					s.append(denied[i][j].getGrestinCode());
				} else {
					s.append("free \t");
				}
			}
			s.append("\n");
		}
		return s.toString();
	}

	public boolean addPerson(Person p) throws AddPersonException {
		if (equals(p)) {
			throw new AddPersonException("This person is already waiting in line.");
		}

		for (int i = 0; i < people.length; i++) {
			for (int j = 0; j < people[i].length; j++) {
				if (people[i][j] == null) {
					people[i][j] = p;
					return true;
				}
			}
		}

		throw new AddPersonException("The line is full.");
	}

	public PersonInfo callNextPerson() {
		for (int i = 0; i < people.length; i++) {
			for (int j = 0; j < people[i].length; j++) {
				if (people[i][j] != null) {
					Person p = people[i][j];
					people[i][j] = null;
					return new PersonInfo(p.toString(), p);
				}
			}
		}
		return null;
	}

	public boolean approve(Person p) throws ApprovePersonException {
		for (int i = 0; i < accepted.length; i++) {
			for (int j = 0; j < accepted[i].length; j++) {
				if (accepted[i][j] == null) {
					p.setGrestinCode(generatedCode());
					accepted[i][j] = p;
					return true;
				}
			}
		}
		throw new ApprovePersonException("The database is full.");
	}

	public boolean deny(Person p) throws DenyPersonException {
		for (int i = 0; i < denied.length; i++) {
			for (int j = 0; j < denied[i].length; j++) {
				if (denied[i][j] == null) {
					p.setGrestinCode(generatedCode());
					denied[i][j] = p;
					return true;
				}
			}
		}
		throw new DenyPersonException("The database is full.");
	}

	public boolean printDatabase(String database, String fileName) throws PrintDbException {
		File f = new File(fileName);
		if (database.equalsIgnoreCase("approved")) {
			try {
				PrintStream p = new PrintStream(f);
				for (int i = 0; i < accepted.length; i++) {
					for (int j = 0; j < accepted[i].length; j++) {
						if (accepted[i][j] != null) {
							String person = findPersonWithGrestinCode(accepted[i][j].getGrestinCode());
							p.println("\n" + person + "\nGrestin Code: " + accepted[i][j].getGrestinCode());
						}
					}
				}
				p.flush();
				p.close();
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GrestinCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (database.equalsIgnoreCase("rejected")) {
			try {
				PrintStream p = new PrintStream(f);
				for (int i = 0; i < denied.length; i++) {
					for (int j = 0; j < denied[i].length; j++) {
						if (denied[i][j] != null) {
							String person = findPersonWithGrestinCode(denied[i][j].getGrestinCode());
							p.println("\n" + person + "\nGrestin Code: " + denied[i][j].getGrestinCode());
						}
					}
				}
				p.flush();
				p.close();
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GrestinCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		throw new PrintDbException("Uknown Command! Use - rejected - DB of rejected OR - approved - DB of approved.");
	}

	private String checkDate(Document d, String today) {
		String checkedMsg = "";
		if (d instanceof Passport) {
			Passport p = (Passport) d;
			String passportDate = p.getDateExpires().trim();
			LocalDate passExpires = LocalDate.parse(passportDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			LocalDate myDate = LocalDate.parse(today.trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

			if (passExpires.isBefore(myDate)) {
				checkedMsg += "Passport has expired on " + passportDate;
			} else if (passExpires.isEqual(myDate)) {
				checkedMsg += "Passport expires today!";
			}
		} else if (d instanceof IdentityCard) {
			IdentityCard ic = (IdentityCard) d;
			String idDate = ic.getDateExpires().trim();
			LocalDate idExpires = LocalDate.parse(idDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			LocalDate myDate = LocalDate.parse(today.trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

			if (idExpires.isBefore(myDate)) {
				checkedMsg += "Identity Card has expired on " + idDate;
			} else if (idExpires.isEqual(myDate)) {
				checkedMsg += "Identity Card expires today!";
			}
		}
		return checkedMsg;
	}

	private String checkBirthday(String pass, String id) {
		String bdayMsg = "";
		LocalDate passB = LocalDate.parse(pass.trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		LocalDate idB = LocalDate.parse(id.trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

		if (!passB.isEqual(idB)) {
			bdayMsg += "Passport birthdate and Identity Card birthdate do not match!";
		}
		return bdayMsg;
	}

	public String discrepancyDetector(Person p) {
		boolean check = true;
		boolean hasDocuments = false;
		StringBuffer message = new StringBuffer("Discrepancies detected: " + "\n");
		Document[] docs = p.getDocuments();
		String passDob = "";
		String idDob = "";
		int count = 1;
		for (int i = 0; i < docs.length; i++) {
			if (docs[i] != null) {
				hasDocuments = true;
				if (docs[i] instanceof Passport) {
					Passport pass = (Passport) docs[i];
					passDob += pass.getDob();
					if (checkDate(pass, this.date) != "") {
						message.append(count + "." + checkDate(pass, this.date) + "\n");
						count++;
					}
					if (!p.getfName().equalsIgnoreCase(pass.getfName())) {
						message.append(count + ".Invalid first name PASSPORT / " + p.getfName() + " != "
								+ pass.getfName() + "\n");
						count++;
						check = false;
					}
					if (!p.getlName().equalsIgnoreCase(pass.getlName())) {
						message.append(count + ".Invalid last name PASSPORT / " + p.getlName() + " != "
								+ pass.getlName() + "\n");
						count++;
						check = false;
					}
					if (!p.getGender().equalsIgnoreCase(pass.getGender())) {
						message.append(count + ".Invalid gender type PASSPORT / " + p.getGender() + " != "
								+ pass.getGender() + "\n");
						count++;
						check = false;
					}
				}
				if (docs[i] instanceof IdentityCard) {
					IdentityCard ic = (IdentityCard) docs[i];
					idDob += ic.getDob();
					if (checkDate(ic, this.date) != "") {
						message.append(count + "." + checkDate(ic, this.date) + "\n");
						count++;
					}
					if (!p.getfName().equalsIgnoreCase(ic.getfName())) {
						message.append(count + ".Invalid first name IDENTITY-CARD / " + p.getfName() + " != "
								+ ic.getfName() + "\n");
						count++;
						check = false;
					}
					if (!p.getlName().equalsIgnoreCase(ic.getlName())) {
						message.append(count + ".Invalid last name IDENTITY-CARD / " + p.getlName() + " != "
								+ ic.getlName() + "\n");
						count++;
						check = false;
					}
					if (!p.getGender().equalsIgnoreCase(ic.getGender())) {
						message.append(count + ".Invalid gender type IDENTITY-CARD / " + p.getGender() + " != "
								+ ic.getGender() + "\n");
						count++;
						check = false;
					}
				}
			}
		}
		if (passDob != "" && idDob != "") {
			if (checkBirthday(passDob, idDob) != "") {
				message.append(count + "." + checkBirthday(passDob, idDob));
			}
		}
		if (!check) {
			return message.toString();
		}

		if (check && hasDocuments) {
			return message.toString() + "NONE";
		}

		return message.toString() + "A PASSPORT OR IDENTITY CARD IS REQUIRED!";
	}

	@Override
	public String generatedCode() {
		String letters = "QWERTYUIOPASDFGHJKLZXCVBNM";
		String numbers = "1234567890";
		char[] lettersArray = letters.toCharArray();
		char[] numbersArray = numbers.toCharArray();
		Random r = new Random();
		String code = "#";
		for (int i = 0; i < 2; i++) {
			int numLet = r.nextInt(lettersArray.length);
			code += lettersArray[numLet];
		}
		for (int i = 0; i < 3; i++) {
			int numNum = r.nextInt(numbersArray.length);
			code += numbersArray[numNum];
		}
		for (int i = 0; i < 1; i++) {
			int numLet = r.nextInt(lettersArray.length);
			code += lettersArray[numLet];
		}
		int numNum = r.nextInt(numbersArray.length);
		code += numbersArray[numNum];
		return code;
	}

	public int numberOfPeopleWaitingInLine() {
		int count = 0;
		for (int i = 0; i < people.length; i++) {
			for (int j = 0; j < people[i].length; j++) {
				if (people[i][j] != null) {
					count++;
				}
			}
		}
		return count;
	}

	public Person[][] getPeople() {
		return people;
	}

	@Override
	public String toString() {
		return "\nInspector: " + inspectorName + ", Date:" + date + "\nNumber of people waiting in line: "
				+ numberOfPeopleWaitingInLine();
	}
}
