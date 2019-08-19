package amata1219.parkour.function.hotbar;

import org.bukkit.inventory.ItemStack;

import amata1219.parkour.user.User;

public interface FunctionalHotbarItem {

	void onClick(User user, ClickType click);

	ItemStack build(User user);

	boolean isSimilar(ItemStack item);

}
