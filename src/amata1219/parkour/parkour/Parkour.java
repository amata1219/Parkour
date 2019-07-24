package amata1219.parkour.parkour;

import org.bukkit.Bukkit;
import org.bukkit.World;

import amata1219.amalib.yaml.Yaml;

public class Parkour {

	//パルクール名
	public final String name;

	//ワールド
	public final World world;

	//設定
	public final ParkourSetting setting;

	public Parkour(Yaml yaml){
		name = yaml.name;

		world = Bukkit.getWorld(yaml.getString("World"));

		setting = new ParkourSetting(this, yaml);
	}

}
