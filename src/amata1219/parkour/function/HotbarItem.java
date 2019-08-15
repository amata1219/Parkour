package amata1219.parkour.function;

import java.util.function.BiConsumer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import amata1219.parkour.user.User;

public class HotbarItem {

	public final Material material;
	public final BiConsumer<ItemStack, User> build;
	public final BiConsumer<User, ClickType> onClick;

	public HotbarItem(Material material, BiConsumer<ItemStack, User> build, BiConsumer<User, ClickType> onClick){
		this.material = material;
		this.build = build;
		this.onClick = onClick;
	}

	public boolean equals(ItemStack item){
		return item != null && item.getType() == material;
	}

	public static enum ClickType {

		RIGHT,
		LEFT;

	}

}
