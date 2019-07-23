package amata1219.parkour.stage;

import java.util.HashMap;
import java.util.Map;

import amata1219.amalib.yaml.Yaml;

public class StageSet {

	public final Map<String, Stage> stages = new HashMap<>();

	public StageSet(Yaml yaml){
		for(String name : yaml.getStringList("Stages"))
			stages.put(name, new Stage(name));
	}

}
