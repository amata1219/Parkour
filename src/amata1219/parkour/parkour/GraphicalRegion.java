package amata1219.parkour.parkour;

import java.util.Arrays;

import org.bukkit.Color;

import amata1219.amalib.region.Region;

public class GraphicalRegion {

	public final Parkour parkour;
	public final Region region;
	public final BordorDisplayer displayer;

	public static GraphicalRegion fromString(Parkour parkour, String text){
		int[] values = Arrays.stream(text.split(","))
								.mapToInt(Integer::parseInt)
								.toArray();
		Region space = new Region(parkour.world, values[0], values[1], values[2], values[3], values[4], values[5]);
		Color color = Color.fromRGB(values[6], values[7], values[8]);
		return new GraphicalRegion(parkour, space, color);
	}

	public GraphicalRegion(Parkour parkour, Region space, Color color){
		this.parkour = parkour;
		this.region = space;
		displayer = new BordorDisplayer(space, color);
	}

}
