package amata1219.parkour.user;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import amata1219.amalib.schedule.Async;
import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;

public class SaveUserDataTask {

	private static BukkitTask task;
	private static Main plugin;
	private static File folder;

	public static void run(){
		plugin = Main.getPlugin();
		folder = new File(plugin.getDataFolder() + File.separator + "Users");

		//ログインしているプレイヤーのデータを10分毎にセーブする
		task = Async.define(() -> {
			for(Player player : Bukkit.getOnlinePlayers()){
				Yaml yaml = new Yaml(plugin, folder, StringTemplate.format("$0.yml", player.getUniqueId()));
				User user = Main.getUserSet().getUser(player);
				user.save(yaml);
			}
		}).executeTimer(12000, 12000);
	}

	public static void cancel(){
		task.cancel();
		plugin = null;
		folder = null;
	}

	private SaveUserDataTask(){

	}

}
