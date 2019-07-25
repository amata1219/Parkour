package amata1219.parkour.parkour;

import org.bukkit.Color;

import amata1219.amalib.region.Region;

public class GraphicalRegion {

	public final Parkour parkour;
	public final Region region;
	public final BordorDisplayer displayer;

	public static GraphicalRegion fromString(Parkour parkour, Color color, String text){
		Region space = Region.fromString(parkour.world, text);
		return new GraphicalRegion(parkour, space, color);
	}

	public GraphicalRegion(Parkour parkour, Region space, Color color){
		this.parkour = parkour;
		this.region = space;
		displayer = new BordorDisplayer(space, color);
	}

}
