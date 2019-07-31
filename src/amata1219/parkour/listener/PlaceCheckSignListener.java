package amata1219.parkour.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import amata1219.parkour.Main;

public class PlaceCheckSignListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent event){
		Block block = event.getBlock();
		Material blockType = block.getType();

		//看板でなければ戻る
		if(blockType != Material.SIGN && blockType != Material.WALL_SIGN)
			return;

		//クリエイティブワールドでなければ戻る
		if(!block.getWorld().equals(Main.getCreativeWorld()))
			return;

		Sign state = (Sign) block.getState();
		ItemStack item = event.getItemInHand();

		if(Main.AT_SIGN.equals(item))
			state.setLine(1, Main.CP_AT_SIGN);
		else if(Main.AT_PLAYER.equals(item))
			state.setLine(1, Main.CP_AT_PLAYER);
		else
			return;

		state.update();
	}

}
