package amata1219.parkour.ui;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.message.MessageTemplate;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.Checkpoints;
import amata1219.parkour.user.User;

public class AllCheckpointSelectionUI implements InventoryUI {

	private final User user;
	private final Parkour parkour;

	public AllCheckpointSelectionUI(User user, Parkour parkour){
		this.user = user;
		this.parkour = parkour;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Checkpoints checkpoints = user.checkpoints;

		//チェックポイントの座標リストを取得する
		List<ImmutableEntityLocation> locations = checkpoints.containsParkour(parkour) ? checkpoints.getCheckpoints(parkour) : Collections.emptyList();

		//アスレ名を取得する
		String parkourName = parkour.name;

		return build(locations.size(), (l) -> {
			//タイトルを設定する
			l.title = StringTemplate.capply("&b-$0 &r-checkpoints", parkourName);

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			//各座標毎に処理をする
			for(int slotIndex = 0; slotIndex < locations.size(); slotIndex++){
				//対応した座標を取得する
				ImmutableEntityLocation location = locations.get(slotIndex);

				int displayCheckAreaNumber = slotIndex + 1;

				l.put((s) -> {

					s.onClick((event) -> {
						//クリックしたプレイヤーを取得する
						Player player = event.player;

						//別のアスレに移動するのであれば参加処理をする
						if(parkour != user.currentParkour) parkour.entry(user);

						//プレイヤーを最終チェックポイントにテレポートさせる
						player.teleport(location.asBukkitLocation());

						//表示例: Teleported to checkpoint 1 @ Update1!
						MessageTemplate.capply("&b-Teleported to checkpoint &0 &7-@ &b-$1-&r-&b-!", displayCheckAreaNumber, parkourName).displayOnActionBar(player);
					});

					s.icon(Material.GRASS_BLOCK,(i) -> {
						//表示例: 1 @ Update1
						i.displayName = StringTemplate.capply("&7-$0 @ $1", displayCheckAreaNumber, parkourName);

						//説明文を設定する
						i.lore(
							StringColor.color("&7-: &b-Click &7-@ &b-Teleport to checkpoint in this parkour")
						);
					});

				}, slotIndex);
			}
		});
	}

}
