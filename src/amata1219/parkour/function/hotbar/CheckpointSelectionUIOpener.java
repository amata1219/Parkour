package amata1219.parkour.function.hotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.parkour.text.BilingualText;
import amata1219.parkour.text.TextStream.MessageType;
import amata1219.parkour.user.InventoryUISet;
import amata1219.parkour.user.User;

public class CheckpointSelectionUIOpener implements FunctionalItem {

	@Override
	public void onClick(User user, ClickType click) {
		Player player = user.asBukkitPlayer();

		//どこのアスレにもいなければ戻る
		if(!user.isOnCurrentParkour()){
			BilingualText.stream("&c-パルクール中でないためチェックポイントの選択画面を開けません！",
					"&c-You can't open checkpoint selection UI because you aren't playing parkour now!")
					.color()
					.sendTo(player, MessageType.ACTION_BAR);
			return;
		}

		InventoryUISet inventoryUserInterfaces = user.inventoryUserInterfaces;

		//右クリックしたのであれば最終、左クリックしたのであれば最新のチェックポイントリストを表示する
		if(click == ClickType.RIGHT) inventoryUserInterfaces.openLastCheckpointSelectionUI();
		else if(click == ClickType.LEFT) inventoryUserInterfaces.openLatestCheckpointSelectionUI();
	}

	@Override
	public ItemStack build(User user) {
		ItemStack item = new ItemStack(Material.CYAN_DYE);

		ItemMeta meta = item.getItemMeta();

		String displayName = BilingualText.stream("&b-最新チェックポイント一覧 &7-@ 左クリック / &b-最終チェックポイント一覧 &7-@右クリック",
				"&b-Latest Checkpoints &7-@ Left Click / &b-Last Checkpoints &7-@ Right Click")
				.localize(user.asBukkitPlayer())
				.color()
				.toString();


		meta.setDisplayName(displayName);

		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.CYAN_DYE;
	}

}
