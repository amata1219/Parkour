package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
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

	private static final ArrayList<Quadruple<Function<UserSetting, Boolean>, Integer, String, Function<User, Object>>> LINES = new ArrayList<>(12);

	@SafeVarargs
	private static void initialize(Quadruple<Function<UserSetting, Boolean>, Integer, String, Function<User, Object>>... components){
		Arrays.stream(components).forEach(LINES::add);
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
			new Quadruple<>(s -> s.displayPing, 2, "&b-遅延 &7-@ &f-$0ms | &b-Ping &7-@ &f-$0ms", u -> ((CraftPlayer) u.asBukkitPlayer()).getHandle().ping / 1.5),
			new Quadruple<>(s -> true, 1, " | ", u -> ""),
			new Quadruple<>(s -> s.displayServerAddress, 0, "$0 | $0", u -> {
				Scoreboard board = u.board.board;

				//全行の中で最大のバイト配列長
				int maxLength = 0;

				//最大のバイト配列長を探す
				for(int score = 2; score <= 9; score++) if(board.hasScore(score))
					maxLength = board.getScore(score).getBytes().length;

				//スコアとスペース分の3文字を追加
				maxLength += 3;

				int halfMaxLength = maxLength / 2;

				String spaces = "";
				for(int i = 0; i < halfMaxLength; i++) spaces += " ";

				return StringTemplate.capply("$0-&b-azisaba.net$1", spaces, maxLength % 2 == 0 ? spaces : spaces + " ");
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
		for(int score = 0; score < LINES.size() - 2; score++) updateValue(score);
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

		UserSetting setting = user.setting;

		//表示しない設定であれば戻る
		if(!line.first.apply(setting)) return;

		//現在表示されている文字列を取得する
		String before = board.getScore(score);

		Localizer localizer = user.localizer;

		//表示する文字列を作成する
		String after = localizer.applyAll(line.third, line.fourth.apply(user));

		//指定されたスコアをアップデートする
		board.updateScore(line.second, after);

		//サーバーアドレス行のコンポーネントを取得する
		Quadruple<Function<UserSetting, Boolean>, Integer, String, Function<User, Object>> serverAddress = LINES.get(11);

		//サーバーアドレスを表示しない又は文字列長に差が無い場合は戻る
		if(!serverAddress.first.apply(setting) || before.length() == after.length()) return;

		String serverAddressDisplayed = localizer.applyAll(serverAddress.third, serverAddress.fourth.apply(user));

		board.updateScore(serverAddress.second, serverAddressDisplayed);
	}

}
