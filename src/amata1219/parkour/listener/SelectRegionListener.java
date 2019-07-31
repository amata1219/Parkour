package amata1219.parkour.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.text.StringTemplate;
import amata1219.parkour.Main;
import amata1219.parkour.message.Messenger;
import amata1219.parkour.user.ParkourRegionSelector;

public class SelectRegionListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Action action = event.getAction();

		//ブロックをクリックしていなければ戻る
		if(action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK)
			return;

		//アイテムを持っていなければ戻る
		if(!event.hasItem())
			return;

		ItemStack item = event.getItem();

		//範囲選択ツールでなければ戻る
		if(item.getType() != Material.STONE_AXE || !item.containsEnchantment(Icon.GLEAM_ENCHANTMENT))
			return;

		Player player = event.getPlayer();
		ParkourRegionSelector selector = Main.getUserSet().users.get(player.getUniqueId()).selector;
		if(selector == null){
			Messenger.sendEmbeddedCommandMessage(player, "§c: Invalid Action error > 範囲選択が無効化されています。このテキストをクリックして有効化して下さい。", StringTemplate.format("/regionselector $0", ChatColor.stripColor(item.getItemMeta().getDisplayName()).split(" > ")[0]));
			return;
		}

		Location location = event.getClickedBlock().getLocation();
		ImmutableBlockLocation blockLocation = new ImmutableBlockLocation(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

		RegionSelection selection = selector.selection;

		//座標を更新する
		if(action == Action.LEFT_CLICK_BLOCK)
			selection.setBoundaryCorner1(blockLocation);
		else
			selection.setBoundaryCorner2(blockLocation);
	}

}
