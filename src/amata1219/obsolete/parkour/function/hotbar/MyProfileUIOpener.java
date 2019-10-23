package amata1219.obsolete.parkour.function.hotbar;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.obsolete.parkour.item.Skull;
import amata1219.obsolete.parkour.text.BilingualText;
import amata1219.obsolete.parkour.user.User;

public class MyProfileUIOpener implements FunctionalItem {

	@Override
	public void onClick(User user, ClickType click) {
		user.inventoryUserInterfaces.openMyProfileUI();
	}

	@Override
	public ItemStack build(User user) {
		//ユーザーのUUIDに基づきスカルヘッドを作成する
		ItemStack item = Skull.createFrom(user.uuid);
		ItemMeta meta = item.getItemMeta();

		String displayName = BilingualText.stream("&b-プロフィールを開く", "&b-Open My Profile")
				.textBy(user.asBukkitPlayer())
				.color()
				.toString();

		meta.setDisplayName(displayName);
		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.PLAYER_HEAD;
	}

}
