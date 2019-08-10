package amata1219.parkour.ui;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.InventoryLine;
import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.user.Users;

public class CategorizedParkoursSelectionUI implements InventoryUI {

	private final Users users = Users.getInstnace();
	private final ParkourCategory category;

	public CategorizedParkoursSelectionUI(ParkourCategory category){
		this.category = category;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//カテゴリ名を取得する
		String categoryName = category.name;

		//カテゴリ内のステージリストを取得する
		List<Parkour> parkours = Parkours.getInstance().getParkours(category);

		InventoryLine line = InventoryLine.necessaryInventoryLine(parkours.size() + 9);

		return build(line, (l) -> {
			//表示例: Extend
			l.title = StringTemplate.capply("&b-$0", categoryName);

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			AtomicInteger slotIndex = new AtomicInteger();

			parkours.forEach(parkour -> {
				//アスレ名を取得する
				String parkourName = parkour.name;

				l.put((s) -> {

					s.onClick((event) -> {
						Player player = event.player;

						//ステージのスポーン地点にテレポートさせる
						player.teleport(parkour.spawnPoint.asBukkitLocation());

						//選択したアスレを今いるアスレとして設定する
						users.getUser(player).currentParkour = parkour;

						//表示例: Teleported to The Earth of Marmalade!
						MessageTemplate.capply("&b-Teleported to $0-&r-&b-!", parkourName).displayOnActionBar(player);
					});

					s.icon(Material.GLASS, (i) -> {
						//表示名: The Earth of Marmalade
						i.displayName = StringTemplate.capply("&b-$0", parkourName);
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
				ParkourCategory category = ParkourCategory.values()[counter.getAndIncrement()];

				l.put((s) -> {

					s.onClick((event) -> {
						//カテゴリに対応したステージリストを開かせる
						ParkourMenuUI.getInstance().getInventoryUI(category).openInventory(event.player);
					});

					s.icon(Material.FEATHER, (ic) -> {
						//表示例: Update
						ic.displayName = StringTemplate.capply("&b-$0", categoryName);

						//今開いているステージリストのカテゴリと同じであれば発光させる
						if(category == this.category) ic.gleam();

					});

				}, i);

			});

		});
	}

}
