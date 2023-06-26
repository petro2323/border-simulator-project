import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Person {
	private String fName;
	private String lName;
	private String gender;
	private String grestinCode;
	private Document[] documents;

	public Person(String fileName) {
		File f = new File(fileName);
		try (BufferedReader b = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = b.readLine()) != null) {
				String[] part = line.split(",");
				this.fName = part[0];
				this.lName = part[1];
				this.gender = part[2];
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.documents = new Document[0];
	}

	public String getfName() {
		return fName;
	}

	public String getlName() {
		return lName;
	}

	public String getGender() {
		return gender;
	}

	public String getGrestinCode() {
		return grestinCode;
	}

	public void setGrestinCode(String grestinCode) {
		this.grestinCode = grestinCode;
	}

	public boolean addDocument(Document d) {
		Document[] newDocuments = new Document[documents.length + 1];
		System.arraycopy(documents, 0, newDocuments, 0, documents.length);
		newDocuments[documents.length] = d;
		documents = newDocuments;
		return true;
	}

	public Document[] getDocuments() {
		return documents;
	}
	
	public void setDocuments(Document[] d) {
		this.documents = d;
	}

	@Override
	public String toString() {
		String allDocs = "";
		for (int i = 0; i < documents.length; i++) {
			if (documents[i] != null) {
				allDocs += documents[i].toString();
			} else {
				break;
			}
		}

		return "\nFirst Name: " + fName + "\nLast Name: " + lName + "\nGender: " + gender + "\nDocuments: " + "\n"
				+ allDocs;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Person other = (Person) obj;
		return Objects.equals(fName, other.fName) && Objects.equals(lName, other.lName)
				&& Objects.equals(gender, other.gender);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fName, lName, gender);
	}
}
