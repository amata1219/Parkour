package amata1219.parkour.ui.stage;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.message.MessageTemplate;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.StageCategory;
import amata1219.parkour.stage.StageSet;
import amata1219.parkour.user.UserSet;

public class StageSelectionUI implements InventoryUI {

	/*
	 *
	 * 	Normal
		Update
		Extend
		Segment
		Biome
	 *
	 */

	/*
	 *
	 * oooooxxxx
	 * xxxxxxxxx
	 * xxxxxxxxx
	 * xxxxxxxxx
	 * xxxxxxxxx
	 * oxoxoxoxo
	 *
	 */

	private final UserSet users = UserSet.getInstnace();
	private final StageCategory category;

	public StageSelectionUI(StageCategory category){
		this.category = category;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//カテゴリ内のステージリストを取得する
		List<Stage> stages = StageSet.getInstance().getStagesByCategory(category);

		return build(stages.size() + 9, (l) -> {
			//表示例: Extend
			l.title = StringTemplate.capply("&b-$0", category.getName());

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			//各ステージ毎に処理をする
			for(int slotIndex = 0; slotIndex < stages.size(); slotIndex++){
				//対応したステージを取得する
				Stage stage = stages.get(slotIndex);

				String stageName = stage.name;

				l.put((s) -> {

					s.onClick((event) -> {
						Player player = event.player;

						//ステージのスポーン地点にテレポートさせる
						player.teleport(stage.getSpawnLocation().asBukkitLocation());

						users.getUser(player).currentStage = stage;

						//表示例: Teleported to The Earth of Marmalade!
						MessageTemplate.capply("&b-Teleported to $0-&r-&b-!", stageName).displayOnActionBar(player);
					});

					s.icon(Material.GRASS_BLOCK, (i) -> {
						//表示名: The Earth of Marmalade
						i.displayName = StringTemplate.capply("&b-$0", stageName);

						//ステージ内のアスレの名前を説明文にセットする
						i.lore = stage.parkourNames.stream().map(parkourName -> StringTemplate.capply("&7-: &b-$0", parkourName)).collect(Collectors.toList());
					});

				}, slotIndex);

			}

			//ステージのカテゴリ一覧
			StageCategory[] categories = StageCategory.values();

			//このインベントリの最後のスロットのインデックス
			int lastSlotIndex = l.option.size - 1;

			for(int count = 0; count < 5; count++){
				StageCategory slotCategory = categories[count];

				//スロットのインデックス
				int slotIndex = lastSlotIndex - count * 2;

				l.put((s) -> {

					s.onClick((event) -> {
						//カテゴリに対応したステージリストを開かせる
						StageSelectionUISet.getInstance().getStagesUI(slotCategory).openInventory(event.player);
					});

					s.icon(Material.FEATHER, (i) -> {
						//表示例: Update
						i.displayName = StringTemplate.capply("&b-$0", slotCategory.getName());

						//今開いているステージリストのカテゴリと同じであれば発光させる
						if(slotCategory == category) i.gleam();

					});

				}, slotIndex);
			}

		});
	}

}
