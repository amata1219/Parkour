package amata1219.parkour.function.hotbar;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.string.StringLocalize;
import amata1219.amalib.string.message.MessageColor;
import amata1219.parkour.user.User;

public class CheckpointsMenuOpener implements FunctionalHotbarItem {

	@Override
	public void onClick(User user, ClickType click) {
		Player player = user.asBukkitPlayer();

		//どこのアスレにもいなければ戻る
		if(user.currentParkour == null){
			MessageColor.color("&c-Operation blocked &7-@ &c-You are not on any parkour").displayOnActionBar(player);
			return;
		}

		//右クリックしたのであれば最終、左クリックしたのであれば最新のチェックポイントリストを表示する
		InventoryUI inventoryUI = click == ClickType.RIGHT ? user.inventoryUserInterfaces.lastCheckpointSelectionUI : user.inventoryUserInterfaces.latestCheckpointSelectionUI;
		inventoryUI.openInventory(player);
	}

	@Override
	public ItemStack build(User user, boolean flag) {
		//ユーザーに対応したプレイヤーを取得する
		Player player = user.asBukkitPlayer();

		ItemStack item = new ItemStack(Material.CYAN_DYE);

		ItemMeta meta = item.getItemMeta();

		//使用言語に対応したテキストを表示名に設定する
		meta.setDisplayName(StringLocalize.capply("&b-最新/最終チェックポイント一覧を開く | &b-Open Latest/Last Checkpoints Menu", player));

		//使用言語に対応したテキストを説明文に設定する
		meta.setLore(Arrays.asList(
			StringLocalize.capply("&7-左クリックすると最新チェックポイント一覧を開きます。 | &7-?", player),
			StringLocalize.capply("&7-右クリックすると最終チェックポイント一覧を開きます。 | &7-?", player)
		));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.CYAN_DYE;
	}

}
