package amata1219.parkour.ui.parkour;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.InventoryLine;
import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.MessageColor;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.parkour.RankedParkour;
import amata1219.parkour.user.User;

public class UpdateParkourSelectionUI implements InventoryUI {

	private final User user;

	public UpdateParkourSelectionUI(User user){
		this.user = user;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//全Updateアスレを取得する
		List<RankedParkour> parkours = Parkours.getInstance().getUpdateParkours();

		//プレイヤーのUpdateランクを取得する
		int rank = user.getUpdateRank();

		InventoryLine line = InventoryLine.necessaryInventoryLine(rank + 19);

		return build(line, l -> {
			//タイトルを設定する
			l.title = StringColor.color("&b-Update");

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			//プレイ可能なアスレのテレポーターをセットする
			for(int slotIndex = 0; slotIndex <= rank; slotIndex++){
				RankedParkour parkour = parkours.get(slotIndex);

				l.put(s -> {

					s.onClick(e -> {

					});

				}, slotIndex);
			}

			int lastSlotIndex = line.inventorySize() - 1;

			//ロビーテレポーターをセットする
			l.put(s -> {

				s.onClick(e -> {
					Player clicker = e.player;

					//本番環境では変える
					clicker.teleport(Bukkit.getWorld("world").getSpawnLocation());

					//表示例: Teleported to lobby!
					MessageColor.color("&b-Teleported to Update lobby!").displayOnActionBar(clicker);
				});

				s.icon(Material.FEATHER, i -> {
					i.displayName = StringColor.color("&b-Teleport to Update lobby");
					i.gleam();
				});

			}, lastSlotIndex - 9);

			AtomicInteger counter = new AtomicInteger();

			IntStream.range(0, 5)
			.map(i -> i * 2)
			.map(i -> lastSlotIndex - i)
			.sorted()
			.forEach(index -> {
				//対応したカテゴリーを取得する
				ParkourCategory category = ParkourCategory.values()[counter.getAndIncrement()];

				l.put(s -> {

					s.onClick(e -> {
						//カテゴリーに対応したアスレリストを開かせる
						switch(category){
						case UPDATE:
							
							break;
						case EXTEND:
							
							break;
						default:
							ParkourMenuUI.getInstance().getInventoryUI(category).openInventory(e.player);
							break;
						}
					});

					s.icon(Material.FEATHER, i -> {
						//表示例: Update
						i.displayName = StringTemplate.capply("&b-$0", category.name);

						//今開いているステージリストのカテゴリと同じであれば発光させる
						if(category == ParkourCategory.UPDATE) i.gleam();

					});

				}, index);

			});

		});
	}

}
