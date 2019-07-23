package amata1219.parkour.parkour;

import java.util.Arrays;

import org.bukkit.World;

import amata1219.amalib.color.RGB;
import amata1219.amalib.space.Region;

public class Area {

	public final Region region;
	public final BordorDisplayer displayer;

	public static Area fromString(World world, String text){
		int[] values = Arrays.stream(text.split(","))
								.mapToInt(Integer::parseInt)
								.toArray();
		Region space = new Region(world, values[0], values[1], values[2], values[3], values[4], values[5]);
		RGB color = new RGB(values[6], values[7], values[8]);
		return new Area(space, color);
	}

	public Area(Region space, RGB color){
		this.region = space;
		displayer = new BordorDisplayer(space, color);
	}

}
