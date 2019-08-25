package amata1219.parkour.ui;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.parkour.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.location.ImmutableLocation;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.string.StringTemplate;
import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.user.CheckpointSet;
import amata1219.parkour.user.User;

public class ParkourCheckpointSelectionUI implements InventoryUI {

	private final User user;
	private final Localizer localizer;
	private final Parkour parkour;

	public ParkourCheckpointSelectionUI(User user, Parkour parkour){
		this.user = user;
		this.localizer = user.localizer;
		this.parkour = parkour;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		CheckpointSet checkpoints = user.checkpoints;

		//アスレに対応したチェックポイントマップを取得する
		Map<Integer, ImmutableLocation> points = checkpoints.getMajorCheckAreaNumbersAndCheckpoints(parkour);

		//メジャーチェックエリア番号を昇順にソートする
		List<Integer> sortedMajorCheckAreaNumbers = points.keySet().stream().sorted((x, y) -> Integer.compare(x, y)).collect(Collectors.toList());

		int checkpointSize = sortedMajorCheckAreaNumbers.size();

		//アスレ名を取得する
		String parkourName = parkour.name;
		String parkourColor = parkour.color;
		String colorlessParkourName = parkour.colorlessName();

		return build(checkpointSize, l -> {
			l.title = localizer.applyAll("$0のチェックポイント一覧 | $0 Checkpoints", colorlessParkourName);

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

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

						//今いるアスレを取得する
						Parkour current = user.currentParkour;

						//別のアスレに移動するのであれば参加処理をする
						if(!parkour.equals(current)) parkour.entry(user);

						//プレイヤーを最終チェックポイントにテレポートさせる
						player.teleport(point.asBukkit());

						localizer.applyAll("$0-r-$1のチェックポイント$2にテレポートしました | $1Teleported to $0 checkpoint$2", parkourName, parkourColor, majorCheckAreaNumberDisplayed);
					});

					s.icon(Material.PRISMARINE_CRYSTALS, i -> {
						//表示例: 1 @ Update1
						i.displayName = StringTemplate.capply("$0$1 &7-@ $1", parkourColor, majorCheckAreaNumberDisplayed, parkourName);

						//説明文を設定する
						i.lore(
							localizer.color("&7-: &b-クリック &7-@ このチェックポイントにテレポートします。 | &7-: &b-Click &7-@ Teleport to this checkpoint.")
						);

						i.amount = majorCheckAreaNumberDisplayed;
					});

				}, slotIndex);
			}
		});
	}

}
