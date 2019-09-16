package amata1219.parkour.command;

import org.bukkit.entity.Player;

import amata1219.parkour.tweet.Tweet;
import graffiti.ArgList;
import graffiti.PlayerCommand;
import graffiti.Text;

public class TweetCommand implements PlayerCommand {

	@Override
	public void onCommand(Player player, ArgList args) {
		args.join(0, args.length()).ifJust(
			t -> Tweet.display(player, t)
		).ifNothing(() -> Text.of("&b-テキストを入力して下さい。", "&c-Please enter text.", player).displayOnActionBar(player));
	}

}
