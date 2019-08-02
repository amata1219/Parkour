package amata1219.parkour.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.amalib.schedule.Sync;
import amata1219.amalib.text.TextColor;
import amata1219.parkour.Main;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;
import amata1219.parkour.user.UserSetting;

public class ToggleHideModeChangeListener implements Listener {

	private final Main plugin = Main.getPlugin();
	private final UserSet userSet = Main.getUserSet();

	//他プレイヤーを非表示にしているプレイヤー
	private final List<User> hideModePlayers = new ArrayList<>();

	//クールダウン中のプレイヤー
	private final List<User> cooldownPlayers = new ArrayList<>();

	@EventHandler
	public void loadHideMode(PlayerJoinEvent event){
		Player player = event.getPlayer();

		//他プレイヤーを非表示にしていなければ戻る
		if(!userSet.getUser(player).setting.hideTraceurs)
			return;

		apply(player);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		for(User user : hideModePlayers)
			user.asBukkitPlayer().hidePlayer(plugin, event.getPlayer());
	}

	@EventHandler
	public void toggleHideModeChange(PlayerDropItemEvent event){
		Player player = event.getPlayer();
		User user = userSet.getUser(player);

		//クールダウン中であれば警告をして戻る
		if(cooldownPlayers.contains(user)){
			TextColor.color("&7-Operation blocked-&c @ &7-Input is too fast").display(player);
			return;
		}

		//ユーザー設定を取得する
		UserSetting setting = user.setting;

		//設定を反転させる
		setting.hideTraceurs = !setting.hideTraceurs;

		//適用する
		apply(player);

		TextColor.color((setting.hideTraceurs ? "&7-Hide" : "&b-Unhide") + " other traceurs").display(player);

		//クールダウンさせる
		cooldownPlayers.add(user);

		//0.5秒後に完了させる
		Sync.define(() -> cooldownPlayers.remove(user)).executeLater(10);
	}

	private void apply(Player player){
		User user = userSet.getUser(player);

		if(user.setting.hideTraceurs){
			for(Player target : Bukkit.getOnlinePlayers())
				player.hidePlayer(plugin, target);

			hideModePlayers.add(user);
		}else{
			for(Player target : Bukkit.getOnlinePlayers())
				player.showPlayer(plugin, target);

			hideModePlayers.remove(user);
		}
	}

}
