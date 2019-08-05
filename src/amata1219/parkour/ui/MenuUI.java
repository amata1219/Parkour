package amata1219.parkour.ui;

import java.util.function.Function;

import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.user.User;

public class MenuUI implements InventoryUI {

	/*
	 * 自分のアイコン: ステータス
	 *
	 * キーコンフィグ
	 * 情報板コンフィグ
	 * ヘッド購入
	 *
	 */

	private final User user;

	public MenuUI(User user){
		this.user = user;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Player player = user.asBukkitPlayer();

		String playerName = player.getName();

		return build(InventoryLine.x1, (l) -> {
			l.title = StringTemplate.applyWithColor("&b-$0's menu", playerName);
		});
	}

}
