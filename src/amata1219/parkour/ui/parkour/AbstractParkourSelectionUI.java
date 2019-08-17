package amata1219.parkour.ui.parkour;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.InventoryLine;
import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.string.StringLocalize;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.amalib.tuplet.Tuple;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public abstract class AbstractParkourSelectionUI<T extends Parkour> implements InventoryUI {

	private static final ParkourCategory[] CATEGORIES = ParkourCategory.values();

	private final Users users = Users.getInstnace();

	private final User user;
	private final ParkourCategory category;
	private final Supplier<List<T>> parkours;
	private final Function<List<T>, InventoryLine> line;
	private final Consumer<InventoryLayout> raw;

	public AbstractParkourSelectionUI(User user, ParkourCategory category, Supplier<List<T>> parkours, Function<List<T>, InventoryLine> line, Consumer<InventoryLayout> raw){
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

		InventoryLine line = this.line.apply(parkours);

		return build(line, l -> {
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

						MessageTemplate.clapply("&7-「&r-$0-&r-&7-」&f-にテレポートしました | &7-?", player, parkourName).displayOnActionBar(player);
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

						boolean timeAttackEnable = parkour.timeAttackEnable;

						//表示するテキストを決定する
						String textOfTimeAttackEnable = StringLocalize.apply(timeAttackEnable ? "&b-有効 | &b-Enable" : "&7-無効 | &7-Disable", player);

						lore.add(StringTemplate.clapply("&7-タイムアタック @ $0 | &7-Time Attack @ $0", player, textOfTimeAttackEnable));

						if(timeAttackEnable){
							//上位の記録を取得する
							List<Tuple<UUID, String>> records = parkour.records.topTenRecords;

							//記録が存在する場合
							if(!records.isEmpty()){
								lore.add("");

								lore.add(StringTemplate.clapply("&7-上位-&b-$0件-&7-の記録 | &7-Top &b-$0-&7 Records", player, records.size()));

								AtomicInteger rank = new AtomicInteger(1);

								records.stream()
								.map(record -> StringTemplate.clapply("&b-$0-&7-位 &b-$1 &7-@ &b-$2 | &b-$0-&7-. &b-$1 &7-@ &b-$2", player, rank.getAndIncrement(), Bukkit.getOfflinePlayer(record.first).getName(), record.second))
								.forEach(lore::add);
							}
						}

						//クリア済みのアスレであれば輝かせる
						if(user.clearedParkourNames.contains(parkourName)) i.gleam();
					});

				}, slotIndex.getAndIncrement());
			});

			int inventorySize = line.inventorySize();
			AtomicInteger counter = new AtomicInteger();

			IntStream.range(0, 5)
			.map(i -> inventorySize - i * 2 - 1)
			.sorted()
			.forEach(index -> {
				//対応したカテゴリーを取得する
				ParkourCategory category = CATEGORIES[counter.getAndIncrement()];

				l.put(s -> {

					s.onClick(e -> user.inventoryUIs.getParkourSelectionUI(category).openInventory(player));

					s.icon(category.icon, i -> {
						i.displayName = StringTemplate.capply("&b-$0", categoryName);

						//今開いているカテゴリーと同じであれば輝かせる
						if(category == this.category) i.gleam();
					});

				}, index);
			});

			raw.accept(l);
		});
	}

}
