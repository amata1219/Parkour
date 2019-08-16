package amata1219.parkour.function.hotbar;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.string.StringLocalize;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.ui.parkour.ParkoursMenus;
import amata1219.parkour.user.User;

public class ParkoursMenuOpener implements FunctionalHotbarItem {

	@Override
	public void onClick(User user, ClickType click) {
		//プレイヤーが今いるアスレを取得する
		Parkour parkour = user.currentParkour;

		//ステージのカテゴリーを取得する
		ParkourCategory category = parkour != null ? parkour.category : ParkourCategory.NORMAL;

		//カテゴリーに対応したステージリストを開かせる
		InventoryUI inventoryUI = null;

		//カテゴリーに対応したアスレリストを取得する
		switch(category){
		case UPDATE:
			inventoryUI = user.inventoryUserInterfaces.updateParkourSelectionUI;
			break;
		case EXTEND:
			inventoryUI = user.inventoryUserInterfaces.extendParkourSelectionUI;
			break;
		default:
			inventoryUI = ParkoursMenus.getInstance().getInventoryUI(category);
			break;
		}

		inventoryUI.openInventory(user.asBukkitPlayer());
	}

	@Override
	public ItemStack build(User user, boolean flag) {
		//ユーザーに対応したプレイヤーを取得する
		Player player = user.asBukkitPlayer();

		ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);

		ItemMeta meta = item.getItemMeta();

		//使用言語に対応したテキストを表示名に設定する
		meta.setDisplayName(StringLocalize.capply("&b-アスレチック一覧を開く | &b-Open Parkours Menu", player));

		//使用言語に対応したテキストを説明文に設定する
		meta.setLore(Arrays.asList(
			StringLocalize.capply("&7-クリックするとアスレチック一覧を開きます。 | &7-?", player)
		));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.HEART_OF_THE_SEA;
	}

}
