package amata1219.parkour.stage;

import java.util.List;
import java.util.stream.Collectors;

import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;

public class Stage {

	private final StageSet stages = StageSet.getInstance();
	private final ParkourSet parkourSet = ParkourSet.getInstance();

	//ステージ名
	public final String name;

	//カテゴリー
	public final StageCategory category;

	//スポーン地点
	private ImmutableEntityLocation spawnLocation;

	//ステージ内のアスレの名前リスト
	public final List<String> parkourNames;

	public Stage(Yaml yaml){
		name = yaml.name;

		category = StageCategory.valueOf(yaml.getString("Category"));

		setSpawnLocation(ImmutableEntityLocation.deserialize(yaml.getString("Spawn location")));

		parkourNames = yaml.getStringList("Parkour list");
	}

	public ImmutableEntityLocation getSpawnLocation(){
		return spawnLocation;
	}

	//スポーン地点を設定する
	public void setSpawnLocation(ImmutableEntityLocation spawnLocation){
		//ブロック中央に座標を修正する
		this.spawnLocation = spawnLocation.middle();
	}

	//このステージ内のアスレを取得する
	public List<Parkour> getParkourList(){
		return parkourNames.stream().filter(parkourSet::containsParkour).map(parkourSet::getParkour).collect(Collectors.toList());
	}

	public void save(){
		Yaml yaml = stages.getYaml(name);

		//カテゴリーを記録する
		yaml.set("Category", category.toString());

		//スポーン地点を記録する
		yaml.set("Spawn location", spawnLocation.serialize());

		//アスレリストを記録する
		yaml.set("Parkour list", parkourNames);

		//セーブする
		yaml.save();
	}

}
