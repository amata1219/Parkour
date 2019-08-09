package amata1219.parkour.stage;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;

import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.Parkours;

public class Stage {

	private final Stages stages = Stages.getInstance();
	private final Parkours parkours = Parkours.getInstance();

	//ステージ名
	public final String name;

	//カテゴリー
	public final StageCategory category;

	//スポーン地点
	private ImmutableEntityLocation spawnPoint;

	//ステージ内のアスレの名前リスト
	public final List<String> parkourNames;

	public Stage(Yaml yaml){
		name = yaml.name;
		category = StageCategory.valueOf(yaml.getString("Category"));
		spawnPoint = ImmutableEntityLocation.deserialize(yaml.getString("Spawn points"));
		parkourNames = yaml.getStringList("Parkours");
	}

	public ImmutableEntityLocation getSpawnLocation(){
		return spawnPoint;
	}

	//スポーン地点を設定する
	public void setSpawnLocation(Location location){
		//ブロック中央に座標を修正する
		spawnPoint = new ImmutableEntityLocation(location).middle();
	}

	//このステージ内のアスレを取得する
	public List<Parkour> getParkourList(){
		return parkourNames.stream()
				.filter(parkours::containsParkour)
				.map(parkours::getParkour)
				.filter(parkour -> parkour.enable)
				.collect(Collectors.toList());
	}

	public void save(){
		Yaml yaml = stages.makeYaml(name);
		yaml.set("Category", category.toString());
		yaml.set("Spawn location", spawnPoint.serialize());
		yaml.set("Parkours", parkourNames);
		yaml.save();
	}

}
