package amata1219.parkour.message;

import org.bukkit.command.CommandSender;

import amata1219.parkour.text.Text;
import amata1219.parkour.tuplet.Tuple;

public interface MessageStyle {

	default void sendTo(Tuple<CommandSender, Text> map){
		sendTo(map.first, map.second);
	}

	void sendTo(CommandSender receiver, Text text);

}
