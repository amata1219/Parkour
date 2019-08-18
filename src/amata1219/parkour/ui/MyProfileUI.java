package amata1219.parkour.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.amalib.inventory.ui.InventoryLine;
import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.Localizer;
import amata1219.amalib.tuplet.Quadruple;
import amata1219.amalib.util.SkullMaker;
import amata1219.parkour.user.InventoryUIs;
import amata1219.parkour.user.User;

public class MyProfileUI implements InventoryUI {

	private final User user;
	private final Localizer localizer;
	private final ArrayList<Quadruple<Integer, Material, String, Runnable>> components = new ArrayList<>(3);

	public MyProfileUI(User user, InventoryUIs inventoryUIs){
		this.user = user;
		this.localizer = user.localizer;

		components.addAll(Arrays.asList(
			component(4, Material.SIGN, localizer.color("&b-スコアボードオプション | &b-?"), () -> inventoryUIs.openScoreboardOptionSelectionUI()),
			component(5, Material.GOLD_INGOT, localizer.color("&b-帽子を購入する | &b-?"), () -> inventoryUIs.openBuyHatUI()),
			component(6, Material.ARMOR_STAND, "&b-帽子を被る | &b-?", () -> inventoryUIs.openWearHatUI())
		));
	}

	private Quadruple<Integer, Material, String, Runnable> component(int slotIndex, Material material, String displayName, Runnable run){
		return new Quadruple<>(slotIndex, material, displayName, run);
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Player player = user.asBukkitPlayer();

		String playerName = player.getName();

		return build(InventoryLine.x1, l -> {
			l.title = localizer.localize("プロフィール | My Profile");

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			//自分のステータス表示
			l.put((s) -> {
				//プレイヤーのスカルヘッドを作成する
				ItemStack skull = SkullMaker.fromPlayerUniqueId(user.uuid);

				s.icon(skull, i -> {
					i.displayName = StringTemplate.capply("&b-$0", playerName);

					i.lore(
						StringTemplate.capply("&7-: &b-Update rank &7-@ &b-$0", user.getUpdateRank()),
						StringTemplate.capply("&7-: &b-Extend rank &7-@ &b-$0", user.getExtendRank()),
						"",
						StringTemplate.capply("&7-: &b-Jumps &7-@ &b-$0", player.getStatistic(Statistic.JUMP)),
						StringTemplate.capply("&7-: &b-Coins &7-@ &b-$0", user.getCoins()),
						StringTemplate.capply("&7-: &b-Time played &7-@ &b-$0h", player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000)
					);

				});

			}, 1);

			for(Quadruple<Integer, Material, String, Runnable> component : components){
				l.put((s) -> {

					s.onClick(event -> component.fourth.run());

					s.icon(component.second, i -> {
						i.displayName = component.third;
						i.gleam();
					});

				}, component.first);
			}

			l.put((s) -> {

				s.onClick((event) -> {
					Player clicker = event.player;

					//今いるアスレから退出する
					user.exitParkour();

					//本番環境では変える
					clicker.teleport(Bukkit.getWorld("world").getSpawnLocation());

					localizer.mcolor("&b-ロビーにテレポートしました | &b-Teleported to lobby").displayOnActionBar(player);
				});

				s.icon(Material.GRASS_BLOCK, i -> {
					i.displayName = localizer.color("&b-ロビーにテレポートする | &b-Teleport to lobby");
					i.gleam();
				});

			}, 7);

		});
	}

}
