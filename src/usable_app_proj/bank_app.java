package usable_app_proj;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class bank_app {
	static final String SEPARATOR = ",";
	static int activeUsers = 0;
	static String userName = "";
	static String userSurname = "";
	static String password = "";
	static Scanner scanner = new Scanner(System.in);
	//static Client client1 = new Client("Jo", "Chi", 150989, 45625);
	//static Client client2 = new Client("John", "Snow", 160881, 55937);
	//static Client client3 = new Client("Jef", "White", 170778, 65358);
	//static Client client4 = new Client("Luis", "Black", 180688, 75411);
	//static Client client5 = new Client("Ebber", "Berry", 190590, 85529);

	//static Client[] clientArray = { client1, client2, client3, client4, client5 };
	static ArrayList<Client> clientList = new ArrayList<Client>();
	
	public static void main(String[] args) {
		String pathToFile = "D:\\Eclipse\\workspace\\bank_app\\client.csv";
		createClientList(pathToFile);
		clientList.get(0).balance=65;
		//System.out.println(clientList.get(0).balance);
		inputChoice();
	}
	/*
	 * Jāizveido, lai nolasa visu tabulu un aizpilda objektam visu informāciju, kura ir tabulā, 
	 * ne tikai kātagad, ka vārdu, uzvāru un pk abas daļas.
	 * jāizveido arī tā, ka pie logout saglabā ekseļa tabulā visu no jauna ievadīto objekta info vai arī izmainīto
	 */
	public static void createClientList(String filePath) {
		Path pathObject = Paths.get(filePath);
		Charset charset = Charset.forName("UTF-8");
		try {
			List<String> lines = Files.readAllLines(pathObject, charset);
			for (int i=1; i<lines.size(); i++) {
				String[] lineArr = lines.get(i).split(SEPARATOR);
				clientList.add(new Client(lineArr[0],lineArr[1], Integer.parseInt(lineArr[2]), Integer.parseInt(lineArr[3])));
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public static void fillClientObj (String filePath) {
		Path pathObject = Paths.get(filePath);
		Charset charset = Charset.forName("UTF-8");
		try {
			List<String> lines = Files.readAllLines(pathObject, charset);
			for (int i=1; i<lines.size(); i++) {
				String[] lineArr = lines.get(i).split(SEPARATOR);
				clientList.get(i).password = lineArr[5];
				clientList.get(i).balance = Integer.parseInt(lineArr[6]); //pars jāuzliek visiem skaitļiem
				clientList.get(i).lastDeposit = Integer.parseInt(lineArr[7]);
				clientList.get(i).lastWithdrawal = Integer.parseInt(lineArr[8]);
				//clientList.get(i).transHistLast5 =lineArr[9]; kad aizpildīsies šī šūna exc jāskatās kā to var nolasīt, jo būs jau str masīvs vai pat masīvs nebūs
				//clientList.add(new Client(lineArr[0],lineArr[1], Integer.parseInt(lineArr[2]), Integer.parseInt(lineArr[3])));
			}
		} catch (IOException e) {
			System.out.println(e);
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
			//te ir kļūda, tad kad izņem naudu, masīvā aizvietojās visas pozīcijas ar izņemto summu
			showHistory();
			break;
		case 8:
			convert();
			break;
		case 9:
			logout();
			break;
		}
		scanner.close();
	}

	/*
	 * pie šī jāpiestrādā, jāpameklē vai var pievienot šādi jaunu objektu caur
	 * konsoli pagaidām jātaisa, ka esošam objektam vienkārši izveido paroli
	 */
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

	/*
	 * izveidot array ar objektiem un tad loopā iet cauri viesiem objektiem, atrast
	 * vai tāds vārds ir starp objektiem un vai atbilstošais vārds saskan ar paroli
	 * tam pašam objektam
	 */
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
			}
		}
		if (activeUsers == 0) {
			System.out.println("Something is wrong. Try again");
		}
		inputChoice();
		scanner.close();
	}

	/*
	 * balstoties uz register() un login() izveidoto vārdu un uzvārdu, atrod īsto
	 * user array sarakstā un izdrukā tam konta atlikumu jāizveido, ka konta
	 * atlikums nevar būt negatīvs
	 */
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
				for (int j = 0; j < clientList.get(i).transHistLast5.length; j++) {
					if (clientList.get(i).transHistLast5[j] == 0) {
						clientList.get(i).transHistLast5[j] = deposit;
						break;
					}
				}
				System.out.println("Your balance is: " + clientList.get(i).balance);
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
					for (int j = 0; j < clientList.get(i).transHistLast5.length; j++) {
						if (clientList.get(i).transHistLast5[j] == 0) {
							clientList.get(i).transHistLast5[j] = withdrawal * -1;
							break;
							/*zemāk jāizveido, lai gadījumā, kad array ir viss aizņemts (nav 0 vērtības), 
							pirmā pozīcija tiek aizņemta ar šo darbību un pārējās pabīdītas vienu indexu tālāk un pēdējais izdzēsts
							visticamāk jāpārveido par atsevišķu metodi un jāievieto arī pie makeDeposit()
							jāpārbauda vai vispār tā strādā !!!!!!!
							esošais variants nestrādā
							*/ 
						}if (!(clientList.get(i).transHistLast5[j] == 0)) {
							for (int a=1; a<clientList.get(i).transHistLast5.length-1;a++) {
								clientList.get(i).transHistLast5[a] = clientList.get(i).transHistLast5[a-1];
							}
							clientList.get(i).transHistLast5[0] = withdrawal * -1;
							
						}
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
		double usd = eur * 1.11;
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
		double eur = usd * 0.9;
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
//logout() jāizveido, ka papildina tabulu ar jaunajiem datiem pirms visas pārējās darbības tiek izdarītas
	public static void logout() {
		//tie ies metode, kas papildina exc tabulu, tikai jāatrod, ka jau esošai var tikai vajadzīgās šūnas papildināt
		activeUsers = 0;
		userName = "";
		userSurname = "";
		password = "";
		System.out.println(userName + userSurname + password);
		System.out.println("You are loged out");
	}

}
