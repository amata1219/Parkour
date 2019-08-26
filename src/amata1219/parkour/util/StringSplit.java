package amata1219.parkour.util;

import java.util.Arrays;

public class StringSplit {

	public static int[] splitToIntArguments(String text){
		return Arrays.stream(text.split(","))
						.mapToInt(Integer::parseInt)
						.toArray();
	}

}
