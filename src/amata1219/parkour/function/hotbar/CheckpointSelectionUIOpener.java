package amata1219.parkour.function.hotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.user.InventoryUISet;
import amata1219.parkour.user.User;

public class CheckpointSelectionUIOpener implements FunctionalItem {

	@Override
	public void onClick(User user, ClickType click) {
		Player player = user.asBukkitPlayer();

		//どこのアスレにもいなければ戻る
		if(!user.isOnCurrentParkour()){
			user.localizer.mcolor("&c-パルクール中でないためチェックポイントの選択画面を開けません | &c-You can't open checkpoint selection UI because you aren't playing parkour now").displayOnActionBar(player);
			return;
		}

		InventoryUISet inventoryUserInterfaces = user.inventoryUserInterfaces;

		//右クリックしたのであれば最終、左クリックしたのであれば最新のチェックポイントリストを表示する
		if(click == ClickType.RIGHT) inventoryUserInterfaces.openLastCheckpointSelectionUI();
		else if(click == ClickType.LEFT) inventoryUserInterfaces.openLatestCheckpointSelectionUI();
	}

	@Override
	public ItemStack build(User user) {
		Localizer localizer = user.localizer;

		ItemStack item = new ItemStack(Material.CYAN_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(localizer.color("&b-最新チェックポイントの選択画面を開く &7-@ 左クリック &8-/ &b-最終チェックポイントの選択画面を開く &7-@ 右クリック | &b-Open Latest Checkpoint Selection UI &7-@ Left Click &8-/ &b-Open Last Checkpoint Selection UI &7-@ Right Click"));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.CYAN_DYE;
	}

}
