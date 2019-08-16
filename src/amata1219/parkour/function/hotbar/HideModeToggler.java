package amata1219.parkour.function.hotbar;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.string.StringLocalize;
import amata1219.parkour.function.ToggleHideMode;
import amata1219.parkour.user.User;

public class HideModeToggler implements FunctionalHotbarItem {

	@Override
	public void onClick(User user, ClickType click) {
		ToggleHideMode.getInstance().change(user);
	}

	@Override
	public ItemStack build(User user) {
		//ユーザーに対応したプレイヤーを取得する
		Player player = user.asBukkitPlayer();

		//非表示モードかどうか
		boolean hideMode = user.setting.hideMode;

		ItemStack item = new ItemStack(hideMode ? Material.GLASS : Material.BEACON);

		ItemMeta meta = item.getItemMeta();

		//使用言語に対応したテキストを表示名に設定する
		meta.setDisplayName(StringLocalize.capply(hideMode ? "&b-プレイヤーを表示する | &b-?" : "&b-プレイヤーを非表示にする | &b-?", player));

		//使用言語に対応したテキストを説明文に設定する
		meta.setLore(Arrays.asList(
			StringLocalize.capply("&7-説明文いらない。 | &7-?", player)
		));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		if(item == null) return false;

		Material type = item.getType();

		return type == Material.GLASS || type == Material.BEACON;
	}

}
