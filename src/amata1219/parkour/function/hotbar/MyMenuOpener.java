package amata1219.parkour.function.hotbar;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.string.message.Localizer;
import amata1219.amalib.util.SkullMaker;
import amata1219.parkour.user.User;

public class MyMenuOpener implements FunctionalHotbarItem {

	@Override
	public void onClick(User user, ClickType click) {
		user.inventoryUserInterfaces.openMyProfileUI();
	}

	@Override
	public ItemStack build(User user) {
		Localizer localizer = user.localizer;

		//ユーザーのUUIDに基づきスカルヘッドを作成する
		ItemStack item = SkullMaker.fromPlayerUniqueId(user.uuid);
		ItemMeta meta = item.getItemMeta();

		//使用言語に対応したテキストを表示名に設定する
		meta.setDisplayName(localizer.color("&b-プロフィールを開く | &b-Open My Profile"));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.PLAYER_HEAD;
	}

}
