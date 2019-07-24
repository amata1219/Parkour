package amata1219.parkour.stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;

public class StageSet {

	private final Main plugin = Main.getPlugin();
	private final File folder = new File(plugin.getDataFolder() + File.separator + "Stages");

	public final Map<String, Stage> stages = new HashMap<>();

	public final Map<String, Stage> parkourNamesToStagesMap = new HashMap<>();

	public StageSet(){
		for(File file : folder.listFiles()){
			Yaml yaml = new Yaml(plugin, file);
			Stage stage = new Stage(yaml);

			//パルクールとステージを結び付けて保持する
			for(Parkour parkour : stage.parkourList)
				parkourNamesToStagesMap.put(parkour.name, stage);

			stages.put(yaml.name, stage);
		}
	}

}
