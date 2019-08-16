package amata1219.parkour.ui.parkour;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.InventoryLine;
import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.string.StringLocalize;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class ParkourUI<T extends Parkour, N> implements InventoryUI {

	private final Users users = Users.getInstnace();

	private final User user;
	private final ParkourCategory category;
	private final Supplier<List<T>> parkours;
	private final BiFunction<User, List<T>, InventoryLine> line;
	private final Consumer<InventoryLayout> raw;

	public ParkourUI(User user, ParkourCategory category, Supplier<List<T>> parkours, BiFunction<User, List<T>, InventoryLine> line, Consumer<InventoryLayout> raw){
		this.user = user;
		this.category = category;
		this.parkours = parkours;
		this.line = line;
		this.raw = raw;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//カテゴリー名を取得する
		String categoryName = category.name;

		//UI上に表示可能なアスレリストを取得する
		List<T> parkours = this.parkours.get();

		return build(line.apply(user, parkours), l -> {
			l.title = StringTemplate.capply("&b-$0", categoryName);

			//デフォルトスロットを設定する
			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			Player player = l.player;
			AtomicInteger slotIndex = new AtomicInteger();

			parkours.forEach(parkour -> {
				String parkourName = parkour.name;

				l.put(s -> {

					s.onClick(e -> {
						//アスレのスポーン地点にテレポートさせる
						parkour.teleportTo(player);

						//アスレに参加させる
						parkour.entry(users.getUser(player));

						MessageTemplate.capply("&7-「&r-$0-&r-&7-」&f-にテレポートしました | &7-?", parkourName).localize().displayOnActionBar(player);
					});

					//アスレのアイコンを設定する
					s.icon(Material.PRISMARINE_SHARD, i -> {
						i.displayName = parkourName;

						List<String> lore = new ArrayList<>();

						//アスレの最大メジャーチェックエリア番号を取得する
						int maxMajorCheckAreaNumber = parkour.checkAreas.getMaxMajorCheckAreaNumber();

						//表示するテキストを決定する
						String numberOfDisplayedCheckAreas = maxMajorCheckAreaNumber >= 0 ? String.valueOf(maxMajorCheckAreaNumber + 1) : "None";

						lore.add(StringTemplate.clapply("&7-チェックエリア @ &b-$0箇所 | &7-Check Areas @ &b-$0", player, numberOfDisplayedCheckAreas));

						boolean enableTimeAttack = parkour.timeAttackEnable;

						//表示するテキストを決定する
						String textOfTimeAttackEnable = StringLocalize.apply(enableTimeAttack ? "&b-有効 | &b-Enable" : "&7-無効 | &7-Disable", player);

						lore.add(StringTemplate.clapply("", player, textOfTimeAttackEnable));
					});

				}, slotIndex.getAndIncrement());
			});

			raw.accept(l);
		});
	}

	/*
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

						//ユーザーを取得する
						User user = users.getUser(l.player);

						//今いるアスレなら発光させる
						if(user.isPlayingWithParkour() && parkour.equals(user.currentParkour)) i.gleam();
					});

				}, slotIndex.getAndIncrement());

			});

			AtomicInteger counter = new AtomicInteger();

			IntStream.range(0, 5)
			.map(i -> i * 2)
			.map(i -> line.inventorySize() - 1 - i)
			.sorted()
			.forEach(index -> {
				//対応したカテゴリーを取得する
				ParkourCategory category = ParkourCategory.values()[counter.getAndIncrement()];

				l.put(s -> {

					s.onClick(e -> {
						Player clicker = e.player;

						InventoryUI inventoryUI = null;

						//ユーザーを取得する
						User user = users.getUser(clicker);

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

						inventoryUI.openInventory(clicker);
					});

					s.icon(category.icon, i -> {
						//表示例: Update
						i.displayName = StringTemplate.capply("&b-$0", category.name);

						//今開いているステージリストのカテゴリと同じであれば発光させる
						if(category == this.category) i.gleam();

					});

				}, index);

			});

		});
	 */

}
