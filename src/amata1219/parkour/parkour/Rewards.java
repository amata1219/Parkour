package amata1219.parkour.parkour;

import amata1219.amalib.string.StringJoin;

public class Rewards {

	private final int[] coins;

	public Rewards(int[] coins){
		this.coins = coins;
	}

	public int getReward(int numberOfTimesCleared){
		return coins[Math.min(numberOfTimesCleared, coins.length - 1)];
	}

	public String serialize(){
		return StringJoin.join(coins, ",");
	}

}
