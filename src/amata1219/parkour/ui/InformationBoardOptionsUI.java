package amata1219.parkour.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.tuplet.Quadruple;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSetting;

public class InformationBoardOptionsUI implements InventoryUI {

	/*
	 * 各操作にサウンドを付ける
	 */

	private final User user;

	private final ArrayList<Quadruple<Integer, Material, String, Supplier<Boolean>>> components = new ArrayList<>();

	public InformationBoardOptionsUI(User user){
		this.user = user;

		UserSetting setting = user.setting;

		components.addAll(Arrays.asList(
			makeComponent(0, Material.SIGN, "Scoreboard", () -> setting.displayScoreboard = !setting.displayScoreboard),
			makeComponent(18, Material.NAME_TAG, "Traceur", () -> setting.displayTraceur = !setting.displayTraceur),
			makeComponent(2, Material.GOLDEN_BOOTS, "Update Rank", () -> setting.displayUpdateRank = !setting.displayUpdateRank),
			makeComponent(4, Material.IRON_BOOTS, "Extend Rank", () -> setting.displayExtendRank = !setting.displayExtendRank),
			makeComponent(6, Material.RABBIT_FOOT, "Jumps", () -> setting.displayJumps = !setting.displayJumps),
			makeComponent(8, Material.GOLD_INGOT, "Coins", () -> setting.displayCoins = !setting.displayCoins),
			makeComponent(22, Material.CLOCK, "Time Played", () -> setting.displayTimePlayed = !setting.displayTimePlayed),
			makeComponent(20, Material.CHAINMAIL_HELMET, "Online Players", () -> setting.displayOnlinePlayers = !setting.displayOnlinePlayers),
			makeComponent(24, Material.COMPASS, "Ping", () -> setting.displayPing = !setting.displayPing),
			makeComponent(26, Material.STRUCTURE_BLOCK, "Server Address", () -> setting.displayServerAddress = !setting.displayServerAddress)
		));
	}

	private Quadruple<Integer, Material, String, Supplier<Boolean>> makeComponent(int slotIndex, Material material, String displayName, Supplier<Boolean> state){
		return new Quadruple<>(slotIndex, material, displayName, state);
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		return build(InventoryLine.x3, (l) -> {
			l.asynchronouslyRunActionOnClose = true;

			l.onClose((event) -> {
				user.informationBoard.loadScoreboard();
				//音を再生する
			});

			for(Quadruple<Integer, Material, String, Supplier<Boolean>> component : components){
				int slotIndex = component.first;
				Material material = component.second;
				String displayName = component.third;
				Supplier<Boolean> state = component.fourth;

				l.put((s) -> {
					s.async = true;

					s.icon(material, (i) -> {
						i.displayName = StringTemplate.applyWithColor("&b-&l-$0", displayName);

						//値が変更されない様に先に反転させておく
						state.get();

						//値を元に戻し取得する
						if(state.get())
							i.gleam();
					});

					s.onClick((event) -> {
						Icon icon = event.currentIcon;

						//有効であれば発光させる
						if(state.get())
							icon.gleam();
						else
							icon.tarnish();
					});

				}, slotIndex);
			}
		});
	}

}
