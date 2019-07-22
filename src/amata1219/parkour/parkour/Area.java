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
	public final List<PacketPlayOutWorldParticles> list;

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

		ImmutableList.Builder<PacketPlayOutWorldParticles> builder = ImmutableList.builder();

		float height = (float) lesser.getY() + 1;

		for(float x = (float) lesser.getX(); x <= greater.getX() + 1; x += 0.5f)
			builder.add(new PacketPlayOutWorldParticles(x, height, (float) lesser.getZ(), color));
	}

	/*
	 * extra = 1
	 * data = (Object) null;
	 * CraftPlayer
	 * public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
			double offsetY, double offsetZ, double extra, T data) {
		if (data != null && !particle.getDataType().isInstance(data)) {
			throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
		} else {
			PacketPlayOutWorldParticles packetplayoutworldparticles = new PacketPlayOutWorldParticles(
					CraftParticle.toNMS(particle, data), true, (float) x, (float) y, (float) z, (float) offsetX,
					(float) offsetY, (float) offsetZ, (float) extra, count);
			this.getHandle().playerConnection.sendPacket(packetplayoutworldparticles);
		}
	}
	 */

}
