package amata1219.parkour.parkour;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;

public class ParkourSet {

	private final Main plugin = Main.getPlugin();
	private final File folder = new File(plugin.getDataFolder() + File.separator + "ParkourList");

	public final Map<String, Parkour> parkourMap = new HashMap<>();

	public ParkourSet(){
		for(File file : folder.listFiles()){
			Yaml yaml = new Yaml(plugin, file);
			Parkour parkour = new Parkour(yaml);
			parkourMap.put(yaml.name, parkour);
		}
	}

}
