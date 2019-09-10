package amata1219.beta.parkour.serialize;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Serializer {

	public static String serialize(Object... objects){
		return String.join(",", Arrays.stream(objects).map(Object::toString).collect(Collectors.toList()));
	}

}
