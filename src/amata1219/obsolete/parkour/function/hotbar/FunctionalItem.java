package amata1219.obsolete.parkour.function.hotbar;

import org.bukkit.inventory.ItemStack;

import amata1219.obsolete.parkour.user.User;

public interface FunctionalItem {

	void onClick(User user, ClickType click);

	ItemStack build(User user);

	boolean isSimilar(ItemStack item);

}
