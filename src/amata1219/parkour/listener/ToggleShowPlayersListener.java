package amata1219.parkour.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import amata1219.parkour.Main;
import amata1219.parkour.message.Messenger;
import amata1219.parkour.user.UserSetting;

public class ToggleShowPlayersListener implements Listener {

	private final Main plugin = Main.getPlugin();
	private final List<UUID> duringCooldownPlayers = new ArrayList<>();

	@EventHandler
	public void onDrop(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		//クールダウン中なら戻る
		if(duringCooldownPlayers.contains(uuid))
			return;

		UserSetting setting = Main.getUserSet().users.get(uuid).setting;

		setting.hideUsers = !setting.hideUsers;

		//プレイヤーを非表示にする場合
		if(setting.hideUsers){
			for(Player target : Bukkit.getOnlinePlayers())
				player.hidePlayer(plugin, target);

			Messenger.sendActionBarMessage(player, ChatColor.GRAY + "Hide Players");
		//プレイヤーを表示する場合
		}else{
			for(Player target : Bukkit.getOnlinePlayers())
				player.showPlayer(plugin, target);

			Messenger.sendActionBarMessage(player, ChatColor.AQUA + "Show Players");
		}

		//クールダウンさせる
		duringCooldownPlayers.add(uuid);

		//0.5秒でクールダウンは完了する
		new BukkitRunnable(){

			@Override
			public void run() {
				duringCooldownPlayers.remove(uuid);
			}

		}.runTaskLater(plugin, 10);
	}

}
