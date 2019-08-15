package amata1219.parkour.function;

import java.util.function.BiConsumer;

import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import amata1219.parkour.user.User;

public class HotbarFunctionalItem {

	public final ItemStack item;
	public final BiConsumer<User, Action> onClick;

	public HotbarFunctionalItem(ItemStack item, BiConsumer<User, Action> onClick){
		this.item = item;
		this.onClick = onClick;
	}

	public boolean equals(ItemStack item){
		if(item == null || !item.hasItemMeta()) return false;

		return this.item.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName());
	}

}
