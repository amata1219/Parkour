package amata1219.parkour.message;

import java.util.Collection;

import amata1219.parkour.command.Sender;
import amata1219.parkour.text.Text;
import amata1219.parkour.tuplet.Tuple;

public class Messenger {

	private final Collection<Tuple<Sender, Text>> tuples;

	public Messenger(Collection<Tuple<Sender, Text>> tuples){
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
