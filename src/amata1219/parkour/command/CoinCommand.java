package amata1219.parkour.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class CoinCommand implements Command {

	private final Users users = Users.getInstnace();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(!args.hasNext()){
			sender.warn(StringTemplate.apply(": Syntax error > /coin player_name (deposit|withdraw) coins | /coin player_name see"));
			return;
		}

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
		default:
			break;
		}

		@SuppressWarnings("deprecation")
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);

		if(player == null || !player.hasPlayedBefore()){
			sender.warn(StringTemplate.apply(": Value error > $0はサーバーに参加した事がありません。", playerName));
			return;
		}

		UUID uuid = player.getUniqueId();

		if(!users.containsUser(uuid)){
			sender.warn(StringTemplate.apply(": Value error > $0はサーバーに参加した事がありません。", playerName));
			return;
		}

		//ユーザーを取得する
		User user = users.getUser(uuid);

		//第2引数で分岐する
		switch(args.next()){
		case "deposit":{
			if(!args.hasNextInt()){
				sender.warn(StringTemplate.apply(": Syntax error > /coin $0 deposit coins", playerName));
				return;
			}

			int coins = args.nextInt();

			user.depositCoins(coins);

			sender.info(StringTemplate.apply(": Success > $0に$1コイン与えました。", playerName, coins));
			return;
		}case "withdraw":{
			if(!args.hasNextInt()){
				sender.warn(StringTemplate.apply(": Syntax error > /coin $0 deposit coins", playerName));
				return;
			}

			int coins = args.nextInt();

			user.withdrawCoins(coins);

			sender.info(StringTemplate.apply(": Success > $0から$1コイン奪いました。", playerName, coins));
			return;
		}case "see":{
			sender.info(StringTemplate.apply(": Information > $0は$1コイン持っています。", playerName, user.getCoins()));
			return;
		}default:
			sender.warn(StringTemplate.apply(": Syntax error > /coin $0 (deposit|withdraw) coins | /coin $0 see", playerName));
			return;
		}
	}

}
