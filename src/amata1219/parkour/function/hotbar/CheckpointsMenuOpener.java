package amata1219.parkour.function.hotbar;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.string.message.Localizer;
import amata1219.amalib.string.message.MessageColor;
import amata1219.parkour.user.InventoryUserInterfaces;
import amata1219.parkour.user.User;

public class CheckpointsMenuOpener implements FunctionalHotbarItem {

	@Override
	public void onClick(User user, ClickType click) {
		Player player = user.asBukkitPlayer();

		//どこのアスレにもいなければ戻る
		if(user.currentParkour == null){
			MessageColor.color("アスレチックのプレイ中でないため開けません").displayOnActionBar(player);
			return;
		}

		InventoryUserInterfaces inventoryUIs = user.inventoryUserInterfaces;

		//右クリックしたのであれば最終、左クリックしたのであれば最新のチェックポイントリストを表示する
		if(click == ClickType.RIGHT) inventoryUIs.openLastCheckpointSelectionUI();
		else if(click == ClickType.LEFT) inventoryUIs.openLatestCheckpointSelectionUI();
	}

	@Override
	public ItemStack build(User user) {
		Localizer localizer = user.localizer;

		ItemStack item = new ItemStack(Material.CYAN_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(localizer.color("&b-最新/最終チェックポイント一覧を開く | &b-?"));
		meta.setLore(Arrays.asList(localizer.color("&7-左クリックで最新、右クリックで最終チェックポイントの一覧を開きます。 | &7-?")));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.CYAN_DYE;
	}

}
