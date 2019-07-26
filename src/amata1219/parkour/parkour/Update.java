package amata1219.parkour.parkour;

import org.bukkit.ChatColor;

public enum Update {

	Update1(ChatColor.GREEN, 10, 10),
	Update2(ChatColor.YELLOW, 50, 50),
	Update3(ChatColor.AQUA, 100, 100),
	Update4(ChatColor.DARK_GRAY, 300, 300),
	Update5(ChatColor.GOLD, 500, 500),
	Update6(ChatColor.GRAY, 700, 700),
	Update7(ChatColor.BLACK, 1000, 1000),
	Update8(ChatColor.BLUE, 2000, 2000),
	Update9(ChatColor.DARK_AQUA, 5000, 5000),
	Update10(ChatColor.DARK_RED, 10000, 5000),
	Update11(ChatColor.LIGHT_PURPLE, 30000, 15000),
	Update12(ChatColor.DARK_PURPLE, 50000, 30000),
	Update13(ChatColor.DARK_BLUE, 100000, 50000),
	Doombless(null, 500000, 30000);

	public final ChatColor color;
	public final int firstRewardCoins;
	public final int secondAndSubsequentTimesRewardCoins;

	private Update(ChatColor color, int firstRewardCoins, int secondAndSubsequentTimesRewardCoins){
		this.color = color;
		this.firstRewardCoins = firstRewardCoins;
		this.secondAndSubsequentTimesRewardCoins = secondAndSubsequentTimesRewardCoins;
	}

}
