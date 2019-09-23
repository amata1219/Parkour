package amata1219.beta.parkour.region;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import amata1219.beta.parkour.color.RGBColor;
import amata1219.beta.parkour.course.ConnectionSet;
import amata1219.beta.parkour.course.Course;
import amata1219.beta.parkour.location.ImmutableLocation;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.ParticleParamRedstone;

public class VisibleRegion extends Region {

	private final Course course;
	private List<PacketPlayOutWorldParticles> packets;
	private int position;
	private BukkitTask task;

	public VisibleRegion(World world, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Course course) {
		super(world, minX, minY, minZ, maxX, maxY, maxZ);
		this.course = course;
	}

	public void generateParticlePackets(){
		boolean running = task != null;

		if(running) undisplayBoundaries();

		List<ImmutableLocation> locs = LocationOnBorderCollector.collect(this, 4);

		RGBColor color = course.boundaryColor();

		packets = locs.stream()
						.map(loc -> {
							float red = color.adjustRed(30) / 255f;
							float green = color.adjustGreen(30) / 255f;
							float blue = color.adjustBlue(30) / 255f;
							return new PacketPlayOutWorldParticles(new ParticleParamRedstone(red, green, blue, 1), true,
								(float) loc.x, (float) loc.y + 0.15f, (float) loc.z, red, green, blue, 1, 0);
						})
						.collect(Collectors.toList());

		position = 0;

		if(running) displayBoundaries();
	}

	public void displayBoundaries(){
		if(task != null) return;

		ConnectionSet connections = course.connections;

		if(connections.isEmpty()) return;

		final int size = packets.size();
		final int halfSize = size / 2;
		final int lastIndex = size - 1;

		task = Async.define(() -> {
			if(position >= size) position = 0;

			PacketPlayOutWorldParticles packet1 = packets.get(position);
			PacketPlayOutWorldParticles packet2 = packets.get(position < halfSize ? position + halfSize : position + halfSize - lastIndex);

			position++;

			connections.runForTraceurConnections(connection -> {
				EntityPlayer player = connection.player;

				int viewChunks = player.clientViewDistance.intValue();

				double xDistance = (int) Math.abs(lesserBoundaryCorner.x - player.locX) >> 4;
				double zDistance = (int) Math.abs(lesserBoundaryCorner.z - player.locZ) >> 4;

				if(xDistance > viewChunks || zDistance > viewChunks) return;

				connection.sendPacket(packet1);
				connection.sendPacket(packet2);
			});
		}).executeTimer(0, 1);
	}

	public void undisplayBoundaries(){
		if(task == null) return;

		task.cancel();
		task = null;
	}

}
