package amata1219.parkour.function.hotbar;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.string.message.Localizer;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.user.User;

public class ParkoursMenuOpener implements FunctionalHotbarItem {

	@Override
	public void onClick(User user, ClickType click) {
		//プレイヤーが今いるアスレを取得する
		Parkour parkour = user.currentParkour;

		//ステージのカテゴリーを取得する
		ParkourCategory category = parkour != null ? parkour.category : ParkourCategory.NORMAL;

		user.inventoryUserInterfaces.openParkourSelectionUI(category);
	}

	@Override
	public ItemStack build(User user) {
		ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);

		ItemMeta meta = item.getItemMeta();

		Localizer localizer = user.localizer;

		meta.setDisplayName(localizer.color("&b-アスレチック一覧を開く | &b-Open Parkours Menu"));
		meta.setLore(Arrays.asList(localizer.color("&7-クリックするとアスレチック一覧を開きます。 | &7-?")));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.HEART_OF_THE_SEA;
	}

}
