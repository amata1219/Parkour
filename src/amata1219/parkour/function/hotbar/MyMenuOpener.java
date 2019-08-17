package amata1219.parkour.function.hotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.string.StringColor;
import amata1219.amalib.util.SkullMaker;
import amata1219.parkour.user.User;

public class MyMenuOpener implements FunctionalHotbarItem {

	@Override
	public void onClick(User user, ClickType click) {
		user.inventoryUIs.openMyProfileUI();
	}

	@Override
	public ItemStack build(User user, boolean flag) {
		//ユーザーに対応したプレイヤーを取得する
		Player player = user.asBukkitPlayer();

		//ユーザーのUUIDに基づきスカルヘッドを作成する
		ItemStack item = SkullMaker.fromPlayerUniqueId(user.uuid);

		ItemMeta meta = item.getItemMeta();

		//使用言語に対応したテキストを表示名に設定する
		meta.setDisplayName(StringColor.lcolor("&b-プロフィールを開く | &b-Open My Profile", player));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.PLAYER_HEAD;
	}

}
