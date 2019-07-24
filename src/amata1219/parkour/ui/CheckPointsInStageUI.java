package amata1219.parkour.ui;

import java.util.List;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;

public class CheckPointsInStageUI implements InventoryUI {

	/*
	 * 使用したいアイテム
	 *
	 * ・埋もれし宝の地図
	 *
	 */

	private final User user;

	public CheckPointsInStageUI(User user){
		this.user = user;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		return build(InventoryLine.necessaryInventoryLine(user.currentlyPlayingParkour.getStage().parkourList.size()), (l) -> {
			//今いるステージのパルクールリストを取得する
			List<Parkour> parkourList = user.currentlyPlayingParkour.getStage().parkourList;

			//各パルクール毎に処理をする
			for(int slotIndex = 0; slotIndex < parkourList.size(); slotIndex++){
				Parkour parkour = parkourList.get(slotIndex);
				l.put((s) -> {
					s.icon(Material.GRASS_BLOCK, (i) -> {

					});
				}, slotIndex);
			}
		});
	}

}
