package amata1219.parkour.string.message;

import amata1219.parkour.string.StringColor;

public class MessageColor {

	public static Message color(String text){
		return color('&', text);
	}

	public static Message color(char alternateColorCode, String text) {
		return Message.wrap(StringColor.color(alternateColorCode, text));
	}

}
