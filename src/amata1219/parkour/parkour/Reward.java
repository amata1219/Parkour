package amata1219.parkour.parkour;

public class Reward {

	private final int[] coins;

	public Reward(int[] coins){
		this.coins = coins;
	}

	public int getReward(int numberOfTimesCleared){
		return coins[Math.min(numberOfTimesCleared, coins.length - 1)];
	}

}
