import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IdentityCard extends Document {

	private int weight;
	private int height;
	private String dob;
	private String dateExpires;

	public IdentityCard(String fileName) {
		super(fileName);
		File f = new File(fileName);
		try (BufferedReader b = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = b.readLine()) != null) {
				String[] part = line.split(",");
				this.weight = Integer.parseInt(part[4].trim());
				this.height = Integer.parseInt(part[5].trim());
				this.dob = part[6];
				this.dateExpires = part[7];
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public int getWeight() {
		return weight;
	}

	public int getHeight() {
		return height;
	}

	public String getDob() {
		return dob;
	}

	public String getDateExpires() {
		return dateExpires;
	}

	@Override
	public String toString() {
		return "\n**IdentityCard**" + super.toString() + "\nWeight: " + weight + " KG" + "\nHeight: " + height + " CM" + "\nDOB: " + dob
				+ "\nExpires: " + dateExpires + "\n*****";
	}
}
