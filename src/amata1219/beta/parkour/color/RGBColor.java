package amata1219.beta.parkour.color;

import java.util.Random;

import amata1219.beta.parkour.serialize.Deserializer;
import amata1219.beta.parkour.serialize.Serializer;
import amata1219.beta.parkour.util.Range;

public class RGBColor {

	private static final Random RANDOM = new Random();

	public static RGBColor deserialize(String text){
		return Deserializer.stream(text)
							.map(Integer::parseInt, 0 ,2)
							.deserializeTo(RGBColor.class);
	}

	public final int red, green, blue;

	public RGBColor(int red, int green, int blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int adjustRed(int width){
		return adjust(red, width);
	}

	public int adjustGreen(int width){
		return adjust(green, width);
	}

	public int adjustBlue(int width){
		return adjust(blue, width);
	}

	private int adjust(int value, int width){
		return Range.limit(0, 255, value + RANDOM.nextInt(width) - (width / 2 - 1));
	}

	public String serialize(){
		return Serializer.serialize(red, green, blue);
	}

}
