package amata1219.obsolete.parkour.function;

import org.bukkit.entity.Player;

import amata1219.obsolete.parkour.parkour.RankColor;
import amata1219.obsolete.parkour.text.Text;
import amata1219.obsolete.parkour.user.User;

public class ImprintRank {

	public static void apply(User user){
		Player player = user.asBukkitPlayer();
		int rank = user.updateRank();

		//表示例: amata1219 @ 5
		String displayName = Text.stream("$rank_color$player_name &7-@ $rank_color$rank-&r")
				.setAttribute("$rank_color", RankColor.values()[rank].color)
				.setAttribute("$player_name", player.getName())
				.setAttribute("$rank", rank)
				.color()
				.toString();

		player.setDisplayName(displayName);
		player.setPlayerListName(displayName);
	}

}
