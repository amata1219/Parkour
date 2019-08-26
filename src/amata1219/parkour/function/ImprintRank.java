package amata1219.parkour.function;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import amata1219.parkour.parkour.RankColor;
import amata1219.parkour.string.StringTemplate;
import amata1219.parkour.user.User;

public class ImprintRank {

	public static void apply(User user){
		int rank = user.updateRank();

		Player player = user.asBukkitPlayer();

		ChatColor rankColor = RankColor.values()[rank].color;

		//表示例: amata1219 @ 5
		String displayName = StringTemplate.capply("$0$1 &7-@ $0$2-&r", rankColor, player.getName(), rank);

		player.setDisplayName(displayName);
		player.setPlayerListName(displayName);
	}

}
