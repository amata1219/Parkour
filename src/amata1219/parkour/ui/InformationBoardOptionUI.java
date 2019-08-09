package amata1219.parkour.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.InventoryLine;
import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.tuplet.Quadruple;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSetting;

public class InformationBoardOptionUI implements InventoryUI {

	private final User user;

	//Function<値を反転させるかどうか, 値>
	private final ArrayList<Quadruple<Integer, Material, String, Function<Boolean, Boolean>>> components = new ArrayList<>();

	public InformationBoardOptionUI(User user){
		this.user = user;

		UserSetting setting = user.setting;

		components.addAll(Arrays.asList(
			makeComponent(0, Material.SIGN, "Scoreboard", (flag) -> flag ? (setting.displayScoreboard = !setting.displayScoreboard) : setting.displayScoreboard),
			makeComponent(18, Material.NAME_TAG, "Traceur", (flag) -> flag ? (setting.displayTraceur = !setting.displayTraceur) : setting.displayTraceur),
			makeComponent(2, Material.GOLDEN_BOOTS, "Update Rank", (flag) -> flag ? (setting.displayUpdateRank = !setting.displayUpdateRank) : setting.displayUpdateRank),
			makeComponent(4, Material.IRON_BOOTS, "Extend Rank", (flag) -> flag ? (setting.displayExtendRank = !setting.displayExtendRank) : setting.displayExtendRank),
			makeComponent(6, Material.RABBIT_FOOT, "Jumps", (flag) -> flag ? (setting.displayJumps = !setting.displayJumps) : setting.displayJumps),
			makeComponent(8, Material.GOLD_INGOT, "Coins", (flag) -> flag ? (setting.displayCoins = !setting.displayCoins) : setting.displayCoins),
			makeComponent(22, Material.CLOCK, "Time Played", (flag) -> flag ? (setting.displayTimePlayed = !setting.displayTimePlayed) : setting.displayTimePlayed),
			makeComponent(20, Material.CHAINMAIL_HELMET, "Online Players", (flag) -> flag ? (setting.displayOnlinePlayers = !setting.displayOnlinePlayers) : setting.displayOnlinePlayers),
			makeComponent(24, Material.COMPASS, "Ping", (flag) -> flag ? (setting.displayPing = !setting.displayPing) : setting.displayPing),
			makeComponent(26, Material.STRUCTURE_BLOCK, "Server Address", (flag) -> flag ? (setting.displayServerAddress = !setting.displayServerAddress) : setting.displayServerAddress)
		));
	}

	private Quadruple<Integer, Material, String, Function<Boolean, Boolean>> makeComponent(int slotIndex, Material material, String displayName, Function<Boolean, Boolean> state){
		return new Quadruple<>(slotIndex, material, displayName, state);
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		return build(InventoryLine.x3, (l) -> {
			//デフォルトスロットを設定する
			l.defaultSlot((s) -> {

				s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, (i) -> {
					i.displayName = " ";
				});

			});

			l.onClose((event) -> {
				user.getInformationBoard().loadScoreboard();
			});

			for(Quadruple<Integer, Material, String, Function<Boolean, Boolean>> component : components){
				Function<Boolean, Boolean> state = component.fourth;

				l.put((s) -> {
					s.icon(component.second, (i) -> {
						applyState(i, component.third, state.apply(false));
					});

					s.onClick((event) -> {
						Icon icon = event.currentIcon;

						//設定を反転させる
						state.apply(true);

						applyState(icon, component.third, state.apply(false));
					});

				}, component.first);
			}

		});
	}

	private void applyState(Icon icon, String name, boolean state){
		//表示する設定の場合
		if(state){
			icon.displayName = StringTemplate.capply("&b-$0", name);
			icon.gleam();

		//表示しない設定の場合
		}else{
			icon.displayName = StringTemplate.capply("&7-$0", name);
			icon.tarnish();
		}
	}

}
