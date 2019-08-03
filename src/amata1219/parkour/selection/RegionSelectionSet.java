package amata1219.parkour.selection;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.tuplet.Tuple;
import amata1219.parkour.parkour.Parkour;

public class RegionSelectionSet implements Listener {

	private static RegionSelectionSet instance;

	public static RegionSelectionSet getInstance(){
		return instance != null ? instance : (instance = new RegionSelectionSet());
	}

	public final HashMap<UUID, Tuple<Parkour, RegionSelection>> selections = new HashMap<>();

	private RegionSelectionSet(){

	}

	@EventHandler
	public void onSelectionClear(PlayerQuitEvent event){
		selections.remove(event.getPlayer());
	}

	/*
	 * public void updateDisplayName(ItemStack item){
		ItemMeta meta = item.getItemMeta();

		ImmutableBlockLocation lesserBoundaryCorner = selection.getLesserBoundaryCorner();
		ImmutableBlockLocation greaterBoundaryCorner = selection.getGreaterBoundaryCorner();
		World world = lesserBoundaryCorner.getWorld();

		meta.setDisplayName(StringTemplate.apply("$0$4 $1$2>$3 $0$5,$6,$7 $1$2-$3 $0$8,$9,$10 $1$2@$3 $0$11",
			ChatColor.GRAY, ChatColor.AQUA, ChatColor.BOLD, ChatColor.RESET, parkour.name,
			lesserBoundaryCorner.x, lesserBoundaryCorner.y, lesserBoundaryCorner.z,
			greaterBoundaryCorner.x, greaterBoundaryCorner.y, greaterBoundaryCorner.z, world != null ? world.getName() : "null"));

		item.setItemMeta(meta);
	}
	 */

}
