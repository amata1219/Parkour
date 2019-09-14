package amata1219.beta.parkour.course;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import amata1219.beta.parkour.location.ImmutableLocation;
import amata1219.beta.parkour.region.VisibleRegion;
import amata1219.beta.parkour.serialize.Deserializer;
import amata1219.parkour.region.Region;
import graffiti.Maybe;
import graffiti.Yaml;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class Course {

	public final String name;
	public String description;
	public Category category;
	public ChatColor courseColor;
	public Color boundaryColor;
	public ImmutableLocation spawn;
	public Region region;
	public VisibleRegion startLine, finishLine;
	public CheckAreaSet checkAreas;
	public int[] rewards;
	public boolean timeAttackEnable;
	//public RecordSet records;
	public Map<UUID, PlayerConnection> connections = new HashMap<>();
	public boolean enable;


	public Course(Yaml yaml){
		name = yaml.name;
		description = yaml.getString("Description");

		yaml.get(yml -> yml.getString("Category"))
		.bind(Category::valueOf)
		.ifJustOrElse(this::setCategory, Category.NORMAL);

		yaml.get(yml -> yml.getString("Course color"))
		.bind(ChatColor::valueOf)
		.ifJustOrElse(this::setCourseColor, ChatColor.WHITE);

		yaml.get(yml -> yml.getString("Boundary color"))
		.bind(text -> Deserializer.stream(text).map(Integer::parseInt, int.class, 1, 3).deserializeTo(Color.class))
		.ifJustOrElse(this::setBoundaryColor, Color.WHITE);
	}

	public Category category(){
		return category;
	}

	public void setCategory(Category category){
		if(category != null) this.category = category;
	}

	public ChatColor courseColor(){
		return courseColor;
	}

	public void setCourseColor(ChatColor courseColor){
		if(courseColor != null) this.courseColor = courseColor;
	}

	public Color boundaryColor(){
		return boundaryColor;
	}

	public void setBoundaryColor(Color boundaryColor){
		if(boundaryColor != null) this.boundaryColor = boundaryColor;
	}

	/*
	 * 	public final String name;
	public final String prefixColor;
	public boolean enable;
	public ParkourCategory category;
	public Color borderColor;
	public Region region;
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
