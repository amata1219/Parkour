package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import amata1219.amalib.region.Region;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.stage.Stage;

public class Parkour {

	//パルクール名
	public final String name;

	//ワールド
	public final World world;

	//空間
	public final Region region;

	//スタートライン
	public final GraphicalRegion startLine;

	//ゴールライン
	public final GraphicalRegion goalLine;

	//チェックエリアリスト
	public final List<GraphicalRegion> checkAreas = new ArrayList<>();

	public Parkour(Yaml yaml){
		name = yaml.name;

		world = Bukkit.getWorld(yaml.getString("World"));

		region = Region.fromString(world, yaml.getString("Region"));

		startLine = GraphicalRegion.fromString(this, yaml.getString("Start line"));
		goalLine = GraphicalRegion.fromString(this, yaml.getString("Goal line"));

		for(String text : yaml.getStringList("Check areas"))
			checkAreas.add(GraphicalRegion.fromString(this, text));
	}

	public Stage getStage(){
		return Main.getStageSet().parkourNamesToStagesMap.get(name);
	}

}
