package amata1219.parkour.ui;

import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.amalib.item.skull.SkullMaker;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.user.User;

public class MenuUI implements InventoryUI {

	/*
	 * 自分のアイコン: ステータス1
	 *
	 * キーコンフィグ5
	 * 情報板コンフィグ6
	 * ヘッド購入7
	 *
	 */

	/*
	 * xoxxxooox
	 */

	private final User user;
	private final InformationBoardOptionsUI informationBoardOptionsUI;

	public MenuUI(User user){
		this.user = user;

		//スコアボードの設定UIを作成する
		informationBoardOptionsUI = new InformationBoardOptionsUI(user);
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Player player = user.asBukkitPlayer();

		String playerName = player.getName();

		return build(InventoryLine.x1, (l) -> {
			//表示例: amata1219's menu
			l.title = StringTemplate.applyWithColor("&b-$0's menu", playerName);

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			//自分のステータス表示
			l.put((s) -> {
				//プレイヤーのスカルヘッドを作成する
				ItemStack skull = SkullMaker.fromPlayerUniqueId(user.uuid);

				s.icon(skull, (i) -> {
					i.displayName = StringTemplate.applyWithColor("&b-$0's state", playerName);



				});

			}, 1);

			//スコアボードの設定
			l.put((s) -> {

				s.onClick((event) -> {
					//スコアボードの設定UIを開く
					informationBoardOptionsUI.openInventory(event.player);
				});

				s.icon(Material.FEATHER, (i) -> {
					//表示名: Scoreboard options
					i.displayName = StringColor.color("&b-Scoreboard options");
				});

			}, 6);

		});
	}

}
