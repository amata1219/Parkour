package amata1219.parkour.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.parkour.inventory.ui.InventoryLine;
import amata1219.parkour.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.item.SkullCreator;
import amata1219.parkour.string.StringTemplate;
import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.tuplet.Quadruple;
import amata1219.parkour.user.User;

public class MyProfileUI implements InventoryUI {

	private static final ArrayList<Quadruple<Integer, Material, String, Consumer<User>>> ICONS = new ArrayList<>();

	@SafeVarargs
	private static void initialize(Quadruple<Integer, Material, String, Consumer<User>>... icons){
		Arrays.stream(icons).forEach(ICONS::add);
	}

	static{
		initialize(
			new Quadruple<>(4, Material.PRISMARINE_SLAB, "&b-スコアボードオプション | &b-Scoreboard Options", u -> u.inventoryUserInterfaces.openScoreboardOptionSelectionUI()),
			new Quadruple<>(5, Material.PRISMARINE_BRICK_SLAB, "&b-帽子を購入する | &b-Buy Hats", u -> u.inventoryUserInterfaces.openBuyHatUI()),
			new Quadruple<>(6, Material.DARK_PRISMARINE_SLAB, "&b-帽子を被る | &b-?", u -> u.inventoryUserInterfaces.openWearHatUI()),
			new Quadruple<>(7, Material.QUARTZ_SLAB, "&bロビーにテレポートする | &b-Teleport to Lobby", u -> {
				//アスレから退出させる
				u.exitCurrentParkour();

				Player player = u.asBukkitPlayer();

				//このテレポート処理は本番環境では変更する
				player.teleport(Bukkit.getWorld("world").getSpawnLocation());

				u.localizer.mcolor("&b-ロビーにテレポートしました | &b-Teleported to Lobby").displayOnActionBar(player);
			})
		);
	}

	private final User user;

	public MyProfileUI(User user){
		this.user = user;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Localizer localizer = user.localizer;
		Player player = user.asBukkitPlayer();

		return build(InventoryLine.x1, l -> {
			l.title = localizer.localize("プロフィール | My Profile");

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			//自分のステータス表示
			l.put((s) -> {
				//プレイヤーのスカルヘッドを作成する
				ItemStack skull = SkullCreator.fromPlayerUniqueId(user.uuid);

				s.icon(skull, i -> {
					i.displayName = StringTemplate.capply("&b-$0", player.getName());

					i.lore(
						localizer.applyAll("&7-: &b-Updateランク &7-@ &b-$0 | &7-: &b-Update Rank &7-@ &b-$0", user.updateRank()),
						localizer.applyAll("&7-: &b-Extendランク &7-@ &b-$0 | &7-: &b-Extend Rank &7-@ &b-$0", user.extendRank()),
						"",
						localizer.applyAll("&7-: &b-ジャンプ数 &7-@ &b-$0 | &7-: &b-Jumps &7-@ &b-$0", player.getStatistic(Statistic.JUMP)),
						localizer.applyAll("&7-: &b-所持コイン数 &7-@ &b-$0 | &7-: &b-Coins &7-@ &b-$0", user.coins()),
						localizer.applyAll("&7-: &b-総プレイ時間 &7-@ &b-$0h | &7-: &b-Time Played &7-@ &b-$0h", player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000)
					);

				});

			}, 1);

			for(Quadruple<Integer, Material, String, Consumer<User>> icon : ICONS){
				l.put(s -> {

					s.onClick(e -> icon.fourth.accept(user));

					s.icon(icon.second, i -> {
						i.displayName = localizer.color(icon.third);
						i.gleam();
					});

				}, icon.first);
			}

		});
	}

}
