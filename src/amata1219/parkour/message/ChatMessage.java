package amata1219.parkour.message;

import amata1219.parkour.command.Sender;
import amata1219.parkour.text.Text;

public class ChatMessage implements MessageStyle {

	public static final ChatMessage INSTANCE = new ChatMessage();

	@Override
	public void sendTo(Sender receiver, Text text) {
		receiver.sendMessage(text.toString());
	}

}
