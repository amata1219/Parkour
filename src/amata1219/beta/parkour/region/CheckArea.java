package amata1219.beta.parkour.region;

import org.bukkit.World;

import amata1219.beta.parkour.course.Course;

public class CheckArea extends VisibleRegion {

	public final int number;

	public CheckArea(World world, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Course course) {
		super(world, minX, minY, minZ, maxX, maxY, maxZ, course);
	}

}
