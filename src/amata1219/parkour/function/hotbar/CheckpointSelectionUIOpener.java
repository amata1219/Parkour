package amata1219.parkour.function.hotbar;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.string.message.Localizer;
import amata1219.parkour.user.InventoryUserInterfaces;
import amata1219.parkour.user.User;

public class CheckpointSelectionUIOpener implements FunctionalItem {

	@Override
	public void onClick(User user, ClickType click) {
		Player player = user.asBukkitPlayer();

		//どこのアスレにもいなければ戻る
		if(!user.parkourWithNow().isPresent()){
			user.localizer.mcolor("&c-パルクール中でないためチェックポイントの選択画面を開けません | &c-You can't open checkpoint selection UI because you aren't playing parkour now").displayOnActionBar(player);
			return;
		}

		InventoryUserInterfaces inventoryUserInterfaces = user.inventoryUserInterfaces;

		//右クリックしたのであれば最終、左クリックしたのであれば最新のチェックポイントリストを表示する
		if(click == ClickType.RIGHT) inventoryUserInterfaces.openLastCheckpointSelectionUI();
		else if(click == ClickType.LEFT) inventoryUserInterfaces.openLatestCheckpointSelectionUI();
	}

	@Override
	public ItemStack build(User user) {
		Localizer localizer = user.localizer;

		ItemStack item = new ItemStack(Material.CYAN_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(localizer.color("&b-最新/最終チェックポイント一覧を開く | &b-Open Latest/Last Checkpoint List"));
		meta.setLore(Arrays.asList(localizer.color("&7-左クリックで最新、右クリックで最終チェックポイントの一覧を開きます。 | &7-Left-click to open the latest checkpoint and right-click to open the last checkpoint list.")));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.CYAN_DYE;
	}

}
