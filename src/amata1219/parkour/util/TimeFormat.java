package amata1219.parkour.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimeFormat {

	private static final SimpleDateFormat FORMAT;

	static{
		FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
		FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public static String format(float time){
		return FORMAT.format(time);
	}

}
