package amata1219.parkour.command;

import org.bukkit.entity.Player;

import amata1219.parkour.string.message.MessageColor;
import amata1219.parkour.tweet.Tweet;

public class TweetCommand implements Command {

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender)) return;

		Player player = sender.asPlayerCommandSender();

		//引数が無ければ戻る
		if(!args.hasNext()){
			MessageColor.color("&c-Execution blocked &7-@ &c-Text can not be empty").displayOnActionBar(player);
			return;
		}

		//全引数を結合して取得する
		String text = args.getRange(0, args.getLength());

		//ツイート用のメッセージを表示する
		Tweet.display(player, text);
	}
}
