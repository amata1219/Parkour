package amata1219.beta.parkour.region;

import org.bukkit.World;

public class VisibleRegion extends Region {

	public VisibleRegion(World world, double lesserBoundaryCornerX, double lesserBoundaryCornerY,
			double lesserBoundaryCornerZ, double greaterBoundaryCornerX, double greaterBoundaryCornerY,
			double greaterBoundaryCornerZ) {
		super(world, lesserBoundaryCornerX, lesserBoundaryCornerY, lesserBoundaryCornerZ, greaterBoundaryCornerX,
				greaterBoundaryCornerY, greaterBoundaryCornerZ);
	}

}
