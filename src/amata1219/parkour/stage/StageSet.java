package amata1219.parkour.stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;

public class StageSet {

	private final Main plugin = Main.getPlugin();
	public final File folder = new File(plugin.getDataFolder() + File.separator + "Stages");

	public final Map<String, Stage> stages = new HashMap<>();

	//アスレ名をキーにそれに対応したステージを保持するマップ
	public final Map<String, Stage> parkourNamesToStagesMap = new HashMap<>();

	public StageSet(){
		if(!folder.exists())
			folder.mkdir();

		for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[]{})){
			Yaml yaml = new Yaml(plugin, file);

			//ファイルを基にステージを作成する
			Stage stage = new Stage(yaml);

			//パルクールとステージを結び付けて保持する
			for(Parkour parkour : stage.parkourList)
				parkourNamesToStagesMap.put(parkour.name, stage);

			stages.put(yaml.name, stage);
		}
	}

}
