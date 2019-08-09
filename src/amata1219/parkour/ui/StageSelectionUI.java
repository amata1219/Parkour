package amata1219.parkour.ui;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.InventoryLine;
import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.StageCategory;
import amata1219.parkour.stage.Stages;
import amata1219.parkour.user.Users;

public class StageSelectionUI implements InventoryUI {

	private final Users users = Users.getInstnace();
	private final StageCategory category;

	public StageSelectionUI(StageCategory category){
		this.category = category;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//カテゴリ内のステージリストを取得する
		List<Stage> stages = Stages.getInstance().getStagesByCategory(category);

		InventoryLine line = InventoryLine.necessaryInventoryLine(stages.size() + 9);

		return build(line, (l) -> {
			//表示例: Extend
			l.title = StringTemplate.capply("&b-$0", category.getName());

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			AtomicInteger slotIndex = new AtomicInteger();

			stages.forEach(stage -> {
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

					s.icon(Material.GLASS, (i) -> {
						//表示名: The Earth of Marmalade
						i.displayName = StringTemplate.capply("&b-$0", stageName);

						//ステージ内のアスレの名前を説明文にセットする
						i.lore = stage.parkourNames.stream().map(parkourName -> StringTemplate.capply("&7-: &b-$0", parkourName)).collect(Collectors.toList());
					});

				}, slotIndex.getAndIncrement());

			});

			AtomicInteger counter = new AtomicInteger();

			IntStream.range(0, 5)
			.map(i -> i * 2)
			.map(i -> line.inventorySize() - 1 - i)
			.sorted()
			.forEach(i -> {
				//対応したカテゴリーを取得する
				StageCategory category = StageCategory.values()[counter.getAndIncrement()];

				l.put((s) -> {

					s.onClick((event) -> {
						//カテゴリに対応したステージリストを開かせる
						StageSelectionUIs.getInstance().getStagesUI(category).openInventory(event.player);
					});

					s.icon(Material.FEATHER, (ic) -> {
						//表示例: Update
						ic.displayName = StringTemplate.capply("&b-$0", category.getName());

						//今開いているステージリストのカテゴリと同じであれば発光させる
						if(category == this.category) ic.gleam();

					});

				}, i);

			});

		});
	}

}
