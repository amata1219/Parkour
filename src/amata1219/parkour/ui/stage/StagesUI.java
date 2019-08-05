package amata1219.parkour.ui.stage;

import java.util.List;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.StageCategory;
import amata1219.parkour.stage.StageSet;

public class StagesUI implements InventoryUI {

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

	private final StageCategory category;
	private final String categoryName;

	public StagesUI(StageCategory category){
		this.category = category;

		String name = category.toString();
		categoryName = name.charAt(0) + name.substring(1).toLowerCase();
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//カテゴリ内のステージリストを取得する
		List<Stage> stages = StageSet.getInstance().getStages(category);

		InventoryLine line = InventoryLine.necessaryInventoryLine(stages.size() + 9);

		return build(line, (l) -> {
			//表示例: Extend
			l.title = StringTemplate.applyWithColor("&b-$0", categoryName);

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			//各ステージ毎に処理をする
			for(Stage stage : stages){

			}

		});
	}

}
