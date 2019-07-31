package amata1219.parkour.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.inventory.ui.dsl.component.Icon;

public class CheckSign {

	public static final ItemStack AT_SIGN;
	public static final ItemStack AT_PLAYER;

	public static final String CP_AT_SIGN = "§b§lCP @ SIGN";
	public static final String CP_AT_PLAYER = "§b§lCP @ PLAYER";

	static{
		AT_SIGN = new ItemStack(Material.SIGN);

		ItemMeta meta = AT_SIGN.getItemMeta();
		meta.setDisplayName(CP_AT_SIGN);
		meta.addEnchant(Icon.GLEAM_ENCHANTMENT, 1, true);

		AT_SIGN.setItemMeta(meta);
	}

	static{
		AT_PLAYER = new ItemStack(Material.SIGN);

		ItemMeta meta = AT_PLAYER.getItemMeta();
		meta.setDisplayName(CP_AT_PLAYER);
		meta.addEnchant(Icon.GLEAM_ENCHANTMENT, 1, true);

		AT_PLAYER.setItemMeta(meta);
	}

}
