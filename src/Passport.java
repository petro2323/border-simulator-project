import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Passport extends Document {

	private String dob;
	private String city;
	private String country;
	private String dateExpires;
	private String idNum;

	public Passport(String fileName) {
		super(fileName);
		File f = new File(fileName);
		try (BufferedReader b = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = b.readLine()) != null) {
				String[] part = line.split(",");
				this.dob = part[4];
				this.city = part[5];
				this.country = part[6];
				this.dateExpires = part[7];
				this.idNum = part[8];
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public String getDob() {
		return dob;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getDateExpires() {
		return dateExpires;
	}

	public String getIdNum() {
		return idNum;
	}

	@Override
	public String toString() {
		return "\n**Passport**" + super.toString() + "\nDOB: " + dob + "\nIssuing City: " + city + "\nCountry: " + country + "\nExpires: "
				+ dateExpires + "\nId Number: " + idNum + "\n*****";
	}
}
