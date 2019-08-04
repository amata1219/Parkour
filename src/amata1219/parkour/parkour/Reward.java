package amata1219.parkour.parkour;

public enum Reward {

	Update1(10, 10),
	Update2(50, 50),
	Update3(100, 100),
	Update4(300, 300),
	Update5(500, 500),
	Update6(700, 700),
	Update7(1000, 1000),
	Update8(2000, 2000),
	Update9(5000, 5000),
	Update10(10000, 10000),
	Update11(30000, 15000),
	Update12(50000, 30000),
	Update13(100000, 50000),
	Doombless(500000, 30000);

	public final int first;
	public final int afterSecondTime;

	private Reward(int first, int afterSecondTime){
		this.first = first;
		this.afterSecondTime = afterSecondTime;
	}

}
