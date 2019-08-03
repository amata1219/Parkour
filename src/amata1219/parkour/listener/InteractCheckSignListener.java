package amata1219.parkour.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import amata1219.amalib.message.MessageColor;
import amata1219.parkour.Main;
import amata1219.parkour.item.CheckSign;
import amata1219.parkour.user.User;

public class InteractCheckSignListener implements Listener {

	@EventHandler
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

		//タイプが一致しているかつ着地していたらチェックポイントを決める
		if(CheckSign.CP_AT_SIGN.equals(line))
			if(blockFlowingPlayer(player))
				user.creativeWorldCheckpoint = block.getLocation();
		else if(CheckSign.CP_AT_TRACEUR.endsWith(line))
			if(blockFlowingPlayer(player))
				user.creativeWorldCheckpoint = player.getLocation();
	}

	private boolean blockFlowingPlayer(Player player){
		if(player.isOnGround())
			return false;

		MessageColor.color("&c-Operation blocked-&7 @ &c-Must be on the ground").display(player);
		return true;
	}

}
