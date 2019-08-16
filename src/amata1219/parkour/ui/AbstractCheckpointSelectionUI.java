package amata1219.parkour.ui;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.location.ImmutableLocation;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.user.Checkpoints;
import amata1219.parkour.user.User;

public class AbstractCheckpointSelectionUI implements InventoryUI {

	private final User user;
	private final String checkpointType, lowerCaseCheckpointType;
	private final BiFunction<Checkpoints, Parkour, ImmutableLocation> getCheckpoint;
	private final BiFunction<Checkpoints, Parkour, Integer> getCheckpointNumber;

	protected AbstractCheckpointSelectionUI(User user, String checkpointType, BiFunction<Checkpoints, Parkour, ImmutableLocation> getCheckpoint, BiFunction<Checkpoints, Parkour, Integer> getCheckpointNumber){
		this.user = user;
		this.checkpointType = checkpointType;
		this.lowerCaseCheckpointType = checkpointType.toLowerCase();
		this.getCheckpoint = getCheckpoint;
		this.getCheckpointNumber = getCheckpointNumber;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//現在プレイ中のアスレを取得する
		Parkour currentParkour = user.currentParkour;

		//カテゴリーを取得する
		ParkourCategory category = currentParkour.category;

		//カテゴリー内の全アスレを取得する
		List<Parkour> parkours = Parkours.getInstance().getParkours(category);

		return build(parkours.size(), l -> {
			//表示例: Last checkpoints @ The Earth of Marmalade
			l.title = StringTemplate.capply("&b-$0 checkpoints &7-@ &b-$1", checkpointType, category.name);

			//デフォルトスロットを設定する
			l.defaultSlot(s -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			Checkpoints checkpoints = user.checkpoints;

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
							//別のアスレに移動するのであれば参加処理をする
							if(parkour != user.currentParkour) parkour.entry(user);

							//プレイヤーを最終チェックポイントにテレポートさせる
							player.teleport(lastCheckpoint.asBukkit());

							//表示例: Teleported to checkpoint 1 @ Update1!
							MessageTemplate.capply("&b-Teleported to a checkpoint &0 &7-@ &b-$1-&r-&b-!", majorCheckAreaNumberDisplayed, parkourName).displayOnActionBar(player);

						}else if(event.isLeftClick()){
							//チェックポイントリストを開かせる
							new CategorizedCheckpointSelectionUI(user, parkour).openInventory(player);
						}

					});

					s.icon(Material.LIGHT_BLUE_DYE, i -> {
						//表示例: 1 @ Update1
						i.displayName = StringTemplate.capply("&7-$0 @ $1", majorCheckAreaNumberDisplayed, parkourName);

						//説明文を設定する
						i.lore(
							StringTemplate.capply("&7-: &b-Right click &7-@ &b-Teleport to $0 checkpoint in this parkour", lowerCaseCheckpointType),
							StringColor.color("&7-: &b-Left click &7-@ &b-Open list of checkpoints you have passed through in this parkour")
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
