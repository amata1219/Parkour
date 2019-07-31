package amata1219.parkour.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import amata1219.parkour.Main;
import amata1219.parkour.user.User;

public class InteractCheckSignListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event){
		//ブロックを右クリックしていなければ戻る
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		Block block = event.getClickedBlock();
		Material blockType = block.getType();

		//看板でなければ戻る
		if(blockType != Material.SIGN && blockType != Material.WALL_SIGN)
			return;

		Player player = event.getPlayer();
		User user = Main.getUserSet().users.get(player.getUniqueId());
		Sign sign = (Sign) block.getState();
		String line = sign.getLine(1);

		if(Main.CP_AT_SIGN.equals(line))
			user.checkpoint = block.getLocation().clone();
		else if(Main.CP_AT_PLAYER.endsWith(line))
			user.checkpoint = player.getLocation().clone();
	}

}
