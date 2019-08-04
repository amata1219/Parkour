package amata1219.parkour.stage;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;

public class Stage {

	private final ParkourSet parkourSet = ParkourSet.getInstance();

	//ステージ名
	public final String name;

	//スポーン地点
	public ImmutableEntityLocation spawnLocation;

	//ステージ内のアスレの名前リスト
	public final List<String> parkourNames;

	public Stage(Yaml yaml){
		name = yaml.name;

		spawnLocation = ImmutableEntityLocation.deserialize(yaml.getString("Spawn location"));

		parkourNames = yaml.getStringList("Parkour list");
	}

	//このステージ内のアスレを取得する
	public List<Parkour> getParkourList(){
		return parkourNames.stream().filter(parkourSet::containsParkour).map(parkourSet::getParkour).collect(Collectors.toList());
	}

	public void save(){
		Yaml yaml = new Yaml(Main.getPlugin(), new File(StageSet.getInstance().folder, StringTemplate.apply("$0.yml", name)));

		//スポーン地点を記録する
		yaml.set("Spawn location", spawnLocation.serialize());

		//アスレリストを記録する
		yaml.set("Parkour list", parkourNames);

		//セーブする
		yaml.save();
	}

}
