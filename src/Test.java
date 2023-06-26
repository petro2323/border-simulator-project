import java.util.Scanner;

public class Test {
	public void test() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the file name of information about the Inspector:");
		String info = sc.next();

		if (info.endsWith(".txt")) {
			GrestinBorder gb = new GrestinBorder(info);
			System.out.println("Program has started.");
			while (true) {
				String command = sc.next();
				boolean gameRunning = false;
				if (command.equalsIgnoreCase("--help")) {
					System.out.println("Commands: ");
					System.out.println("--edit");
					System.out.println("--start");
					System.out.println("--exit");
				}
				if (command.equalsIgnoreCase("--edit")) {
					System.out.println("1.Add Person In Line");
					System.out.println("2.Edit Person");
					int option = sc.nextInt();
					if (option == 1) {
						System.out.println("Enter the file name of information about the Person:");
						String person = sc.next();
						Person p = new Person(person);
						System.out.println("Select the document type: 0.NONE, 1.Passport, 2.IdentityCard");
						int type = sc.nextInt();
						switch (type) {
						case 0:
							try {
								gb.addPerson(p);
								System.out.println(p.getfName() + p.getlName() + " has no documents.");
							} catch (AddPersonException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						case 1:
							System.out.println("Enter the file name of Passport:");
							String passport = sc.next();
							if (passport.endsWith(".txt")) {
								Passport pass = new Passport(passport);
								p.addDocument(pass);
								try {
									gb.addPerson(p);
									System.out.println("The passport has been added to the inventory of " + p.getfName()
											+ p.getlName());
								} catch (AddPersonException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							break;
						case 2:
							System.out.println("Enter the file name of Identity Card:");
							String id = sc.next();
							if (id.endsWith(".txt")) {
								IdentityCard ic = new IdentityCard(id);
								p.addDocument(ic);
								try {
									gb.addPerson(p);
									System.out.println("The identity card has been added to the inventory of "
											+ p.getfName() + p.getlName());
								} catch (AddPersonException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							break;
						default:
							System.out.println("**Invalid option**");
							break;
						}
					} else if (option == 2) {
						if (gb.numberOfPeopleWaitingInLine() > 0) {
							Person[][] people = gb.getPeople();
							System.out.println("Enter the file name of the person you want to edit:");
							String person = sc.next();
							Person p = new Person(person);
							boolean found = false;
							for (int i = 0; i < people.length; i++) {
								for (int j = 0; j < people[i].length; j++) {
									if (people[i][j] != null && people[i][j].equals(p)) {
										found = true;
										p = people[i][j];
										System.out.println(p.getfName() + p.getlName() + " has been found!");
										System.out.println("Edit option: 1.Add Documents");
										int add = sc.nextInt();
										if (add == 1) {
											System.out.println("1.Passport, 2.IdentityCard");
											int type1 = sc.nextInt();
											if (type1 == 1) {
												System.out.println("Enter the file name of the passport:");
												String passport = sc.next();
												if (passport.endsWith(".txt")) {
													Passport p1 = new Passport(passport);
													p.addDocument(p1);
													System.out.println("Passport has been added.");
												}
											} else if (type1 == 2) {
												System.out.println("Enter the file name of the identity card:");
												String id = sc.next();
												if (id.endsWith(".txt")) {
													IdentityCard ic = new IdentityCard(id);
													p.addDocument(ic);
													System.out.println("Identity Card has been added.");
												}
											} else {
												System.out.println("**Invalid option**");
											}
										} else {
											System.out.println("**Invalid option**");
										}
									}
								}
							}
							if (!found) {
								System.out.println("**Person does not exist**");
							}
						} else {
							System.out.println("**There are no people in line**");
						}
					}
				}
				if (command.equalsIgnoreCase("--start")) {
					gameRunning = true;
					boolean inside = false;
					while (gameRunning) {
						System.out.println("type: '--getincab' to get in the cabinet.");
						String command1 = sc.next();
						if (command1.equalsIgnoreCase("--stop")) {
							System.out.println("Simulation has been stopped.");
							gameRunning = false;
							break;
						}
						if (command1.equalsIgnoreCase("--help")) {
							System.out.println("Commands: --getincab, --stop");
						}
						if (command1.equalsIgnoreCase("--getincab")) {
							inside = true;
							while (inside) {
								System.out.println(gb.toString());
								System.out.println("1.Database of approved people");
								System.out.println("2.Database of rejected people");
								System.out.println("3.Find person with GRESTIN CODE");
								System.out.println("4.Call Next Person");
								System.out.println("5.Print DB");
								System.out.println("6.Get Out");
								int option = sc.nextInt();
								switch (option) {
								case 1:
									System.out.println(gb.databaseOfAccepted());
									break;
								case 2:
									System.out.println(gb.databaseOfRejected());
									break;
								case 3:
									String gCode = sc.next();
									try {
										System.out.println(gb.findPersonWithGrestinCode(gCode));
									} catch (GrestinCodeException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;
								case 4:
									if (gb.numberOfPeopleWaitingInLine() > 0) {
										PersonInfo pi = gb.callNextPerson();
										System.out.println(pi.getPersonString());
										Person p = pi.getPerson();
										System.out.println(gb.discrepancyDetector(p));
										System.out.println(
												"*Enter 'approve' to let the person pass or 'deny' to send the person back*");
										String choice = sc.next();
										boolean choiceValid = false;
										do {
											if (choice.equalsIgnoreCase("approve")) {
												try {
													gb.approve(p);
													System.out.println(p.getfName() + p.getlName()
															+ " is approved. :The response has been recorded:");
													choiceValid = true;
													break;
												} catch (ApprovePersonException e) {
													e.printStackTrace();
												}
											} else if (choice.equalsIgnoreCase("deny")) {
												try {
													gb.deny(p);
													System.out.println(p.getfName() + p.getlName()
															+ " is denied. :The response has been recorded:");
													choiceValid = true;
													break;
												} catch (DenyPersonException e) {
													e.printStackTrace();
												}
											} else {
												System.out.println(
														"**Unknown command. Please enter 'approve' or 'deny'**");
												choice = sc.next();
											}
										} while (!choiceValid);
									} else {
										System.out.println("Cannot call next person, line is empty.");
									}
									break;
								case 5:
									System.out.println("Enter the DB TYPE:");
									String db = sc.next();
									System.out.println("Name:");
									String name = sc.next();
									boolean success = false;
									do {
										if (name.endsWith(".txt")) {
											try {
												gb.printDatabase(db, name);
												success = true;
											} catch (PrintDbException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
												System.out.println("Try again: ");
												db = sc.next();
											}
										} else {
											System.out.println("Name must end with .txt! Try again:");
											name = sc.next();
										}
									} while (!success);
									break;
								case 6:
									System.out.println("You've got out of the cabinet.");
									inside = false;
									break;
								}
							}
						}
					}
				}
				if (command.equalsIgnoreCase("--exit")) {
					System.out.println("Program has ended.");
					break;
				}
			}
		}
	}
}
