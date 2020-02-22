package usable_app_proj;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class bank_app {
	static final String SEPARATOR = ",";
	static int activeUsers = 0;
	static String userName = "";
	static String userSurname = "";
	static String password = "";
	static Scanner scanner = new Scanner(System.in);
	static String pathToFile = "D:\\Eclipse\\workspace\\usable_app_proj\\client.csv";
	static ArrayList<Client> clientList = new ArrayList<Client>();
	
	public static void main(String[] args) {
		createClientList(pathToFile);
		inputChoice();
		new web_scraping_USD();
	}
	
	public static void createClientList(String filePath) {
		Path pathObject = Paths.get(filePath);
		Charset charset = Charset.forName("UTF-8");
		try {
			List<String> lines = Files.readAllLines(pathObject, charset);
			for (int i=1; i<lines.size(); i++) {
				String[] lineArr = lines.get(i).split(SEPARATOR);
				clientList.add(new Client(lineArr[0],lineArr[1], Integer.parseInt(lineArr[2]), Integer.parseInt(lineArr[3])));
			}
		fillClientObj (filePath, lines);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public static void fillClientObj (String filePath, List<String> lines) {
			for (int i=0; i<lines.size()-1; i++) { 
				String[] lineArr = lines.get(i+1).split(SEPARATOR);
				clientList.get(i).password = lineArr[5];
				System.out.println(clientList.get(i).password);
				clientList.get(i).balance = Integer.parseInt(lineArr[6]);
				clientList.get(i).lastDeposit = Integer.parseInt(lineArr[7]);
				clientList.get(i).lastWithdrawal = Integer.parseInt(lineArr[8]);
				int index = 9;
				for (int j=0; j<clientList.get(i).transHistLast5.length; j++) {
					if (index==9+clientList.get(i).transHistLast5.length-1) {
						clientList.get(i).transHistLast5[j]=Integer.parseInt(lineArr[index].substring(1,lineArr[index].length()-1));
					}
					else {clientList.get(i).transHistLast5[j]=Integer.parseInt(lineArr[index].substring(lineArr[index].length()-1));
					}System.out.println(Arrays.toString(clientList.get(i).transHistLast5));
					index++;
				}
			}
	}

	public static void inputChoice() {
		System.out.println("-------------------------------------");
		System.out.println("Select an option:");
		if (Client.clientCount == 0) {
			System.out.println(" 1 - to register");
		}
		if (Client.clientCount > 0 && activeUsers == 0) {
			System.out.println(" 1 - to register");
			System.out.println(" 2 - to log in");
		}
		if (activeUsers == 1) {
			System.out.println(" 3 - to see your balance");
			System.out.println(" 4 - to see your ID");
			System.out.println(" 5 - to deposit");
			System.out.println(" 6 - to withdraw");
			System.out.println(" 7 - to see last 5 transactions");
			System.out.println(" 8 - to convert EUR and USD");
			System.out.println(" 9 - to to logout");
		}
		System.out.println("-------------------------------------");
		// jāsaliek visām metodēm, kur kaut ko ievada lietotājs, lai izķer erorus,
		// varbūt, eroru ziņu var pārveidot par metodi...
		int input = 0;
		boolean incorectInput = true;
		do {
			try {
				input = scanner.nextInt();
				incorectInput = false;
			} catch (Exception e) {
				System.out.println("Please enter valid option.");
				scanner = new Scanner(System.in); // kādēļ šī rindiņa jāievieto? bez tās aiziet bezgalīgs loops, bet
													// nesaprotu kādēļ?

			}
		} while (incorectInput);

		switch (input) {
		case 1:
			register();
			break;
		case 2:
			login();
			break;
		case 3:
			balance();
			break;
		case 4:
			seeID();
			break;
		case 5:
			makeDeposit();
			break;
		case 6:
			makeWithdraw();
			break;
		case 7:
			showHistory();
			break;
		case 8:
			convert();
			break;
		case 9:
			logout();
			break;
		default:
			System.out.println("Please enter valid option.");
			inputChoice();
			break;
		}
		scanner.close();
	}

	public static void register() {
		boolean isAClient = false;
		System.out.print("Insert your first name: ");
		userName = "" + scanner.nextLine();
		userName = "" + scanner.nextLine(); // šī problēma ar skeneri ir jāatrisina, jāpapēta dziļāk
		System.out.print("Insert your last name: ");
		userSurname = "" + scanner.nextLine();

		for (int i = 0; i < clientList.size(); i++) {
			if (userName.equals(clientList.get(i).firstName) && userSurname.equals(clientList.get(i).lastName)) {
				System.out.println("Insert your new password: ");
				password = "" + scanner.nextLine();
				clientList.get(i).password = password;
				isAClient = true;
				break;
			}
		}
		if (!isAClient) {
			System.out.println("We dont have you as a client. " + userName + " " + userSurname);
		}
		inputChoice();
		scanner.close();
	}

	public static void login() {
		System.out.print("Insert your first name: ");
		userName = "" + scanner.nextLine();
		userName = "" + scanner.nextLine(); // šī problēma ar skeneri ir jāatrisina, jāpapēta dziļāk
		System.out.print("Insert your last name: ");
		userSurname = scanner.nextLine();
		System.out.print("Insert your password: ");
		password = "" + scanner.nextLine();
		for (int i = 0; i < clientList.size(); i++) {
			if (isUserTrue()) {
				System.out.println("You are loged in");
				activeUsers = 1;
				break;
			}
		}
		if (activeUsers == 0) {
			System.out.println("Something is wrong. Try again");
		}
		inputChoice();
		scanner.close();
	}

	public static void balance() {
		for (int i = 0; i < clientList.size(); i++) {
			if (isUserTrue()) {
				System.out.println("Your balance is: " + clientList.get(i).balance);
				break;
			} else
				System.out.println("We have no data.");
		}
		inputChoice();
	}

	public static void seeID() {
		for (int i = 0; i < clientList.size(); i++) {
			if (isUserTrue()) {
				System.out.println("Your ID is: " + clientList.get(i).id);
				break;
			} else
				System.out.println("We have no data.");
		}
		inputChoice();
	}

	public static void makeDeposit() {
		for (int i = 0; i < clientList.size(); i++) {
			if (isUserTrue()) {
				System.out.print("Enter the amount: ");
				boolean incorrectInput = true;
				int deposit=0;
				do {
					try {
						deposit = scanner.nextInt();
						incorrectInput = false;
					} catch (Exception e) {
						System.out.println("Please use only numbers");
						scanner = new Scanner(System.in);
					}
				} while (incorrectInput);
				clientList.get(i).balance += deposit;
				clientList.get(i).lastDeposit = deposit;
				System.out.println("Your balance is: " + clientList.get(i).balance);
				boolean have0 = false;
				//zemāk esošo daļu liks citā metodē
				for (int j = 0; j < clientList.get(i).transHistLast5.length; j++) {
					if (clientList.get(i).transHistLast5[j] == 0) {
						clientList.get(i).transHistLast5[j] = deposit;
						have0 = true;
						break;
					} 
				}
				if (!have0) {
					for (int j=0; j<clientList.get(i).transHistLast5.length-1;j++) {
						clientList.get(i).transHistLast5[j] = clientList.get(i).transHistLast5[j+1];
					}
					clientList.get(i).transHistLast5[clientList.get(i).transHistLast5.length-1] = deposit;
					
				}
				break;
			}
		}
		inputChoice();
	}

	public static void makeWithdraw() {
		for (int i = 0; i < clientList.size(); i++) {
			if (isUserTrue()) {
				System.out.print("Enter the amount: ");
				int withdrawal = 0;
				boolean incorectInput = true;
				do {
					try {
				withdrawal = scanner.nextInt();
				incorectInput = false;
					} catch (Exception e) {
						System.out.println("Please use only numbers");
						scanner = new Scanner(System.in);
					}
				} while (incorectInput);
				if (withdrawal <= clientList.get(i).balance) {
					clientList.get(i).balance -= withdrawal;
					clientList.get(i).lastWithdrawal = withdrawal;
					System.out.println("Your balance is: " + clientList.get(i).balance);
					boolean have0 = false;
					//zemāk esošo daļu liks citā metodē un jāpielāgo arī, kad pievieno naudu
					for (int j = 0; j < clientList.get(i).transHistLast5.length; j++) {
						if (clientList.get(i).transHistLast5[j] == 0) {
							clientList.get(i).transHistLast5[j] = withdrawal * -1;
							have0 = true;
							break;
						} 
					}
					if (!have0) {
						for (int j=0; j<clientList.get(i).transHistLast5.length-1;j++) {
							clientList.get(i).transHistLast5[j] = clientList.get(i).transHistLast5[j+1];
						}
						clientList.get(i).transHistLast5[clientList.get(i).transHistLast5.length-1] = withdrawal * -1;
						
					}
					break;
				} else {
					System.out.println("You can't withdraw such an amount");
					break;
				}
			}
		}
		inputChoice();
	}

	public static void showHistory() {
		for (int i = 0; i < clientList.size(); i++) {
			if (isUserTrue()) {
				System.out.print("Your last 5 transactions are: ");
				for (int j = 0; j < clientList.get(i).transHistLast5.length; j++) {
					System.out.print(" " + clientList.get(i).transHistLast5[j]);
				}
				break;
			}
		}
		System.out.println(" ");
		inputChoice();
	}

	public static void convert() {
		System.out.println("At the moment, You can conwert EUR and USD.");
		System.out.println("If You want to convert from EUR to USD press 1");
		System.out.println("If You want to convert from USD to EUR press 2");
		int input = 0;
		boolean incorectInput = true;
		do {
			try {
				input = scanner.nextInt();
				incorectInput = false;
			} catch (Exception e) {
				System.out.println("Please enter valid option.");
				scanner = new Scanner(System.in); // kādēļ šī rindiņa jāievieto? bez tās aiziet bezgalīgs loops, bet
													// nesaprotu kādēļ?
			}
		} while (incorectInput);

		switch (input) {
		case 1:
			fromEurToUsd();
			break;
		case 2:
			fromUsdToEur();
			break;
		}
		scanner.close();
	}

	/*
	 * jāizveido, ka noapaļo iegūto skaitli abām valūtas konvertēšanas metodēm
	 * kā arī abām konvertēšanas metodēm ar webskreipingu jāuztaisa, ka iegūst aktuālo info
	 */
	public static void fromEurToUsd() {
		System.out.print("Enter the amount You want to convert from: ");
		double eur = 0;
		boolean incorectInput = true;
		do {
			try {
				eur = scanner.nextDouble();
				incorectInput = false;
			} catch (Exception e) {
				System.out.println("Please use only numbers");
				scanner = new Scanner(System.in);
			}
		} while (incorectInput);
		double usd = eur * getCurrentUSD();
		System.out.print(eur + " EUR is " + usd + " USD\n");
		inputChoice();
		scanner.close();
	}

	public static void fromUsdToEur() {
		System.out.print("Enter the amount You want to convert from: ");
		double usd = 0;
		boolean incorectInput = true;
		do {
			try {
				usd = scanner.nextDouble();
				incorectInput = false;
			} catch (Exception e) {
				System.out.println("Please use only numbers");
				scanner = new Scanner(System.in);
			}
		} while (incorectInput);
		double eur = usd * getCurrentEUR();
		System.out.print(usd + " USD is " + eur + " EUR\n");
		inputChoice();
		scanner.close();
	}

	public static boolean isUserTrue() {
		boolean isUserTrue = false;
		for (int i = 0; i < clientList.size(); i++) {
			if (userName.equals(clientList.get(i).firstName) && userSurname.equals(clientList.get(i).lastName)
					&& password.equals(clientList.get(i).password)) {
				isUserTrue = true;
			}
		}
		return isUserTrue;
	}

	public static void logout() {
		addNewInfo(pathToFile);
		activeUsers = 0;
		userName = "";
		userSurname = "";
		password = "";
		System.out.println(userName + userSurname + password);
		System.out.println("You are loged out");
	}
	
	//viss kas saistīts ar objektu info iegūšanu no tabulas un pēc tam tabulas papildināšana jāpārvieto uz klases mapi
	//un tad visu izsauks no turienes ar metodi
	public static void addNewInfo(String filePath) {
		try {
			FileWriter csvWriter = new FileWriter(filePath);

			csvWriter.append("firstName");
			csvWriter.append(",");
			csvWriter.append("lastName");
			csvWriter.append(",");
			csvWriter.append("pkPart1");
			csvWriter.append(",");
			csvWriter.append("pkPart2");
			csvWriter.append(",");
			csvWriter.append("id");
			csvWriter.append(",");
			csvWriter.append("password");
			csvWriter.append(",");
			csvWriter.append("balance");
			csvWriter.append(",");
			csvWriter.append("lastDeposite");
			csvWriter.append(",");
			csvWriter.append("lastWithdrawal");
			csvWriter.append(",");
			csvWriter.append("transHistLast5");
			csvWriter.append("\n");
			
			//vai šo neavar īsāk?
			for (Client rowData : clientList) {
				csvWriter.append(rowData.firstName);
				csvWriter.append(",");
				csvWriter.append(rowData.lastName);
				csvWriter.append(",");
				csvWriter.append(""+rowData.pkPart1);
				csvWriter.append(",");
				csvWriter.append(""+rowData.pkPart2);
				csvWriter.append(",");
				csvWriter.append(rowData.id);
				csvWriter.append(",");
				csvWriter.append(""+rowData.password);
				csvWriter.append(",");
				csvWriter.append(""+rowData.balance);
				csvWriter.append(",");
				csvWriter.append(""+rowData.lastDeposit);
				csvWriter.append(",");
				csvWriter.append(""+rowData.lastWithdrawal);
				csvWriter.append(",");
				csvWriter.append(Arrays.toString(rowData.transHistLast5));
				csvWriter.append("\n");
			}

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public static double getCurrentUSD() {
		Document doc=null;
		try{
			doc = Jsoup.connect("https://www.bank.lv/").userAgent("Mozilla/72.0.2").get();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Elements rows = doc.getElementsByTag("td");
		double USD = 0;
		for (int i=0; i<rows.size(); i++) {
			if (rows.get(i).getElementsByTag("td").first().text().equals("USD")) {
				USD=Double.parseDouble(rows.get(i+1).getElementsByTag("td").first().text());
				break;
			}
		}
		return USD;
	}
	
	public static double getCurrentEUR() {
		Document doc=null;
		try{
			doc = Jsoup.connect("https://www.exchangerates.org.uk/Dollars-to-Euros-currency-conversion-page.html").userAgent("Mozilla/72.0.2").get();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Element curValEur = doc.getElementById("shd2b;");
		double EUR=Double.parseDouble(curValEur.getElementsByTag("span").first().text());
		return EUR;
	}

}
