package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_13_R2.CraftParticle;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.ImmutableList;

import amata1219.amalib.location.ImmutableLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.schedule.Async;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.ParticleParam;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class GraphicalRegion {

	public final Parkour parkour;
	public final Region region;
	private final List<PacketPlayOutWorldParticles> particles;
	private final List<PlayerConnection> connections = new ArrayList<>();
	private int index1, index2;
	private BukkitTask task;

	public static GraphicalRegion fromString(Parkour parkour, Color color, String text){
		Region space = Region.fromString(parkour.world, text);
		return new GraphicalRegion(parkour, space, color);
	}

	public GraphicalRegion(Parkour parkour, Region space, Color color){
		this.parkour = parkour;
		this.region = space;

		ImmutableLocation lesser = space.lesserBoundaryCorner;
		ImmutableLocation greater = space.greaterBoundaryCorner;

		ParticlePacketListBuilder builder = new ParticlePacketListBuilder(color, (float) lesser.getEntityY() + 1);

		float unit = 1f / 3f;

		for(float x = (float) lesser.getEntityX(); x <= greater.getEntityX() + 1; x += unit)
			builder.make(x, (float) lesser.getEntityZ());

		for(float z = (float) lesser.getEntityZ(); z <= greater.getEntityZ() + 1; z += unit)
			builder.make((float) greater.getEntityX(), z);

		for(float x = (float) greater.getEntityX(); x >= lesser.getEntityX(); x -= unit)
			builder.make(x, (float) lesser.getEntityZ());

		for(float z = (float) greater.getEntityZ(); z >= lesser.getEntityZ(); z -= unit)
			builder.make((float) greater.getEntityX(), z);

		particles = builder.build();

		index2 = particles.size() / 2;
	}

	public void addPlayer(Player player){
		PlayerConnection connection = asEntityPlayer(player).playerConnection;
		if(connections.contains(connection))
			return;

		connections.add(connection);
		if(connections.size() == 1)
			display();
	}

	public void removePlayer(Player player){
		connections.remove(asEntityPlayer(player).playerConnection);
		if(connections.size() == 1)
			cancel();
	}

	private EntityPlayer asEntityPlayer(Player player){
		return ((CraftPlayer) player).getHandle();
	}

	private void display(){
		cancel();

		final int size = particles.size();

		task = Async.define(() -> {
			if(size >= index1)
				index1 = 0;
			else if(size >= index2)
				index2 = 0;

			PacketPlayOutWorldParticles packet1 = particles.get(index1++);
			PacketPlayOutWorldParticles packet2 = particles.get(index2++);

			for(PlayerConnection connection : connections){
				connection.sendPacket(packet1);
				connection.sendPacket(packet2);
			}
		}).executeTimer(0, 6);
	}

	private void cancel(){
		if(task != null)
			task.cancel();
	}

	private static class ParticlePacketListBuilder {

		private static final Random RANDOM = new Random();
		private static final float EXTRA = 1;
		private static final int COUNT = 0;
		private static final boolean FLAG = true;
		private static final ParticleParam PARTICLE = CraftParticle.toNMS(org.bukkit.Particle.REDSTONE, null);

		private final Color color;
		private final float y;

		private final ImmutableList.Builder<PacketPlayOutWorldParticles> builder = ImmutableList.builder();

		private ParticlePacketListBuilder(Color color, float y){
			this.color = color;
			this.y = y;
		}

		public void make(float x, float z){
			builder.add(new PacketPlayOutWorldParticles(PARTICLE, FLAG, x, y, z, mix(color.getRed()), mix(color.getGreen()), mix(color.getBlue()), EXTRA, COUNT));
		}

		public List<PacketPlayOutWorldParticles> build(){
			return builder.build();
		}

		private int mix(int color){
			return color + RANDOM.nextInt(30) - 14;
		}

	}

}
