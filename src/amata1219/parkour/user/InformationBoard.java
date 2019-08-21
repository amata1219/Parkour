package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import amata1219.amalib.scoreboard.Scoreboard;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.tuplet.Quadruple;

public class InformationBoard {

	private static final ArrayList<Quadruple<Function<UserSetting, Boolean>, Integer, String, Function<User, Object>>> LINES = new ArrayList<>(12);

	@SafeVarargs
	private static void initialize(Quadruple<Function<UserSetting, Boolean>, Integer, String, Function<User, Object>>... components){
		Arrays.stream(components).forEach(LINES::add);
	}

	private static String getSpaces(int length){

	}

	static{
		initialize(
			new Quadruple<>(s -> true, 11, "  |  ", u -> ""),
			new Quadruple<>(s -> true, 10, "  |  ", u -> ""),
			new Quadruple<>(s -> s.displayTraceur, 9, "&b-トレイサー &7-@ &f-$0 | &b-Traceur &7-@ &f-$0", u -> u.asBukkitPlayer().getName()),
			new Quadruple<>(s -> s.displayUpdateRank, 8, "&b-Updateランク &7-@ &f-$0 | &b-Update Rank &7-@ &f-$0", u -> u.getUpdateRank()),
			new Quadruple<>(s -> s.displayExtendRank, 7, "&b-Extendランク &7-@ &f-$0 | &b-Extend Rank &7-@ &f-$0", u -> u.getExtendRank()),
			new Quadruple<>(s -> s.displayJumps, 6, "&b-ジャンプ数 &7-@ &f-$0 | &b-Jumps &7-@ &f-$0", u -> u.asBukkitPlayer().getStatistic(Statistic.JUMP)),
			new Quadruple<>(s -> s.displayCoins, 5, "&b-所持コイン数 &7-@ &f-$0 | &b-Coins &7-@ &f-$0", u -> u.getCoins()),
			new Quadruple<>(s -> s.displayTimePlayed, 4, "&b-総プレイ時間 &7-@ &f-$0h | &b-Time Played &7-@ &f-$0h", u -> u.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000),
			new Quadruple<>(s -> s.displayOnlinePlayers, 3, "&b-接続プレイヤー数 &7-@ &f-$0 | &b-Online Players &7-@ &f-$0", u -> Bukkit.getOnlinePlayers().size()),
			new Quadruple<>(s -> s.displayPing, 2, "&b-遅延 &7-@ &f-$0ms | &b-Ping &7-@ &f-$0ms", u -> ((CraftPlayer) u.asBukkitPlayer()).getHandle().ping),
			new Quadruple<>(s -> true, 1, " | ", u -> ""),
			new Quadruple<>(s -> s.displayServerAddress, 0, "&b-$0 | &b-$0", u -> {
				Scoreboard board = u.board.board;
				int maxLength = 0;
				for(int score = 0; score < 15; score++) if(board.hasScore(score))
					maxLength = board.getScore(score).length();

				int halfMaxLength = maxLength / 2;

				return maxLength % 2 == 0 ?
			})
		);
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

		for(Quadruple<Function<UserSetting, Boolean>, Integer, String, Function<User, Object>> line : LINES){
			//表示しない設定であれば処理しない
			if(!line.first.apply(setting)) continue;

			//何故か$0が空文字になる

			//表示するテキストを作成する
			String text = user.localizer.applyAll(line.third, line.fourth.apply(user));

			//対応したスコアにテキストをセットする
			board.setScore(line.second, text);
		}

		board.setDisplay(true);
	}

	public void clearScoreboard(){
		if(board == null) return;

		board.setDisplay(false);

		board = null;
	}

	public void updateAll(){
		for(int score = 0; score < LINES.size(); score++) updateValue(score);
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

		Quadruple<Function<UserSetting, Boolean>, Integer, String, Function<User, Object>> line = LINES.get(score);

		//表示しない設定であれば戻る
		if(!line.first.apply(user.setting)) return;

		//表示するテキストを作成する
		String text = user.localizer.applyAll(line.third, line.fourth.apply(user));

		//指定されたスコアをアップデートする
		board.updateScore(line.second, text);
	}

}
