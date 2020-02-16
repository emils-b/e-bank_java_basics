package usable_app_proj;

public class Client {
	String firstName;
	String lastName;
	int pkPart1;
	int pkPart2;
	String id;
	String password;
	int balance;
	int lastDeposit;
	int lastWithdrawal;
	int[] transHistLast5 = new int[5];
	static int clientCount = 0;
	
	public Client(String firstName, String lastName, int pkPart1, int pkPart2) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.pkPart1 = pkPart1;
		this.pkPart2 = pkPart2;
		clientCount++;
		this.id = makeID(); //izveido ID katram automātiski
	}
	
	//izveido klienta ID.
	public String makeID() {
		String part1 = this.firstName.toUpperCase().substring(0, 2);
		String part2 = this.lastName.toUpperCase().substring(this.lastName.length()-2, this.lastName.length());
		String part3 = Math.abs(this.pkPart2 - this.pkPart1) + "";
		String id = part1 + part3 + part2;
		return id;
	}
		
	public void getClientPk() {
		System.out.println(this.firstName + " " + this.lastName + " PK is: " + this.pkPart1 + "-" + this.pkPart2);
	}
	
	public int getClientCount() {
		return clientCount;
	}

}
