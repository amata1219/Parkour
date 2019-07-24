package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import amata1219.amalib.region.Region;
import amata1219.amalib.yaml.Yaml;

public class ParkourSetting {

	//空間
	public final Region region;

	//スタートライン
	public final Area startLine;

	//ゴールライン
	public final Area goalLine;

	//チェックエリアリスト
	public final List<Area> areas = new ArrayList<>();

	public ParkourSetting(Parkour parkour, Yaml yaml){
		World world = parkour.world;

		region = Region.fromString(world, yaml.getString("Region"));

		startLine = Area.fromString(world, yaml.getString("Start line"));
		goalLine = Area.fromString(world, yaml.getString("Goal line"));

		for(String text : yaml.getStringList("Check areas"))
			areas.add(Area.fromString(world, text));
	}

}
