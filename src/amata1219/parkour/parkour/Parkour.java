package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;

import amata1219.amalib.region.Region;
import amata1219.amalib.text.StringSplit;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.stage.Stage;

public class Parkour {

	//アスレ名
	public final String name;

	//ワールド
	public final World world;

	//アスレの領域
	public final Region region;

	//スタートライン
	public final GraphicalRegion startLine;

	//ゴールライン
	public final GraphicalRegion goalLine;

	//チェックエリアのリスト
	public final List<GraphicalRegion> checkAreas = new ArrayList<>();

	public Parkour(Yaml yaml){
		name = yaml.name;

		world = Bukkit.getWorld(yaml.getString("World"));

		//領域を作成する
		region = Region.fromString(world, yaml.getString("Region"));

		//テキストをカンマ毎に分割しそれぞれを数値に変換する
		int[] colors = StringSplit.splitToIntArguments(yaml.getString("Color"));

		//カラーを作成する
		Color color = Color.fromRGB(colors[0], colors[1], colors[2]);

		//スタートラインを作成する
		startLine = GraphicalRegion.fromString(this, color, yaml.getString("Start line"));

		//ゴールラインを作成する
		goalLine = GraphicalRegion.fromString(this, color, yaml.getString("Goal line"));

		//各チェックエリアを作成してリストに詰め込む
		for(String text : yaml.getStringList("Check areas"))
			checkAreas.add(GraphicalRegion.fromString(this, color, text));
	}

	//このマップがあるステージを返す
	public Stage getStage(){
		return Main.getStageSet().parkourNamesToStagesMap.get(name);
	}

}
