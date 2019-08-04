package amata1219.parkour.ui;

import java.util.List;
import java.util.Map;
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
import amata1219.parkour.stage.Stage;
import amata1219.parkour.user.User;

public class LastCheckpointsUI implements InventoryUI {

	private final User user;

	public LastCheckpointsUI(User user){
		this.user = user;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//現在プレイ中のアスレを取得する
		Parkour parkourPlayingNow = user.parkourPlayingNow;

		//今いるステージを取得する
		Stage stage = parkourPlayingNow.getStage();

		//ステージ内にあるアスレを取得する
		List<Parkour> parkourList = stage.getParkourList();

		return build(parkourList.size(), (l) -> {
			//表示例: Last checkpoints @ The Earth of Marmalade
			l.title = StringTemplate.applyWithColor("&b-Last checkpoints &7-@ &b-$0", stage.name);

			//ユーザーのチェックポイントマップを取得する
			Map<String, List<ImmutableEntityLocation>> points = user.checkpoints;

			//各アスレ毎に処理をする
			for(int slotIndex = 0; slotIndex < parkourList.size(); slotIndex++){
				//インデックスに対応したアスレを取得する
				Parkour parkour = parkourList.get(slotIndex);

				//アスレ名を取得する
				String parkourName = parkour.name;

				//アスレに対応したチェックポイントが存在しなければ繰り返す
				if(!points.containsKey(parkourName))
					continue;

				//チェックポイントのリストを取得する
				List<ImmutableEntityLocation> locations = points.get(parkourName);

				//リストのサイズを最終チェックエリアの番号として扱う
				int displayCheckAreaNumber = locations.size();

				l.put((s) -> {

					s.onClick((event) -> {
						//クリックしたプレイヤーを取得する
						Player player = event.player;

						//最終チェックポイントを取得する
						ImmutableEntityLocation lastCheckpoint = locations.get(displayCheckAreaNumber - 1);

						//プレイヤーを最終チェックポイントにテレポートさせる
						player.teleport(lastCheckpoint.asBukkitLocation());

						//表示例: Teleported to checkpoint 1 @ Update1!
						MessageTemplate.applyWithColor("&b-Teleported to a checkpoint &0 &7-@ &b-$1-&r-&b-!", displayCheckAreaNumber, parkourName).displayOnActionBar(player);;
					});

					s.icon(Material.GRASS_BLOCK, (i) -> {
						//表示例: 1 @ Update1
						i.displayName = StringTemplate.applyWithColor("&7-$0 @ $1", displayCheckAreaNumber, parkourName);

						//説明文を設定する
						i.lore(
							StringColor.color("&7-: &b-Left click &7-@ &b-Teleport to last checkpoint in this parkour"),
							StringColor.color("&7-: &b-Right click &7-@ &b-Open list of checkpoints you have passed through in this parkour")
						);

						//現在プレイ中のアスレであれば発光させる
						if(parkour.equals(parkourPlayingNow)) i.gleam();

					});

				}, slotIndex);
			}
		});
	}

}
