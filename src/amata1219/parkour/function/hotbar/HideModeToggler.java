package amata1219.parkour.function.hotbar;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.string.message.Localizer;
import amata1219.parkour.function.ToggleHideMode;
import amata1219.parkour.user.User;

public class HideModeToggler implements FunctionalHotbarItem {

	@Override
	public void onClick(User user, ClickType click) {
		ToggleHideMode.getInstance().change(user);
		ControlFunctionalHotbarItem.updateSlot(user.asBukkitPlayer(), 6);
	}

	@Override
	public ItemStack build(User user) {
		Localizer localizer = user.localizer;

		//非表示モードかどうか
				boolean hideMode = user.setting.hideMode;

		ItemStack item = new ItemStack(hideMode ? Material.GLASS : Material.BEACON);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(localizer.color(hideMode ? "&b-プレイヤーを表示する | &b-?" : "&b-プレイヤーを非表示にする | &b-?"));
		meta.setLore(Arrays.asList(localizer.color("&7-説明文いらない。 | &7-?")));

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
