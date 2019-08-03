package amata1219.parkour.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.parkour.Main;
import amata1219.parkour.function.ToggleHideModeChange;
import amata1219.parkour.user.User;

public class SelectFunctionListener implements Listener {

	/*
	 * @最終CPへTP
	 * @ステージ内の最終CP一覧 > アスレ内CP一覧
	 * @アスレ間移動
	 * @HideMode
	 *
	 * @Menu >
	 *   MyStates
	 * @Setting >
	 *   Scoreboard
	 *   Key
	 *   Skull
	 *
	 *   default slot > light gray glass
	 *
	 */

	@EventHandler
	public void onSlotsInitialize(PlayerJoinEvent event){

	}

	@EventHandler
	public void onFunctionSelect(PlayerItemHeldEvent event){
		Player player = event.getPlayer();

		//ユーザーを取得する
		User user = Main.getUserSet().getUser(player);

		//念の為の修正処理
		if(event.getPreviousSlot() != 0) player.getInventory().setHeldItemSlot(0);

		switch(event.getNewSlot()){
		case 1:
			break;
		case 2:
			break;
		case 3:
			//Last CP
			break;
		case 4:
			//All CP
			break;
		case 5:
			//Parkour TP
			break;
		case 6:
			//Menu
			break;
		case 7:
			//Setting
			break;
		case 8:
			ToggleHideModeChange.getInstance().change(user);

			//ガラス板を変える

			break;
		default:
			return;
		}

		event.setCancelled(true);
	}

}
