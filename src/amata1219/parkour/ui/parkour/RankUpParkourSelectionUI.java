package amata1219.parkour.ui.parkour;

import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.parkour.inventory.ui.InventoryLine;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.parkour.RankUpParkour;
import amata1219.parkour.string.StringLocalize;
import amata1219.parkour.string.message.MessageLocalize;
import amata1219.parkour.user.User;

public class RankUpParkourSelectionUI extends AbstractParkourSelectionUI<RankUpParkour> {

	public RankUpParkourSelectionUI(User user, ParkourCategory category,  Supplier<Integer> rank) {
		super(
			user,
			category,

			//カテゴリーに対応したアスレリストを返す関数を作成する
			() -> ParkourSet.getInstance().getEnabledParkours(category)
			.map(parkour -> (RankUpParkour) parkour)
			.sorted((parkour1, parkour2) -> Integer.compare(parkour1.rank, parkour2.rank))
			.collect(Collectors.toList()),

			//必要な段数を計算する関数を作成する
			parkours -> InventoryLine.necessaryInventoryLine(rank.get() + 19),

			//ロビーへの移動ボタンをセットする
			layout -> {

				layout.put(s -> {
					Player player = layout.player;

					s.onClick(e -> {
						//本番環境では変える
						player.teleport(Bukkit.getWorld("world").getSpawnLocation());

						user.exitCurrentParkour();

						MessageLocalize.applyAll("&b-$0ロビーにテレポートしました | &b-Teleported to $0 lobby", player, category.name).displayOnActionBar(player);
					});

					s.icon(Material.NETHER_STAR, i -> i.displayName = StringLocalize.applyAll("&b-$0ロビーにテレポートする | &b-Teleport to $0 Lobby", player, category.name));

				}, layout.option.size - 10);

			}
		);
	}

}
