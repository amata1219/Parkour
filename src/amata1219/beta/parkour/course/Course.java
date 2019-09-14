package amata1219.beta.parkour.course;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;

import amata1219.beta.parkour.location.ImmutableLocation;
import amata1219.parkour.region.Region;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class Course {

	public final String name;
	public final ChatColor color;
	public Category category;
	public ImmutableLocation spawn;
	public Region region;
	public Map<UUID, PlayerConnection> connections = new HashMap<>();

	public boolean enable;

	/*
	 * public Region region;
	public ImmutableLocation spawn;
	public ParkourRegion startLine, finishLine;
	public CheckAreas checkAreas;
	public Rewards rewards;
	public boolean timeAttackEnable;
	public Records records;
	public String description;
	public PlayerConnections connections = new PlayerConnections();

	 */

	public static enum Category {

		NORMAL,
		SEGMENT,
		BIOME,
		UPDATE,
		EXTEND;

	}

}
