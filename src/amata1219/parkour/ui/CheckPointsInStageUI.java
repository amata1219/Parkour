package amata1219.parkour.ui;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.amalib.text.StringTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class CheckPointsInStageUI implements InventoryUI {

	private final User user;

	public CheckPointsInStageUI(User user){
		this.user = user;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		return build(InventoryLine.necessaryInventoryLine(user.currentlyPlayingParkour.getStage().parkourList.size()), (l) -> {
			//今いるステージのパルクールリストを取得する
			List<Parkour> parkourList = user.currentlyPlayingParkour.getStage().parkourList;

			//ユーザーのチェックポイントマップを取得する
			Map<String, List<Location>> points = user.checkPoints;

			//各アスレ毎に処理をする
			for(int slotIndex = 0; slotIndex < parkourList.size(); slotIndex++){
				//インデックスに対応したアスレを取得する
				Parkour parkour = parkourList.get(slotIndex);
				String parkourName = parkour.name;

				//アスレに対応したチェックポイントが存在しなければコンティニュー
				if(!points.containsKey(parkourName))
					continue;

				//チェックポイントのリストを取得する
				List<Location> locations = points.get(parkourName);

				//リストのサイズを最終チェックエリアの番号として扱う
				int size = locations.size();

				l.put((s) -> {
					//クリックした時にチェックポイントにテレポートさせつつ、アクションバーにテレポート先を表示する
					s.onClick((event) -> {
						event.player.teleport(locations.get(size - 1));

						//表示例: TP to 1 @ Update1!
						event.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(StringTemplate.format("$0TP to $1 @ $2!", ChatColor.GRAY, size, parkourName)));
					});

					s.icon(Material.GRASS_BLOCK, (i) -> {
						//表示例: 1 @ Update1
						i.displayName = StringTemplate.format("$0$1 @ $2", ChatColor.AQUA, size, parkourName);

						i.lore(
							ChatColor.GRAY + ": Left Click > TP to Last CP!",
							ChatColor.GRAY + ": Right Click > Open This Parkour's CP List!"
						);

						//現在プレイ中のアスレであれば発光させる
						if(parkour.equals(user.currentlyPlayingParkour))
							i.gleam();
					});
				}, slotIndex);
			}
		});
	}

}
