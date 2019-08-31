package amata1219.parkour.ui;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.parkour.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.location.ImmutableLocation;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.text.BilingualText;
import amata1219.parkour.text.Text;
import amata1219.parkour.user.CheckpointSet;
import amata1219.parkour.user.User;

public class ParkourCheckpointSelectionUI extends AbstractUI {

	private final Parkour parkour;

	public ParkourCheckpointSelectionUI(User user, Parkour parkour){
		super(user);
		this.parkour = parkour;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Player player = user.asBukkitPlayer();

		CheckpointSet checkpoints = user.checkpoints;

		//アスレに対応したチェックポイントマップを取得する
		Map<Integer, ImmutableLocation> points = checkpoints.getMajorCheckAreaNumbersAndCheckpoints(parkour);

		//メジャーチェックエリア番号を昇順にソートする
		List<Integer> sortedMajorCheckAreaNumbers = points.keySet().stream().sorted((x, y) -> Integer.compare(x, y)).collect(Collectors.toList());

		int checkpointSize = sortedMajorCheckAreaNumbers.size();

		//アスレ名を取得する
		String parkourName = parkour.name;
		String prefixColor = parkour.prefixColor;

		return build(checkpointSize, l -> {
			l.title = parkour.colorlessName();

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			//各座標毎に処理をする
			for(int slotIndex = 0; slotIndex < checkpointSize; slotIndex++){
				int majorCheckAreaNumber = sortedMajorCheckAreaNumbers.get(slotIndex);

				//対応した座標を取得する
				ImmutableLocation point = points.get(majorCheckAreaNumber);

				int majorCheckAreaNumberForDisplay = majorCheckAreaNumber + 1;

				l.put(s -> {

					s.onClick(e -> {
						//今いるアスレを取得する
						Parkour current = user.currentParkour;

						//別のアスレに移動するのであれば参加処理をする
						if(!parkour.equals(current)) parkour.entry(user);

						//プレイヤーを最終チェックポイントにテレポートさせる
						player.teleport(point.asBukkit());

						BilingualText.stream("$parkour-&r-$colorのチェックポイント$numberにテレポートしました",
								"You teleported to $parkour checkpoint$number")
								.setAttribute("$parkour", parkourName)
								.setAttribute("$color", prefixColor)
								.setAttribute("$number", majorCheckAreaNumberForDisplay)
								.color()
								.setReceiver(player)
								.sendActionBarMessage();
					});

					s.icon(Material.PRISMARINE_CRYSTALS, i -> {
						i.displayName = Text.stream("$color$number &7-@ $parkour")
								.setAttribute("$color", prefixColor)
								.setAttribute("$number", majorCheckAreaNumberForDisplay)
								.setAttribute("$parkour", parkourName)
								.color()
								.toString();

						String lore = BilingualText.stream("&7-: &b-クリック &7-@ このチェックポイントにテレポートします。",
								"&7-: &b-Click &7-@ Teleport to this checkpoint.")
								.textBy(player)
								.color()
								.toString();

						i.lore(lore);

						i.amount = majorCheckAreaNumberForDisplay;
					});

				}, slotIndex);
			}
		});
	}

}
