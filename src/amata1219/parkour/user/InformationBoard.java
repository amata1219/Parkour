package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import amata1219.amalib.scoreboard.Scoreboard;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.Localizer;
import amata1219.amalib.tuplet.Quadruple;

public class InformationBoard {

	private static final List<Quadruple<Function<UserSetting, Boolean>, Integer, Function<Localizer, String>, Function<User, Object>>> LINES = new ArrayList<>(12);

	static{
		initialize(
			new Quadruple<>(s -> true, 11, l -> "", u -> ""),
			new Quadruple<>(s -> true, 10, l -> "", u -> ""),
			new Quadruple<>(s -> s.displayTraceur, 9, l -> l.color("&b-Traceur &7-@ &f-$0"), u -> u.asBukkitPlayer().getName()),
			new Quadruple<>(s -> s.displayUpdateRank, 8, l -> l.color("&b-Update Rank &7-@ &f-$0"), u -> u.getUpdateRank()),
			new Quadruple<>(s -> s.displayExtendRank, 7, l -> l.color("&b-Extend Rank &7-@ &f-$0"), u -> u.getExtendRank()),
			new Quadruple<>(s -> s.displayJumps, 6, l -> l.color("&b-Jumps &7-@ &f-$0"), u -> u.asBukkitPlayer().getStatistic(Statistic.JUMP)),
			new Quadruple<>(s -> s.displayCoins, 5, l -> l.color("&b-Coins &7-@ &f-$0"), u -> u.getCoins()),
			new Quadruple<>(s -> s.displayTimePlayed, 4, l -> l.color("&b-Time Played &7-@ &f-$0h"), u -> u.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000),
			new Quadruple<>(s -> s.displayOnlinePlayers, 3, l -> l.color("&b-Online Players &7-@ &f-$0"), u -> Bukkit.getOnlinePlayers().size()),
			new Quadruple<>(s -> s.displayPing, 2, l -> l.color("&b-Ping &7-@ &f-$0ms"), u -> ((CraftPlayer) u.asBukkitPlayer()).getHandle().ping),
			new Quadruple<>(s -> true, 1, l -> "", u -> ""),
			new Quadruple<>(s -> s.displayServerAddress, 0, l -> l.color("&b-$0"), u -> "   azisaba.net")
		);
	}

	@SafeVarargs
	private static void initialize(Quadruple<Function<UserSetting, Boolean>, Integer, Function<Localizer, String>, Function<User, Object>>... components){
		Arrays.stream(components).forEach(LINES::add);
	}

	private final User user;
	private Scoreboard board;

	public InformationBoard(User user){
		this.user = user;
	}

	public void loadScoreboard(){
		UserSetting setting = user.setting;

		//スコアボードを表示しない設定であれば戻る
		if(!setting.displayScoreboard){
			//スコアボードが表示されていれば非表示にする
			if(board != null && board.isDisplay()) board.setDisplay(false);

			return;
		}

		//スコアボードを新しく作成する
		board = new Scoreboard(user.asBukkitPlayer(), StringColor.color("&9-&l-A-&r-&b-zisaba &9-&l-N-&r-&b-etwork"));

		for(Quadruple<Function<UserSetting, Boolean>, Integer, Function<Localizer, String>, Function<User, Object>> line : LINES){
			//表示しない設定であれば処理しない
			if(!line.first.apply(setting)) continue;

			//表示するテキストを作成する
			String text = StringTemplate.apply(line.third.apply(user.localizer), line.fourth.apply(user));

			//対応したスコアにテキストをセットする
			board.setScore(line.second.intValue(), text);
		}

		board.setDisplay(true);
	}

	public void clearScoreboard(){
		if(board == null) return;

		board.setDisplay(false);

		board = null;
	}

	public void updateUpdateRank(){
		updateValue(3);
	}

	public void updateExtendRank(){
		updateValue(4);
	}

	public void updateJumps(){
		updateValue(5);
	}

	public void updateCoins(){
		updateValue(6);
	}

	public void updateTimePlayed(){
		updateValue(7);
	}

	public void updateOnlinePlayers(){
		updateValue(8);
	}

	public void updatePing(){
		updateValue(9);
	}

	private void updateValue(int score){
		if(board == null) return;

		Quadruple<Function<UserSetting, Boolean>, Integer, Function<Localizer, String>, Function<User, Object>> line = LINES.get(score);

		//表示しない設定であれば戻る
		if(!line.first.apply(user.setting)) return;

		//表示するテキストを作成する
		String text = StringTemplate.apply(line.third.apply(user.localizer), line.fourth.apply(user));

		//指定されたスコアをアップデートする
		board.updateScore(line.second, text);
	}

}
