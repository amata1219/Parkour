package amata1219.parkour.util;

import java.text.SimpleDateFormat;

public class TimeFormat {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");

	public static String format(float time){
		return FORMAT.format(time);
	}

}
