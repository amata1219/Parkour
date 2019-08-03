package amata1219.parkour.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

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
	 */

	@EventHandler
	public void onFunctionSelect(PlayerItemHeldEvent event){
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
			//HideMode
			break;
		default:
			return;
		}

		event.setCancelled(true);
	}

}
