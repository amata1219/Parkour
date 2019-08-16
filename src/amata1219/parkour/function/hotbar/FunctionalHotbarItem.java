package amata1219.parkour.function.hotbar;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public interface FunctionalHotbarItem {

	void onClick(User user, ClickType click);

	ItemStack build(User user);

	default ItemStack build(Player player){
		return build(Users.getInstnace().getUser(player));
	}

	boolean isSimilar(ItemStack item);

}
