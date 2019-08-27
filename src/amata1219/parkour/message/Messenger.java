package amata1219.parkour.message;

import org.bukkit.entity.Player;

import amata1219.parkour.text.Text;
import amata1219.parkour.tuplet.Tuple;

public interface Messenger {

	default void sendTo(Tuple<Player, Text> map){
		sendTo(map.first, map.second);
	}

	void sendTo(Player player, Text text);

}
