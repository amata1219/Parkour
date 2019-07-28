package amata1219.parkour.command;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.text.StringTemplate;
import amata1219.parkour.Main;
import amata1219.parkour.user.User;

public class CoinCommand implements Command {

	private final Map<UUID, User> users = Main.getUserSet().users;

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//第1引数をプレイヤー名として取得する
		String playerName = args.next();

		//プレイヤー名とは関係無い機能の処理をする
		switch(playerName){
		case "help":
		case "commands":
		case "usage":
		case "howtouse":
			sender.warn(": Not implemented error > 実装されていません。");
			return;
		case "list":
				return;
		default:
			break;
		}

		@SuppressWarnings("deprecation")
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);

		if(player == null || !player.hasPlayedBefore()){
			sender.warn(StringTemplate.format(": Value error > [$0]はサーバーに参加した事がありません。", playerName));
			return;
		}

		UUID uuid = player.getUniqueId();

		if(!users.containsKey(uuid)){
			sender.warn(StringTemplate.format(": Value error > [$0]はサーバーに参加した事がありません。", playerName));
			return;
		}

		User user = users.get(uuid);

		//第2引数で分岐する
		switch(args.next()){
		case "deposit":{
			if(!args.hasNextInt()){
				sender.warn(StringTemplate.format(": Syntax error > /coin $0 deposit [coins]", playerName));
				return;
			}

			int coins = args.nextInt();

			user.depositCoins(coins);

			sender.info(StringTemplate.format(": Success > [$0]に[$1]コイン与えました。", playerName, coins));
			return;
		}case "withdraw":{
			if(!args.hasNextInt()){
				sender.warn(StringTemplate.format(": Syntax error > /coin $0 deposit [coins]", playerName));
				return;
			}

			int coins = args.nextInt();

			user.withdrawCoins(coins);

			sender.info(StringTemplate.format(": Success > [$0]から[$1]コイン奪いました。", playerName, coins));
			return;
		}case "see":{
			sender.info(StringTemplate.format(": Information > [$0]は[$1]コイン持っています。", playerName, user.getCoins()));
			return;
		}default:
			sender.warn(StringTemplate.format(": Syntax error > /coin $0 (deposit|withdraw) [coins] | /coin $0 see", playerName));
			return;
		}
	}

}
