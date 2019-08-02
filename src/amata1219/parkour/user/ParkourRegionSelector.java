package amata1219.parkour.user;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.parkour.Parkour;

public class ParkourRegionSelector {

	public final Parkour parkour;
	public final RegionSelection selection = new RegionSelection();

	public ParkourRegionSelector(Parkour parkour){
		this.parkour = parkour;
	}

	public void updateDisplayName(ItemStack item){
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

}
