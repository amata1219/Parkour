package amata1219.parkour.stage;

import java.util.List;

import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.yaml.Yaml;

public class Stage {

	//ステージ名
	public final String name;

	//スポーン地点
	public ImmutableEntityLocation spawnLocation;

	//ステージ内のアスレの名前リスト
	public final List<String> parkourList;

	public Stage(Yaml yaml){
		name = yaml.name;

		spawnLocation = ImmutableEntityLocation.deserialize(yaml.getString("Spawn location"));

		parkourList = yaml.getStringList("Parkour list");
	}

	public void save(){

	}

}
