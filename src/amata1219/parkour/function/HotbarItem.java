package amata1219.parkour.function;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.parkour.user.User;

public class HotbarItem {

	public final Material material;
	public final String displayNameTemplate;
	public final BiConsumer<User, ClickType> onClick;
	public final List<String> lore;

	public HotbarItem(Material material, String displayNameTemplate, BiConsumer<User, ClickType> onClick, String... lore){
		this.material = material;
		this.displayNameTemplate = displayNameTemplate;
		this.onClick = onClick;
		this.lore = Arrays.asList(lore);
	}

	public boolean equals(ItemStack item){
		if(item == null || item.getType() != material || !item.hasItemMeta()) return false;

		ItemMeta meta = item.getItemMeta();

		//表示名や説明文が無ければ戻る
		if(!meta.hasDisplayName() || !meta.hasLore()) return false;

		return meta.getLore().equals(lore);
	}

	public static enum ClickType {

		RIGHT,
		LEFT;

	}

}
