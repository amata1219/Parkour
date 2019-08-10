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
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.MessageColor;
import amata1219.amalib.tuplet.Quadruple;
import amata1219.amalib.util.SkullMaker;
import amata1219.parkour.user.User;

public class MenuUI implements InventoryUI {

	private final User user;
	private final ArrayList<Quadruple<Integer, Material, String, InventoryUI>> components = new ArrayList<>(3);

	public MenuUI(User user){
		this.user = user;

		components.addAll(Arrays.asList(
			component(5, Material.FEATHER, StringColor.color("&b-Open scoreboard options"), new InformationBoardOptionUI(user)),
			component(6, Material.FEATHER, StringColor.color("&b-Open head menu"), new HeadMenuUI(user.heads))
		));
	}

	private Quadruple<Integer, Material, String, InventoryUI> component(int slotIndex, Material material, String displayName, InventoryUI inventoryUI){
		return new Quadruple<>(slotIndex, material, displayName, inventoryUI);
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Player player = user.asBukkitPlayer();

		String playerName = player.getName();

		return build(InventoryLine.x1, (l) -> {
			//表示例: amata1219's menu
			l.title = StringTemplate.capply("&b-$0's menu", playerName);

			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			//自分のステータス表示
			l.put((s) -> {
				//プレイヤーのスカルヘッドを作成する
				ItemStack skull = SkullMaker.fromPlayerUniqueId(user.uuid);

				s.icon(skull, (i) -> {
					i.displayName = StringTemplate.capply("&b-$0's state", playerName);

					i.lore(
						StringTemplate.capply("&7-: &b-Update rank &7-@ &b-$0", user.getUpdateRank()),
						StringTemplate.capply("&7-: &b-Extend rank &7-@ &b-$0", user.getExtendRank()),
						"",
						StringTemplate.capply("&7-: &b-Jmps &7-@ &b-$0", player.getStatistic(Statistic.JUMP)),
						StringTemplate.capply("&7-: &b-Coins &7-@ &b-$0", user.getCoins()),
						StringTemplate.capply("&7-: &b-Time played &7-@ &b-$0h", player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000)
					);

				});

			}, 1);

			for(Quadruple<Integer, Material, String, InventoryUI> component : components){
				l.put((s) -> {

					s.onClick(event -> component.fourth.openInventory(event.player));

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

					//表示例: Teleported to lobby!
					MessageColor.color("&b-Teleported to lobby!").displayOnActionBar(player);
				});

				s.icon(Material.FEATHER, i -> {
					i.displayName = StringColor.color("&b-Teleport to lobby");
					i.gleam();
				});

			}, 7);

		});
	}

}
