package amata1219.parkour.message;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import amata1219.parkour.text.Text;
import amata1219.parkour.tuplet.Tuple;

public class Messenger {

	private final Collection<Tuple<CommandSender, Text>> tuples;

	public Messenger(Collection<Tuple<CommandSender, Text>> tuples){
		this.tuples = tuples;
	}

	public void sendChatMessage(){
		send(ChatMessage.INSTANCE);
	}

	public void sendActionBarMessage(){
		send(ActionBar.INSTANCE);
	}

	public void send(MessageStyle style){
		tuples.forEach(style::sendTo);
	}

}
