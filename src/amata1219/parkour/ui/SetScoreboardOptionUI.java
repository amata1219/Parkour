package amata1219.parkour.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.tuplet.Quadruple;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSetting;

public class SetScoreboardOptionUI implements InventoryUI {

	/*
	 * 各操作にサウンドを付ける
	 */

	private final User user;

	private final ArrayList<Quadruple<Integer, Material, String, Supplier<Boolean>>> components = new ArrayList<>();

	public SetScoreboardOptionUI(User user){
		this.user = user;

		UserSetting setting = user.setting;

		components.addAll(Arrays.asList(
			component(0, Material.SIGN, "Scoreboard", () -> setting.displayScoreboard = !setting.displayScoreboard),
			component(2, Material.GOLDEN_BOOTS, "Update Rank", () -> setting.displayUpdateRank = !setting.displayUpdateRank),
			component(4, Material.IRON_BOOTS, "Extend Rank", () -> setting.displayExtendRank = !setting.displayExtendRank),
			component(6, Material.RABBIT_FOOT, "Jumps", () -> setting.displayJumps = !setting.displayJumps),
			component(8, Material.GOLD_INGOT, "Coins", () -> setting.displayCoins = !setting.displayCoins),
			component(18, Material.NAME_TAG, "My Name", () -> setting.displayPlayerName = !setting.displayPlayerName),
			component(20, Material.CHAINMAIL_HELMET, "Online Players", () -> setting.displayOnlinePlayers = !setting.displayOnlinePlayers),
			component(22, Material.CLOCK, "Time Played", () -> setting.displayTimePlayed = !setting.displayTimePlayed),
			component(24, Material.COMPASS, "Ping", () -> setting.displayPing= !setting.displayPing)
		));
	}

	private Quadruple<Integer, Material, String, Supplier<Boolean>> component(int slotIndex, Material material, String displayName, Supplier<Boolean> state){
		return new Quadruple<>(slotIndex, material, displayName, state);
	}

	/*
	 * public boolean displayScoreboard; = sign
	public boolean displayUpdateRank; = golden boots
	public boolean displayExtendRank; = iron boots
	public boolean displayJumps; = rabbit leg
	public boolean displayCoins; = gold
	public boolean displayPlayerName; = name tag
	public boolean displayOnlinePlayers; = chain helmet
	public boolean displayTimePlayed; = clock
	public boolean displayPing; = compass
	 */

	@Override
	public Function<Player, InventoryLayout> layout() {
		return build(InventoryLine.x3, (l) -> {
			l.asynchronouslyRunActionOnClose = true;

			l.onClose((event) -> {
				//update scoreboard
			});

			for(Quadruple<Integer, Material, String, Supplier<Boolean>> component : components){
				int slotIndex = component.first;
				Material material = component.second;
				String displayName = component.third;
				Supplier<Boolean> state = component.fourth;

				l.put((s) -> {
					s.async = true;

					s.icon(material, (i) -> {
						i.displayName = StringTemplate.format("$0$1$2", ChatColor.AQUA, ChatColor.BOLD, displayName);

						//値が変更されない様に先に反転させておく
						state.get();

						//値を元に戻し取得する
						if(state.get())
							i.gleam();
					});

					s.onClick((event) -> {
						gleam(event.currentIcon, state.get());
					});
				}, slotIndex);
			}

			//最後の奇数スロットが空いているので初期化ボタンを作る
		});
	}

	private void gleam(Icon icon, boolean gleam){
		if(gleam)
			icon.gleam();
		else
			icon.tarnish();
	}

	/*
	 * o x o x o x o x o
	 * x x x x x x x x x
	 * o x o x o x o x o
	 */

}
