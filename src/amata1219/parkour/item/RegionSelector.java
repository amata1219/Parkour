package amata1219.parkour.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.inventory.ui.dsl.component.Icon;

public class RegionSelector {

	public static final ItemStack SELECTOR;

	static{
		SELECTOR = new ItemStack(Material.STONE_AXE);

		ItemMeta meta = SELECTOR.getItemMeta();

		meta.setDisplayName("§7$0 §b§l>§r §70,0,0 §b§l-§r §70,0,0 §b§l@§r §7world");
		meta.addEnchant(Icon.GLEAM_ENCHANTMENT, 1, true);

		SELECTOR.setItemMeta(meta);
	}

}
