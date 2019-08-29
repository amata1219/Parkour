package amata1219.parkour.message;

import amata1219.parkour.command.Sender;
import amata1219.parkour.text.Text;
import amata1219.parkour.tuplet.Tuple;

public interface MessageStyle {

	default void sendTo(Tuple<Sender, Text> map){
		sendTo(map.first, map.second);
	}

	void sendTo(Sender receiver, Text text);

}
