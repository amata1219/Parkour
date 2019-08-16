package amata1219.parkour.ui;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.location.ImmutableLocation;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.Checkpoints;
import amata1219.parkour.user.User;

public class CategorizedCheckpointSelectionUI implements InventoryUI {

	private final User user;
	private final Parkour parkour;

	public CategorizedCheckpointSelectionUI(User user, Parkour parkour){
		this.user = user;
		this.parkour = parkour;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Checkpoints checkpoints = user.checkpoints;

		//アスレに対応したチェックポイントマップを取得する
		Map<Integer, ImmutableLocation> points = checkpoints.getMajorCheckAreaNumbersAndCheckpoints(parkour);

		//メジャーチェックエリア番号を昇順にソートする
		List<Integer> sortedMajorCheckAreaNumbers = points.keySet().stream().sorted((x, y) -> Integer.compare(x, y)).collect(Collectors.toList());

		int checkpointSize = sortedMajorCheckAreaNumbers.size();

		//アスレ名を取得する
		String parkourName = parkour.name;

		return build(checkpointSize, l -> {
			//タイトルを設定する
			l.title = StringTemplate.capply("&b-$0 &r-checkpoints", parkourName);

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			//各座標毎に処理をする
			for(int slotIndex = 0; slotIndex < checkpointSize; slotIndex++){
				int majorCheckAreaNumber = sortedMajorCheckAreaNumbers.get(slotIndex);

				//対応した座標を取得する
				ImmutableLocation point = points.get(majorCheckAreaNumber);

				int majorCheckAreaNumberDisplayed = majorCheckAreaNumber + 1;

				l.put(s -> {

					s.onClick(e -> {
						//クリックしたプレイヤーを取得する
						Player player = e.player;

						//別のアスレに移動するのであれば参加処理をする
						if(parkour != user.currentParkour) parkour.entry(user);

						//プレイヤーを最終チェックポイントにテレポートさせる
						player.teleport(point.asBukkit());

						//表示例: Teleported to checkpoint 1 @ Update1!
						MessageTemplate.capply("&b-Teleported to checkpoint &0 &7-@ &b-$1-&r-&b-!", majorCheckAreaNumberDisplayed, parkourName).displayOnActionBar(player);
					});

					s.icon(Material.LIGHT_BLUE_DYE, i -> {
						//表示例: 1 @ Update1
						i.displayName = StringTemplate.capply("&7-$0 @ $1", majorCheckAreaNumberDisplayed, parkourName);

						//説明文を設定する
						i.lore(
							StringColor.color("&7-: &b-Click &7-@ &b-Teleport to checkpoint in this parkour")
						);

						i.amount = majorCheckAreaNumberDisplayed;
					});

				}, slotIndex);
			}
		});
	}

}
