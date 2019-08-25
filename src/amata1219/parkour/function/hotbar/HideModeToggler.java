package amata1219.parkour.function.hotbar;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.parkour.function.ToggleHideMode;
import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.user.User;

public class HideModeToggler implements FunctionalItem {

	@Override
	public void onClick(User user, ClickType click) {
		ToggleHideMode.getInstance().change(user);
		ControlFunctionalItem.updateSlot(user.asBukkitPlayer(), ItemType.HIDE_MODE_TOGGLER);
	}

	@Override
	public ItemStack build(User user) {
		Localizer localizer = user.localizer;

		//非表示モードかどうか
				boolean hideMode = user.setting.hideMode;

		ItemStack item = new ItemStack(hideMode ? Material.GLASS : Material.BEACON);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(localizer.color(hideMode ? "&b-プレイヤーを表示する | &b-?" : "&b-プレイヤーを非表示にする | &b-?"));

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
