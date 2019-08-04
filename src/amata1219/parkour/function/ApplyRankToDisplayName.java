package amata1219.parkour.function;

import org.bukkit.entity.Player;

import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.RankColor;
import amata1219.parkour.user.User;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;

public class ApplyRankToDisplayName {

	public static void apply(User user){
		//ニックネームAPIを取得する
		BetterNickAPI api = Main.getNickAPI();

		int rank = user.updateRank;

		//プレイヤーを取得する
		Player player = user.asBukkitPlayer();

		//表示例: amata1219 @ 5
		String displayName = StringTemplate.applyWithColor("$0 &7-@ $1$2", player.getName(), RankColor.values()[rank].color, rank);

		//表示名を変更する
		api.setPlayerDisplayName(player, displayName, "", "");
		api.setPlayerChatName(player, displayName, "", "");
		api.setPlayerTablistName(player, displayName, "", "");
	}

}
