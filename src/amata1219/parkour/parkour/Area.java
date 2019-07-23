package amata1219.parkour.parkour;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

import com.google.common.collect.ImmutableList;

import amata1219.amalib.color.RGB;
import amata1219.amalib.location.ImmutableLocation;
import amata1219.amalib.space.Space;

public class Area {

	public final Space space;
	public final RGB color;
	public final List<ParticlePacket> list;

	public static Area fromString(World world, String text){
		int[] a = Arrays.stream(text.split(",")).mapToInt(Integer::parseInt).toArray();
		Space space = new Space(world, a[0], a[1], a[2], a[3], a[4], a[5]);
		RGB color = new RGB(a[6], a[7], a[8]);
		return new Area(space, color);
	}

	public Area(Space space, RGB color){
		this.space = space;
		this.color = color;

		ImmutableLocation lesser = space.lesserBoundaryCorner;
		ImmutableLocation greater = space.greaterBoundaryCorner;

		ImmutableList.Builder<ParticlePacket> builder = ImmutableList.builder();

		float height = (float) lesser.getY() + 1;

		for(float x = (float) lesser.getX(); x <= greater.getX() + 1; x += 0.5f)
			builder.add(new ParticlePacket(x, height, (float) lesser.getZ(), color));

		for(float z = (float) lesser.getZ(); z <= greater.getZ() + 1; z += 0.5f)
			builder.add(new ParticlePacket((float) greater.getX(), height, z, color));

		for(float x = (float) greater.getX(); x >= lesser.getX(); x -= 0.5f)
			builder.add(new ParticlePacket(x, height, (float) lesser.getZ(), color));

		for(float z = (float) greater.getZ(); z >= lesser.getZ(); z -= 0.5f)
			builder.add(new ParticlePacket((float) greater.getX(), height, z, color));

		list = builder.build();
	}

}
