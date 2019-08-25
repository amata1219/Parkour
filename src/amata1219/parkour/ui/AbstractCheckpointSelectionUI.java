package amata1219.parkour.ui;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.parkour.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.location.ImmutableLocation;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.string.StringTemplate;
import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.user.CheckpointSet;
import amata1219.parkour.user.User;

public abstract class AbstractCheckpointSelectionUI implements InventoryUI {

	private final User user;
	private final String checkpointType;
	private final BiFunction<CheckpointSet, Parkour, ImmutableLocation> getCheckpoint;
	private final BiFunction<CheckpointSet, Parkour, Integer> getCheckpointNumber;

	public AbstractCheckpointSelectionUI(User user, String checkpointType, BiFunction<CheckpointSet, Parkour, ImmutableLocation> getCheckpoint, BiFunction<CheckpointSet, Parkour, Integer> getCheckpointNumber){
		this.user = user;
		this.checkpointType = checkpointType;
		this.getCheckpoint = getCheckpoint;
		this.getCheckpointNumber = getCheckpointNumber;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Localizer localizer = user.localizer;

		//今いるアスレを取得する
		Parkour currentParkour = user.currentParkour;

		//カテゴリーを取得する
		ParkourCategory category = currentParkour.category;

		//カテゴリー内の有効なアスレを辞書順にソートして取得する
		List<Parkour> parkours = Parkours.getInstance()
		.getEnabledParkours(category)
		.sorted((p1, p2) -> p1.colorlessName().compareTo(p2.colorlessName()))
		.collect(Collectors.toList());

		return build(parkours.size(), l -> {
			l.title = localizer.applyAll("$0の$1チェックポイント一覧 | List of $1 Checkpoints in $0", category.name, localizer.localize(checkpointType));

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			CheckpointSet checkpoints = user.checkpoints;

			//各アスレ毎に処理をする
			for(int slotIndex = 0; slotIndex < parkours.size(); slotIndex++){
				//インデックスに対応したアスレを取得する
				Parkour parkour = parkours.get(slotIndex);

				//アスレに対応したチェックポイントが存在しなければ繰り返す
				if(!checkpoints.containsParkour(parkour)) continue;

				//アスレ名を取得する
				String parkourName = parkour.name;

				//最終チェックポイントを取得する
				ImmutableLocation lastCheckpoint = getCheckpoint.apply(checkpoints, parkour);

				//メジャーチェックエリア番号を取得する
				int majorCheckAreaNumberDisplayed = getCheckpointNumber.apply(checkpoints, parkour) + 1;

				l.put(s -> {

					s.onClick(event -> {
						//クリックしたプレイヤーを取得する
						Player player = event.player;

						if(event.isRightClick()){
							//今いるアスレを取得する
							Parkour current = user.currentParkour;

							//別のアスレに移動するのであれば参加処理をする
							if(!parkour.equals(current)) parkour.entry(user);

							//プレイヤーを最終チェックポイントにテレポートさせる
							player.teleport(lastCheckpoint.asBukkit());

							localizer.mapplyAll("&b-チェックポイント$0 &7-@ &b-$1 にテレポートしました | &b-Teleported to checkpoint$0 &7-@ &b-$1", majorCheckAreaNumberDisplayed, parkour.colorlessName()).displayOnActionBar(player);
						}else if(event.isLeftClick()){
							//チェックポイントリストを開かせる
							new CategorizedCheckpointSelectionUI(user, parkour).openInventory(player);
						}

					});

					s.icon(Material.LIGHT_BLUE_DYE, i -> {
						//表示例: 1 @ Update1
						i.displayName = StringTemplate.capply("&7-$0 @ $1", majorCheckAreaNumberDisplayed, parkourName);

						String colorlessParkourName = parkour.colorlessName();

						//説明文を設定する
						i.lore(
							localizer.color("&7-: &b-右クリック &7-@ このチェックポイントにテレポートします。 | &7-: &b-Right click &7-@ Teleport to this checkpoint."),
							localizer.applyAll("&7-: &b-左クリック &7-@ $0で設定したチェックポイント一覧を開きます。 | &7-: &b-Left click &7-@ Opens a list of checkpoints you have set in $0.", colorlessParkourName)
						);

						i.amount = majorCheckAreaNumberDisplayed;

						//現在プレイ中のアスレであれば発光させる
						if(parkour.equals(currentParkour)) i.gleam();

					});

				}, slotIndex);
			}
		});
	}

}
