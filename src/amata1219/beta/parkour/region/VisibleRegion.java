package amata1219.beta.parkour.region;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import amata1219.beta.parkour.location.ImmutableLocation;
import amata1219.parkour.region.LocationOnBorderCollector;
import amata1219.parkour.util.Color;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.ParticleParamRedstone;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class VisibleRegion extends Region {

	private final Map<UUID, PlayerConnection> connections;
	private List<PacketPlayOutWorldParticles> packets;
	private BukkitTask task;

	public VisibleRegion(World world, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Map<UUID, PlayerConnection> connections) {
		super(world, minX, minY, minZ, maxX, maxY, maxZ);
		this.connections = connections;
	}

	public void generateParticlePackets(){
		boolean running = task != null;

		if(running) undisplayBoundaries();

		List<ImmutableLocation> locations = LocationOnBorderCollector.collect(this, 4);

		Color color = parkour.borderColor;

		//各座標に対応したパーティクルパケットを作成する
		packets = locations.stream()
							.map(location -> {
								float red = color.adjustRed(30) / 255f;
								float green = color.adjustGreen(30) / 255f;
								float blue = color.adjustBlue(30) / 255f;

								return new PacketPlayOutWorldParticles(new ParticleParamRedstone(red, green, blue, 1), true,
										(float) location.x, (float) location.y + 0.15f, (float) location.z,
										red, green, blue, 1, 0);
								})
							.collect(Collectors.toList());

		position = 0;

		if(running) displayBorders();
	}

	public void displayBoundaries(){

	}

	public void undisplayBoundaries(){

	}

}
