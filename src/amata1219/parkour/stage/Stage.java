package amata1219.parkour.stage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;

public class Stage {

	//ステージ名
	public final String name;

	//ステージ内のアスレの名前リスト
	public final List<String> parkourNames;

	//アスレのリスト
	public final List<Parkour> parkourList;

	public Stage(Yaml yaml){
		name = yaml.name;

		parkourNames = yaml.getStringList("ParkourList");

		Map<String, Parkour> parkourMap = Main.getParkourSet().parkourMap;

		//アスレ名からアスレを取得しリスト化する
		parkourList = parkourNames
							.stream()
							.filter(parkourMap::containsKey)
							.map(parkourMap::get)
							.collect(Collectors.toList());
	}

}
