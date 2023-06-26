import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Document {
	private String fName;
	private String lName;
	private String gender;
	private String signature;

	public Document(String fileName) {
		File f = new File(fileName);
		try (BufferedReader b = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = b.readLine()) != null) {
				String[] part = line.split(",");
				this.fName = part[0];
				this.lName = part[1];
				this.gender = part[2];
				this.signature = part[3];
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

	public String getSignature() {
		return signature;
	}

	@Override
	public String toString() {
		return "\nFirst Name: " + fName + "\nLast Name: " + lName + "\nGender: " + gender + "\nSignature: " + signature;
	}

}
