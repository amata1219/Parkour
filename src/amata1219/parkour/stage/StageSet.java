package amata1219.parkour.stage;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;

public class StageSet {

	public final Map<String, Stage> stages = new HashMap<>();

	public StageSet(Yaml yaml){
		if(!yaml.isConfigurationSection("Stages"))
			return;

		ConfigurationSection section = yaml.getConfigurationSection("Stages");
		for(String name : section.getKeys(false)){
			Stage stage = stages.put(name, new Stage(name));
			section.getStringList(name)
					.stream()
					.map(Main.getParkourSet().parkourMap::get)
					.forEach(stage.parkourList::add);
		}
	}

}
