package amata1219.parkour.user;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;

public class UserSet {

	private final Main plugin = Main.getPlugin();
	private final File folder = new File(plugin.getDataFolder() + File.separator + "Users");

	public final Map<UUID, User> users = new HashMap<>();

	public UserSet(){
		for(File file : folder.listFiles()){
			Yaml yaml = new Yaml(plugin, file);
			User user = new User(yaml);
			users.put(user.uuid, user);
		}
	}

}
