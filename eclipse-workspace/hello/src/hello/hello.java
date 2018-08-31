package hello;

import java.io.*;
import java.sql.*;

public class hello {

	public static void OpenConnection() throws ClassNotFoundException, SQLException {
		String DATABASE_URL = "jdbc:mysql://localhost/public";
		String USER = "public";
		String PASSWORD = "public1area";
		Connection connection = null;
		Statement statement = null;

		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Creating database connection...");
		connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

		System.out.println("Executing statement...");
		statement = connection.createStatement();

		String sql;
		sql = "SELECT * FROM stage_fop";

		ResultSet resultSet = statement.executeQuery(sql);

		System.out.println("Retrieving data from database...");
		System.out.println("\nStage_fop:");
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String first_name = resultSet.getString("FIRST_NAME");
			String last_name = resultSet.getString("LAST_NAME");
			int middle_name = resultSet.getInt("MIDDLE_NAME");

			System.out.println("\n================\n");
			System.out.println("id: " + id);
			System.out.println("First name: " + first_name);
			System.out.println("Last name: " + last_name);
			System.out.println("Middle name: $" + middle_name);
		}

		System.out.println("Closing connection and releasing resources...");
		resultSet.close();
		statement.close();
		connection.close();

	}

	public static void SaveToDB(String FIO, String Stan, String Adr, boolean IsParsingBad, String Zip, String OBL,
			String Rajon_oblast, String City, String Town, String Rajon_city, String Street, String StreetType,
			String Dom, String Kvart, String Kved, String NKved, String SKved) {
		System.out.format("%s|%s\n", FIO, Stan);
		System.out.format("PARSING=%b ADDRESS=%s\n", IsParsingBad, Adr);
		System.out.format("%s|%s|%s|%s|%s|%s|%s|%s|%s\n", Zip, OBL, Rajon_oblast, City, Town, Rajon_city, Street,
				StreetType, Dom, Kvart, Kved, NKved, SKved);
		System.out.format("%s|%s|%s\n", Kved, NKved, SKved);
		// database public table stage_fop

		;
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		BufferedReader bReader = new BufferedReader(new FileReader("D://temp//Master.xml"));
		String line, lineUpper, SAddressUp;
		int cntr = 0;
		int Thousand = 0;
		try {
			OpenConnection();
		} catch (SQLException e) {
			System.out.println("Unable to connect, slowly dying...");
		}

		while ((line = bReader.readLine()) != null) {
			lineUpper = line.toUpperCase();
			if (lineUpper.startsWith("</DATA")) {
				System.out.println("All <DATA> were read");
				break;
			}
			cntr = cntr + 1;
			if (lineUpper.startsWith("<XML")) {
				System.out.println("XML header");
				continue;
			}
			if (lineUpper.startsWith("<DATA")) {
				System.out.println("DATA header");
				continue;
			}

			if (lineUpper.startsWith("<RECORD>")) {
				String FIO = "", FirstName = "", LastName = "", MiddleName = "";
				int pos1 = 0, pos2 = 0;
				// <RECORD><FIO>Ì²ÒĞÎÙÅÍÊÎ ŞĞ²É Â²ÊÒÎĞÎÂÈ×</FIO>
				// <ADDRESS>53300, Äí³ïğîïåòğîâñüêà îáë., ì³ñòî Îğäæîí³ê³äçå, ÂÓËÈÖß Ë.
				// ×ÀÉÊ²ÍÎ¯, áóäèíîê 28, êâàğòèğà 36</ADDRESS>
				// <KVED>52.62.0 ĞÎÇÄĞ²ÁÍÀ ÒÎĞÃ²ÂËß Ç ËÎÒÊ²Â ÒÀ ÍÀ ĞÈÍÊÀÕ</KVED>
				// <STAN>ïğèïèíåíî</STAN>
				// </RECORD>

				// , Address, Kved, Stan;
				line.replace("<RECORD>", "");
				line.replace("</RECORD>", "");

				if ((line.indexOf("<FIO>") > 0) & (line.indexOf("</FIO>") > 0)) {
					FIO = line.substring(5 + line.indexOf("<FIO>"), line.indexOf("</FIO>"));
					FIO = FIO.trim();
					if (FIO.contains(" ")) {
						pos1 = FIO.indexOf(" ");
						FirstName = FIO.substring(1, FIO.indexOf(" ") - 1);
					}

				}
				;

				String Adr = "", Zip = "", OBL = "", Rajon_oblast = "", City = "", Town = "", Rajon_city = "",
						Street = "", StreetType = "", Dom = "", Kvart = "";
				boolean IsParsingBad = false;
				if ((line.indexOf("<ADDRESS>") > 0) & (line.indexOf("</ADDRESS>") > 0)) {

					String Params[] = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
					int i = 0;

					Adr = line.substring(9 + line.indexOf("<ADDRESS>"), line.indexOf("</ADDRESS>"));
					SAddressUp = Adr.trim().toUpperCase();

					// System.out.format("PARSE STEP 0=%s\n", SAddressUp);
					if (SAddressUp.contains(",")) {
						i = i + 1;
						Params[i] = SAddressUp.substring(0, SAddressUp.indexOf(",")).trim();
						SAddressUp = SAddressUp.substring(1 + SAddressUp.indexOf(","), SAddressUp.length()).trim();
						// System.out.format("PARSE STEP %d=%s\n", i, Params[i]);
					}
					;
					while (SAddressUp.contains(",")) {
						i = i + 1;
						Params[i] = SAddressUp.substring(0, SAddressUp.indexOf(",")).trim();
						SAddressUp = SAddressUp.substring(1 + SAddressUp.indexOf(","), SAddressUp.length()).trim();
						// System.out.format("PARSE STEP %d=%s\n", i, Params[i]);
					}
					;
					if (SAddressUp.length() > 0) {
						i = i + 1;
						Params[i] = SAddressUp.substring(0, SAddressUp.length()).trim();
						// System.out.format("PARSE STEP %d=%s LAST \n", i, Params[i]);
					}
					;
					if (Params[1].contains("0") || Params[1].contains("1") || Params[1].contains("2")
							|| Params[1].contains("3") || Params[1].contains("4") || Params[1].contains("5")
							|| Params[1].contains("6") || Params[1].contains("7") || Params[1].contains("8")
							|| Params[1].contains("9")) {
						Zip = Params[1];
					}
					for (int j = 1; j <= i; j++) {
						if (Params[j].contains("ÎÁË."))
							OBL = Params[j];
						if (Params[j].contains(" ÎÁËÀÑÒÜ"))
							OBL = Params[j];
						if (Params[j].contains(" ĞÀÉÎÍ")) {
							if (City == "")
								Rajon_oblast = Params[j];
							else
								Rajon_city = Params[j];
						}
						;
						if (Params[j].contains(" Ğ-Í")) {
							if (City == "")
								Rajon_oblast = Params[j];
							else
								Rajon_city = Params[j];
						}
						;
						if (Params[j].contains("Ì²ÑÒÎ "))
							City = Params[j];
						if (Params[j].contains("Ì.") && (Params[j].startsWith("Ì.")))
							City = Params[j];
						if (Params[j].contains("ÏÃÒ "))
							Town = Params[j];
						if (Params[j].contains("ÑÅËÎ "))
							Town = Params[j];
						if (Params[j].contains("ÑÅËÈÙÅ "))
							Town = Params[j];
						if (Params[j].contains("ÊÂÀĞÒÀË")) {
							Rajon_city = Params[j];
						}
						;
						if (Params[j].contains(" Ì²ÊĞÎĞÀÉÎÍ")) {
							Rajon_city = Params[j];
						}
						;
						if (Params[j].contains("ÂÓËÈÖß")) {
							Street = Params[j];
							StreetType = "ÂÓËÈÖß";
						}
						if (Params[j].contains("ÂÓË.")) {
							Street = Params[j];
							StreetType = "ÂÓËÈÖß";
						}
						if (Params[j].contains("ÏĞÎÑÏÅÊÒ")) {
							Street = Params[j];
							StreetType = "ÏĞÎÑÏÅÊÒ";
						}
						if (Params[j].contains("ÏĞÎÂÓËÎÊ")) {
							Street = Params[j];
							StreetType = "ÏĞÎÂÓËÎÊ";
						}
						;
						if (Params[j].contains("ÁÓËÜÂÀĞ")) {
							Street = Params[j];
							StreetType = "ÁÓËÜÂÀĞ";
						}
						;
						if (Params[j].contains("Â&apos;¯ÇÄ")) {
							Street = Params[j];
							StreetType = "Â&apos;¯ÇÄ";
						}
						;
						if (Params[j].contains("ÁÓÄÈÍÎÊ"))
							Dom = Params[j];
						if (Params[j].contains("ÊÂÀĞÒÈĞÀ"))
							Kvart = Params[j];
						if (Params[j].contains("ÊÂ."))
							Kvart = Params[j];
					}
					int ParamCntr = 0;
					for (int k = 0; k < Params.length; k++) {
						if (Params[k].length() != 0)
							ParamCntr = ParamCntr + 1;
					}
					;
					if (ParamCntr != 1 + Adr.length() - Adr.replaceAll(",", "").length()) {
						IsParsingBad = true;
						System.out.format("%s%s|%s|%s|%s|%s|%s|%s|%s|%s\n", "BAD PARSING =", Zip, OBL, Rajon_oblast,
								City, Town, Rajon_city, Street, Dom, Kvart);
					}

				}

				String NKved = "0.0";
				String SKved = "";
				String Kved = "";
				if ((line.indexOf("<KVED>") > 0) & (line.indexOf("</KVED>") > 0)) {
					Kved = line.substring(6 + line.indexOf("<KVED>"), line.indexOf("</KVED>"));
					Kved.trim();
					if (Kved.contains(" ")) {
						NKved = Kved.substring(0, Kved.indexOf(" "));
						NKved = NKved.trim();
						SKved = Kved.substring(Kved.indexOf(" "), Kved.length());
						SKved = SKved.trim();
					}
				} else {
					Kved = "";
				}
				;
				if (Kved == "") {
					;
					SKved = "";
					NKved = "0.0";
				}
				// System.out.format("KVED=%s|%s \nFULL=%s\n", NKved, SKved, Kved);
				String Stan = "";
				if ((line.indexOf("<STAN>") > 0) & (line.indexOf("</STAN>") > 0)) {
					Stan = line.substring(6 + line.indexOf("<STAN>"), line.indexOf("</STAN>"));
					Stan = Stan.trim();
				} else {
					Stan = "NULL";
				}
				SaveToDB(FIO, Stan, Adr, IsParsingBad, Zip, OBL, Rajon_oblast, City, Town, Rajon_city, Street,
						StreetType, Dom, Kvart, Kved, NKved, SKved);
			}

			if (cntr % 10000 == 0) {
				Thousand = Thousand + 1;
				System.out.format("Next %d0 000\n", Thousand);

			}
			if (cntr > 5000) {
				System.out.println("Break in debugger");
				break;

			}

		}
		bReader.close();
		System.out.println("Well done");
	}
}
