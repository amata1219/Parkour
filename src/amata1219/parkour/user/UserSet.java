package amata1219.parkour.user;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;

import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;

public class UserSet {

	private final Main plugin = Main.getPlugin();
	private final File folder = new File(plugin.getDataFolder() + File.separator + "Users");

	public final Map<UUID, User> users = new HashMap<>();

	public UserSet(){
		if(!folder.exists())
			folder.mkdir();

		for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[]{})){
			Yaml yaml = new Yaml(plugin, file);
			User user = new User(yaml);
			users.put(user.uuid, user);
		}
	}

	public void registerNewUser(UUID uuid){
		Yaml yaml = new Yaml(plugin, new File(folder, StringTemplate.format("$0.yml", uuid)));
		User user = new User(yaml);
		users.put(uuid, user);
	}

	public User getUser(Player player){
		return users.get(player.getUniqueId());
	}

}
