package amata1219.beta.parkour.serialize;

import com.google.common.base.Joiner;

public class Serializer {

	public static String serialize(Object... parts){
		return Joiner.on(',').join(parts);
	}

}
