package amata1219.beta.parkour.region;

import java.util.ArrayList;

import org.bukkit.World;

import amata1219.beta.parkour.color.RGBColor;
import amata1219.beta.parkour.course.ConnectionSet;
import amata1219.beta.parkour.course.Course;
import amata1219.beta.parkour.location.ImmutableLocation;
import amata1219.beta.parkour.task.AsyncTask;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.ParticleParamRedstone;

public class VisibleRegion extends Region {

	private final Course course;
	private ArrayList<PacketPlayOutWorldParticles> packets = new ArrayList<>();
	private AsyncTask task;

	public VisibleRegion(World world, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Course course) {
		super(world, minX, minY, minZ, maxX, maxY, maxZ);
		this.course = course;
	}

	public void generateBoundaries(){
		if(task != null) return;
		
		packets.clear();

		RGBColor color = course.boundaryColor();
		for(ImmutableLocation loc : LocationOnBorderCollector.collect(this, 4)){
			float r = color.adjustRed(30) / 255f;
			float g = color.adjustGreen(30) / 255f;
			float b = color.adjustBlue(30) / 255f;
			ParticleParamRedstone param = new ParticleParamRedstone(r, g, b, 1);
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(param, true,
					(float) loc.x, (float) loc.y + 0.15f, (float) loc.z, r, g, b, 1, 0);
			packets.add(packet);
		}
	}

	public void displayBoundaries(){
		if(task != null) return;

		ConnectionSet connections = course.connections;
		if(connections.isEmpty()) return;

		final int size = packets.size();
		final int half = size / 2;
		final int last = size - 1;
		
		task = AsyncTask.define(self -> {
			int index = (int) self.count() % size;
			PacketPlayOutWorldParticles packet = packets.get(index), other = packets.get(index < half ? index + half : index + half - last);
			connections.forEach(connection -> {
				EntityPlayer player = connection.player;
				int viewChunks = player.clientViewDistance.intValue();
				double distX = (int) Math.abs(min.x - player.locX) >> 4;
				double distZ = (int) Math.abs(min.z - player.locZ) >> 4;
				if(distX <= viewChunks && distZ <= viewChunks){
					connection.sendPacket(packet);
					connection.sendPacket(other);
				}
			});
		});
		
		task.executeTimer(1);
	}

	public void undisplayBoundaries(){
		if(task != null){
			task.cancel();
			task = null;
		}
	}

}
