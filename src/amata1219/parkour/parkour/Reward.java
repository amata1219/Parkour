package amata1219.parkour.parkour;

public enum Reward {

	Update1(10, 10),
	Update2(50, 50),
	Update3(100, 100),
	Update4(1000, 1000),
	Update5(3000, 1000),
	Update6(5000, 3000),
	Update7(8000, 5000),
	Update8(10000, 8000),
	Update9(30000, 15000),
	Update10(500000, 30000),
	Update11(80000, 30000),
	Update12(100000, 50000),
	Update13(300000, 100000),
	Doombless(800000, 500000);

	public final int first;
	public final int afterSecondTime;

	private Reward(int first, int afterSecondTime){
		this.first = first;
		this.afterSecondTime = afterSecondTime;
	}

}
