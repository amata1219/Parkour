package amata1219.parkour.command;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.parkour.Tweet;

public class TweetCommand implements Command {

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender)) return;

		//引数が無ければ戻る
		if(!args.hasNext()){
			sender.warn("テキストを入力して下さい。");
			return;
		}

		String text = args.getInRange(0, args.args.length);

		Tweet.display(sender.asPlayerCommandSender(), text);
	}
}
