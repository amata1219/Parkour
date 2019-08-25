package amata1219.parkour.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.parkour.inventory.ui.InventoryLine;
import amata1219.parkour.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.inventory.ui.dsl.component.Icon;
import amata1219.parkour.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.string.StringTemplate;
import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.tuplet.Quintuple;
import amata1219.parkour.user.User;
import amata1219.parkour.user.StatusBoardSetting;

public class ScoreboardDisplaySettingsUI implements InventoryUI {

	/*
	 * Integer first = スロット番号
	 * Material second = アイコンの見た目
	 * String third = 表示名
	 * Function<UserSetting, Boolean> fourth = 対応した値を取得する
	 * Consumer<UserSetting> fifth = 対応した値を反転させる
	 */
	private static final ArrayList<Quintuple<Integer, Material, String, Function<StatusBoardSetting, Boolean>, Consumer<StatusBoardSetting>>> ICONS = new ArrayList<>(10);

	@SafeVarargs
	private static void initialize(Quintuple<Integer, Material, String, Function<StatusBoardSetting, Boolean>, Consumer<StatusBoardSetting>>... components){
		Arrays.stream(components).forEach(ICONS::add);
	}

	static{
		initialize(
			new Quintuple<>(0, Material.SIGN, "スコアボード | Scoreboard", s -> s.displayScoreboard, s -> s.displayScoreboard = !s.displayScoreboard),
			new Quintuple<>(2, Material.SIGN, "Updateランク | Update Rank", s -> s.displayUpdateRank, s -> s.displayUpdateRank = !s.displayUpdateRank),
			new Quintuple<>(4, Material.SIGN, "Extendランク | Extend Rank", s -> s.displayExtendRank, s -> s.displayExtendRank = !s.displayExtendRank),
			new Quintuple<>(6, Material.SIGN, "ジャンプ数 | Jumps", s -> s.displayJumps, s -> s.displayJumps = !s.displayJumps),
			new Quintuple<>(8, Material.SIGN, "所持コイン数 | Coins", s -> s.displayCoins, s -> s.displayCoins = !s.displayCoins),
			new Quintuple<>(18, Material.SIGN, "トレイサー| Traceur", s -> s.displayTraceur, s -> s.displayTraceur = !s.displayTraceur),
			new Quintuple<>(20, Material.SIGN, "総プレイ時間 | Time Played", s -> s.displayTimePlayed, s -> s.displayTimePlayed = !s.displayTimePlayed),
			new Quintuple<>(22, Material.SIGN, "接続プレイヤー数 | Online Players", s -> s.displayOnlinePlayers, s -> s.displayOnlinePlayers = !s.displayOnlinePlayers),
			new Quintuple<>(24, Material.SIGN, "遅延 | Ping", s -> s.displayPing, s -> s.displayPing = !s.displayPing),
			new Quintuple<>(26, Material.SIGN, "サーバーアドレス | Server Address", s -> s.displayServerAddress, s -> s.displayServerAddress = !s.displayServerAddress)
		);
	}

	private final User user;
	private final StatusBoardSetting setting;

	public ScoreboardDisplaySettingsUI(User user){
		this.user = user;
		this.setting = user.setting;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		Localizer localizer = user.localizer;

		return build(InventoryLine.x3, l -> {
			l.title = localizer.localize("スコアボードの表示設定 | Scoreboard display settings");

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			l.onClose(e -> user.statusBoard.loadScoreboard());

			for(Quintuple<Integer, Material, String, Function<StatusBoardSetting, Boolean>, Consumer<StatusBoardSetting>> icon : ICONS){
				String iconName = icon.third;
				Function<StatusBoardSetting, Boolean> state = icon.fourth;

				l.put(s -> {
					s.icon(icon.second, i -> applyDisplaySettingToIcon(i, iconName, state.apply(setting)));

					s.onClick(e -> {
						//表示設定を反転させる
						icon.fifth.accept(setting);

						applyDisplaySettingToIcon(e.currentIcon, iconName, state.apply(setting));
					});
				}, icon.first);

			}
		});
	}

	private void applyDisplaySettingToIcon(Icon icon, String iconName, boolean state){
		//表示する場合
		if(state){
			icon.displayName = StringTemplate.capply("&b-$0", iconName);
			icon.gleam();
		}else{
			icon.displayName = StringTemplate.capply("&7-$0", iconName);
			icon.tarnish();
		}
	}

}
