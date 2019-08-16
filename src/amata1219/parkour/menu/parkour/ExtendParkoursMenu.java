package amata1219.parkour.menu.parkour;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.amalib.tuplet.Tuple;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.parkour.RankedParkour;
import amata1219.parkour.user.User;

public class ExtendParkoursMenu implements InventoryUI {

	private final User user;

	public ExtendParkoursMenu(User user){
		this.user = user;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//全Extendアスレを取得する
		List<RankedParkour> parkours = Parkours.getInstance().getExtendParkours();

		//プレイヤーのExtendランクを取得する
		int rank = user.getExtendRank();

		InventoryLine line = InventoryLine.necessaryInventoryLine(rank + 19);

		return build(line, l -> {
			//タイトルを設定する
			l.title = StringColor.color("&b-Extend");

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			//プレイ可能なアスレを表示する
			for(int slotIndex = 0; slotIndex <= Math.min(rank, parkours.size() - 1); slotIndex++){
				RankedParkour parkour = parkours.get(slotIndex);

				//アスレ名を取得する
				String parkourName = parkour.name;

				l.put((s) -> {

					s.onClick(e -> {
						Player player = e.player;

						//ステージのスポーン地点にテレポートさせる
						parkour.teleportTo(player);

						//アスレに参加させる
						parkour.entry(user);

						//表示例: Teleported to The Earth of Marmalade!
						MessageTemplate.capply("&b-Teleported to $0-&r-&b-!", parkourName).displayOnActionBar(player);
					});

					s.icon(Material.GLASS, i -> {
						//表示名: The Earth of Marmalade
						i.displayName = StringTemplate.capply("&b-$0", parkourName);

						List<String> lore = new ArrayList<>();

						int maxMajorCheckAreaNumber = parkour.checkAreas.getMaxMajorCheckAreaNumber();

						//チェックエリア数を表示する
						lore.add(StringTemplate.capply("&7-: &b-Check areas &7-@ $0", maxMajorCheckAreaNumber >= 0 ? StringTemplate.capply("&f-$0", maxMajorCheckAreaNumber + 1) : "§7None"));

						//タイムアタックが有効かどうかを表示する
						lore.add(StringTemplate.capply("&7-: &b-Enable time attack &7-@ &f-$0", parkour.enableTimeAttack));

						//タイムアタックが有効の場合
						if(parkour.enableTimeAttack){
							//上位記録を取得する
							List<Tuple<UUID, String>> records = parkour.records.topTenRecords;

							//記録が1つでもある場合
							if(!records.isEmpty()){
								lore.add("");

								//表示例: Top 10 records
								lore.add(StringTemplate.capply("&7-: &b-Top $0 records", records.size()));

								//最大で上位10名の記録を表示する
								records.stream()
								.map(record -> StringTemplate.capply("&7 - &b-$0 &7-@ &f-$1", Bukkit.getOfflinePlayer(record.first).getName(), record.second))
								.forEach(lore::add);
							}
						}

						//今いるアスレなら発光させる
						if(user.isPlayingWithParkour() && parkour.equals(user.currentParkour)) i.gleam();
					});

				}, slotIndex);
			}

			int lastSlotIndex = line.inventorySize() - 1;

			//ロビー移動ボタンを表示する
			l.put(s -> {

				s.onClick(e -> {
					Player clicker = e.player;

					//本番環境では変える
					clicker.teleport(Bukkit.getWorld("world").getSpawnLocation());

					user.exitParkour();

					//表示例: Teleported to lobby!
					MessageColor.color("&b-Teleported to Extend lobby!").displayOnActionBar(clicker);
				});

				s.icon(Material.FEATHER, i -> {
					i.displayName = StringColor.color("&b-Teleport to Extend lobby");
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
						InventoryUI inventoryUI = null;

						//カテゴリーに対応したアスレリストを取得する
						switch(category){
						case UPDATE:
							inventoryUI = user.inventoryUserInterfaces.updateParkourSelectionUI;
							break;
						case EXTEND:
							inventoryUI = user.inventoryUserInterfaces.extendParkourSelectionUI;
							break;
						default:
							inventoryUI = ParkoursMenus.getInstance().getInventoryUI(category);
							break;
						}

						inventoryUI.openInventory(e.player);
					});

					s.icon(category.icon, i -> {
						//表示例: Update
						i.displayName = StringTemplate.capply("&b-$0", category.name);

						//今開いているステージリストのカテゴリと同じであれば発光させる
						if(category == ParkourCategory.EXTEND) i.gleam();

					});

				}, index);

			});

		});
	}

}
