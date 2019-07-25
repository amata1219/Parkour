package amata1219.parkour.stage;

import java.util.List;
import java.util.stream.Collectors;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;

public class Stage {

	//ステージ名
	public final String name;

	//アスレのリスト
	public final List<Parkour> parkourList;

	public Stage(Yaml yaml){
		name = yaml.name;

		//アスレ名をそれに対応したアスレに変換しリスト化する
		parkourList = yaml.getStringList("ParkourList")
							.stream()
							.map(Main.getParkourSet().parkourMap::get)
							.collect(Collectors.toList());
	}

}
