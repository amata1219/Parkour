package amata1219.parkour.stage;

import java.util.List;
import java.util.stream.Collectors;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkour;

public class Stage {

	//ステージ名
	public final String name;

	//パルクールのリスト
	public final List<Parkour> parkourList;

	public Stage(Yaml yaml){
		//ファイル名をステージ名として代入する
		name = yaml.name;

		//パルクール名のリストをパルクールのリストに変換し代入する
		parkourList = yaml.getStringList("Parkour names")
							.stream()
							//.map(a -> new Parkour())
							.collect(Collectors.toList());
	}

}
