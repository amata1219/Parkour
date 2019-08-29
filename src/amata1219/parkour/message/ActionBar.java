package amata1219.parkour.message;

import amata1219.parkour.command.Sender;
import amata1219.parkour.text.Text;

public class ActionBar implements MessageStyle {

	public static final ActionBar INSTANCE = new ActionBar();

	@Override
	public void sendTo(Sender receiver, Text text) {

	}

}
