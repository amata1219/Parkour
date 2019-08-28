package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

import amata1219.parkour.scoreboard.Scoreboard;
import amata1219.parkour.text.BilingualText;
import amata1219.parkour.text.Text;
import amata1219.parkour.tuplet.Quintuple;

public class StatusBoard {

	private static final ArrayList<Quintuple<Function<StatusBoardSetting, Boolean>, Integer, String, String, Function<User, Object>>> LINES = new ArrayList<>(12);
	private static final Pattern DOUBLE_BYTE_CHARACTER_CHECKER = Pattern.compile("^[^!-~｡-ﾟ]+$");

	@SafeVarargs
	private static void initialize(Quintuple<Function<StatusBoardSetting, Boolean>, Integer, String, String, Function<User, Object>>... components){
		Arrays.stream(components).forEach(LINES::add);
	}

	static{
		initialize(
			/*new Quintuple<>(s -> true, 11, "  ", " ", u -> ""),
			new Quintuple<>(s -> true, 10, "  ", " ", u -> ""),
			new Quintuple<>(s -> s.displayTraceur, 9, "&b-トレイサー &7-@ &f-$value", "&b-Traceur &7-@ &f-$value", u -> u.asBukkitPlayer().getName()),
			new Quintuple<>(s -> s.displayUpdateRank, 8, "&b-Updateランク &7-@ &f-$value", "&b-Update Rank &7-@ &f-$value", u -> u.getUpdateRank()),
			new Quintuple<>(s -> s.displayExtendRank, 7, "&b-Extendランク &7-@ &f-$value", "&b-Extend Rank &7-@ &f-$value", u -> u.getExtendRank()),
			new Quintuple<>(s -> s.displayJumps, 6, "&b-ジャンプ数 &7-@ &f-$value", "&b-Jumps &7-@ &f-$value", u -> u.asBukkitPlayer().getStatistic(Statistic.JUMP)),
			new Quintuple<>(s -> s.displayCoins, 5, "&b-所持コイン数 &7-@ &f-$value", "&b-Coins &7-@ &f-$value", u -> u.getCoins()),
			new Quintuple<>(s -> s.displayTimePlayed, 4, "&b-総プレイ時間 &7-@ &f-$valueh", "&b-Time Played &7-@ &f-$valueh", u -> u.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000),
			new Quintuple<>(s -> s.displayOnlinePlayers, 3, "&b-接続プレイヤー数 &7-@ &f-$value", "&b-Online Players &7-@ &f-$value", u -> Bukkit.getOnlinePlayers().size()),
			new Quintuple<>(s -> s.displayPing, 2, "&b-遅延 &7-@ &f-$valuems", "&b-Ping &7-@ &f-$valuems", u -> ((CraftPlayer) u.asBukkitPlayer()).getHandle().ping),
			new Quintuple<>(s -> true, 1, " ", " ", u -> ""),
			new Quintuple<>(s -> s.displayServerAddress, 0, "$value", "$value", u -> {

			new Quintuple<>(s -> true, 11, "  ", " ", u -> ""),
			new Quintuple<>(s -> true, 10, "  ", " ", u -> ""),
			new Quintuple<>(s -> s.displayTraceur, 9, "&b-トレイサー&7-::-&7-<-&f-$value-&7->", "&b-Traceur&7-::-&7-<-&f-$value-&7->", u -> u.asBukkitPlayer().getName()),
			new Quintuple<>(s -> s.displayUpdateRank, 8, "&b-Updateランク&7-::-&7-<-&f-$value-&7->", "&b-Update Rank&7-::-&7-<-&f-$value-&7->", u -> u.getUpdateRank()),
			new Quintuple<>(s -> s.displayExtendRank, 7, "&b-Extendランク&7-::-&7-<-&f-$value-&7->", "&b-Extend Rank&7-::-&7-<-&f-$value-&7->", u -> u.getExtendRank()),
			new Quintuple<>(s -> s.displayJumps, 6, "&b-ジャンプ数&7-::-&7-<-&f-$value-&7->", "&b-Jumps&7-::-&7-<-&f-$value-&7->", u -> u.asBukkitPlayer().getStatistic(Statistic.JUMP)),
			new Quintuple<>(s -> s.displayCoins, 5, "&b-所持コイン数&7-::-&7-<-&f-$value-&7->", "&b-Coins&7-::-&7-<-&f-$value-&7->", u -> u.getCoins()),
			new Quintuple<>(s -> s.displayTimePlayed, 4, "&b-総プレイ時間&7-::-&7-<-&f-$valueh-&7->", "&b-Time Played&7-::-&7-<-&f-$value-&7->h", u -> u.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000),
			new Quintuple<>(s -> s.displayOnlinePlayers, 3, "&b-接続プレイヤー数&7-::-&7-<-&f-$value-&7->", "&b-Online Players&7-::-&7-<-&f-$value-&7->", u -> Bukkit.getOnlinePlayers().size()),
			new Quintuple<>(s -> s.displayPing, 2, "&b-遅延&7-::-&7-<-&f-$valuems-&7->", "&b-Ping&7-::-&7-<-&f-$value-&7->ms", u -> ((CraftPlayer) u.asBukkitPlayer()).getHandle().ping),
			new Quintuple<>(s -> true, 1, " ", " ", u -> ""),
			new Quintuple<>(s -> s.displayServerAddress, 0, "$value", "$value", u -> {

			new Quintuple<>(s -> true, 11, "  ", " ", u -> ""),
			new Quintuple<>(s -> true, 10, "  ", " ", u -> ""),
			new Quintuple<>(s -> s.displayTraceur, 9, "&b-トレイサー&7-: &7-<-&f-$value-&7->", "&b-Traceur&7-: &7-<-&f-$value-&7->", u -> u.asBukkitPlayer().getName()),
			new Quintuple<>(s -> s.displayUpdateRank, 8, "&b-Updateランク&7-: &7-<-&f-$value-&7->", "&b-Update Rank&7-: &7-<-&f-$value-&7->", u -> u.getUpdateRank()),
			new Quintuple<>(s -> s.displayExtendRank, 7, "&b-Extendランク&7-: &7-<-&f-$value-&7->", "&b-Extend Rank&7-: &7-<-&f-$value-&7->", u -> u.getExtendRank()),
			new Quintuple<>(s -> s.displayJumps, 6, "&b-ジャンプ数&7-: &7-<-&f-$value-&7->", "&b-Jumps&7-: &7-<-&f-$value-&7->", u -> u.asBukkitPlayer().getStatistic(Statistic.JUMP)),
			new Quintuple<>(s -> s.displayCoins, 5, "&b-所持コイン数&7-: &7-<-&f-$value-&7->", "&b-Coins&7-: &7-<-&f-$value-&7->", u -> u.getCoins()),
			new Quintuple<>(s -> s.displayTimePlayed, 4, "&b-総プレイ時間&7-: &7-<-&f-$valueh-&7->", "&b-Time Played&7-: &7-<-&f-$value-&7->h", u -> u.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000),
			new Quintuple<>(s -> s.displayOnlinePlayers, 3, "&b-接続プレイヤー数&7-: &7-<-&f-$value-&7->", "&b-Online Players&7-: &7-<-&f-$value-&7->", u -> Bukkit.getOnlinePlayers().size()),
			new Quintuple<>(s -> s.displayPing, 2, "&b-遅延&7-: &7-<-&f-$valuems-&7->", "&b-Ping&7-: &7-<-&f-$value-&7->ms", u -> ((CraftPlayer) u.asBukkitPlayer()).getHandle().ping),
			new Quintuple<>(s -> true, 1, " ", " ", u -> ""),
			new Quintuple<>(s -> s.displayServerAddress, 0, "$value", "$value", u -> {

			new Quintuple<>(s -> true, 11, "  ", " ", u -> ""),
			new Quintuple<>(s -> true, 10, "  ", " ", u -> ""),
			new Quintuple<>(s -> s.displayTraceur, 9, "&b-トレイサー &7-@ &7-<-&f-$value-&7->", "&b-Traceur &7-@ &7-<-&f-$value-&7->", u -> u.asBukkitPlayer().getName()),
			new Quintuple<>(s -> s.displayUpdateRank, 8, "&b-Updateランク &7-@ &7-<-&f-$value-&7->", "&b-Update Rank &7-@ &7-<-&f-$value-&7->", u -> u.getUpdateRank()),
			new Quintuple<>(s -> s.displayExtendRank, 7, "&b-Extendランク &7-@ &7-<-&f-$value-&7->", "&b-Extend Rank &7-@ &7-<-&f-$value-&7->", u -> u.getExtendRank()),
			new Quintuple<>(s -> s.displayJumps, 6, "&b-ジャンプ数 &7-@ &7-<-&f-$value-&7->", "&b-Jumps &7-@ &7-<-&f-$value-&7->", u -> u.asBukkitPlayer().getStatistic(Statistic.JUMP)),
			new Quintuple<>(s -> s.displayCoins, 5, "&b-所持コイン数 &7-@ &7-<-&f-$value-&7->", "&b-Coins &7-@ &7-<-&f-$value-&7->", u -> u.getCoins()),
			new Quintuple<>(s -> s.displayTimePlayed, 4, "&b-総プレイ時間 &7-@ &7-<-&f-$valueh-&7->", "&b-Time Played &7-@ &7-<-&f-$value-&7->h", u -> u.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000),
			new Quintuple<>(s -> s.displayOnlinePlayers, 3, "&b-接続プレイヤー数 &7-@ &7-<-&f-$value-&7->", "&b-Online Players &7-@ &7-<-&f-$value-&7->", u -> Bukkit.getOnlinePlayers().size()),
			new Quintuple<>(s -> s.displayPing, 2, "&b-遅延 &7-@ &7-<-&f-$valuems-&7->", "&b-Ping &7-@ &7-<-&f-$value-&7->ms", u -> ((CraftPlayer) u.asBukkitPlayer()).getHandle().ping),
			new Quintuple<>(s -> true, 1, " ", " ", u -> ""),
			new Quintuple<>(s -> s.displayServerAddress, 0, "$value", "$value", u -> {

			new Quintuple<>(s -> true, 11, "  ", " ", u -> ""),
            new Quintuple<>(s -> true, 10, "  ", " ", u -> ""),
            new Quintuple<>(s -> s.displayTraceur, 9, "&b-トレイサー &7-@ &f-$value", "&b-Traceur &7-@ &f-$value", u -> u.asBukkitPlayer().getName()),
            new Quintuple<>(s -> s.displayUpdateRank, 8, "&b-Updateランク &7-@ &f-$value", "&b-Update Rank &7-@ &f-$value", u -> u.getUpdateRank()),
            new Quintuple<>(s -> s.displayExtendRank, 7, "&b-Extendランク &7-@ &f-$value", "&b-Extend Rank &7-@ &f-$value", u -> u.getExtendRank()),
            new Quintuple<>(s -> s.displayJumps, 6, "&b-ジャンプ数 &7-@ &f-$value", "&b-Jumps &7-@ &f-$value", u -> u.asBukkitPlayer().getStatistic(Statistic.JUMP)),
            new Quintuple<>(s -> s.displayCoins, 5, "&b-所持コイン数 &7-@ &f-$value", "&b-Coins &7-@ &f-$value", u -> u.getCoins()),
            new Quintuple<>(s -> s.displayTimePlayed, 4, "&b-総プレイ時間 &7-@ &f-$valueh", "&b-Time Played &7-@ &f-$valueh", u -> u.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000),
            new Quintuple<>(s -> s.displayOnlinePlayers, 3, "&b-接続プレイヤー数 &7-@ &f-$value", "&b-Online Players &7-@ &f-$value", u -> Bukkit.getOnlinePlayers().size()),
            new Quintuple<>(s -> s.displayPing, 2, "&b-遅延 &7-@ &f-$valuems", "&b-Ping &7-@ &f-$valuems", u -> ((CraftPlayer) u.asBukkitPlayer()).getHandle().ping),
            new Quintuple<>(s -> true, 1, " ", " ", u -> ""),
            new Quintuple<>(s -> s.displayServerAddress, 0, "$value", "$value", u -> {*/

			new Quintuple<>(s -> true, 10, " ", " ", u -> ""),
			new Quintuple<>(s -> s.displayTraceur, 9, "&b-トレイサー&7-: &f-$value", "&b-Traceur&7-: &f-$value", u -> u.asBukkitPlayer().getName()),
			new Quintuple<>(s -> s.displayUpdateRank, 8, "&b-Updateランク&7-: &f-$value", "&b-Update Rank&7-: &f-$value", u -> u.updateRank()),
			new Quintuple<>(s -> s.displayExtendRank, 7, "&b-Extendランク&7-: &f-$value", "&b-Extend Rank&7-: &f-$value", u -> u.extendRank()),
			new Quintuple<>(s -> s.displayJumps, 6, "&b-ジャンプ数&7-: &f-$value", "&b-Jumps&7-: &f-$value", u -> u.asBukkitPlayer().getStatistic(Statistic.JUMP)),
			new Quintuple<>(s -> s.displayCoins, 5, "&b-所持コイン数&7-: &f-$value", "&b-Coins&7-: &f-$value", u -> u.coins()),
			new Quintuple<>(s -> s.displayTimePlayed, 4, "&b-総プレイ時間&7-: &f-$valueh", "&b-Time Played&7-: &f-$valueh", u -> u.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000),
			new Quintuple<>(s -> s.displayOnlinePlayers, 3, "&b-接続プレイヤー数&7-: &f-$value", "&b-Online Players&7-: &f-$value", u -> Bukkit.getOnlinePlayers().size()),
			new Quintuple<>(s -> s.displayPing, 2, "&b-遅延&7-: &f-$valuems", "&b-Ping&7-: &f-$valuems", u -> ((CraftPlayer) u.asBukkitPlayer()).getHandle().ping),
			new Quintuple<>(s -> true, 1, "", "", u -> ""),
			new Quintuple<>(s -> s.displayServerAddress, 0, "$value", "$value", u -> {
				Scoreboard board = u.statusBoard.board;

				int maxLength = 0;

				for(int score = 2; score <= 9; score++) if(board.hasScore(score)){
					String text = ChatColor.stripColor(board.getScore(score));

					double length = 0;

					//全角文字であれば2.5、そうでなければ1加算する
					for(char character : text.toCharArray()) length += DOUBLE_BYTE_CHARACTER_CHECKER.matcher(String.valueOf(character)).matches() ? 2.5 : 1;

					maxLength = Math.max((int) length, maxLength);
				}

				//azisaba.netの文字数分だけ引く
				maxLength = Math.max(maxLength - 11, 0);

				int halfMaxLength = maxLength / 2;

				String spaces = "";
				for(int i = 0; i < halfMaxLength; i++) spaces += " ";

				return Text.stream("$spaces-&b-azisaba.net").setAttribute("$spaces", spaces).color().toString();
			})
		);
	}

	private final User user;
	private Scoreboard board;

	public StatusBoard(User user){
		this.user = user;
	}

	public void loadScoreboard(){
		StatusBoardSetting setting = user.setting;

		//スコアボードを表示しない設定であれば戻る
		if(!setting.displayScoreboard){
			//スコアボードが表示されていれば非表示にする
			if(board != null && board.isDisplay()) board.setDisplay(false);

			return;
		}

		//スコアボードを新しく作成する
		board = new Scoreboard(user.asBukkitPlayer(), Text.stream("&9-&l-A-&r-&b-zisaba &9-&l-N-&r-&b-etwork").color().toString());

		for(Quintuple<Function<StatusBoardSetting, Boolean>, Integer, String, String, Function<User, Object>> line : LINES){
			//表示しない設定であれば処理しない
			if(!line.first.apply(setting)) continue;

			//表示するテキストを作成する
			String text = BilingualText.stream(line.third, line.fourth).setAttribute("$value", line.fifth.apply(user)).color().toString();

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
		for(int score = 0; score < LINES.size() - 1; score++) updateValue(score, false);
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
		updateValue(score, false);
	}

	private void updateValue(int score, boolean shouldUpdateServerAddress){
		if(board == null) return;

		Quintuple<Function<StatusBoardSetting, Boolean>, Integer, String, String, Function<User, Object>> line = LINES.get(score);

		StatusBoardSetting setting = user.setting;

		//表示しない設定であれば戻る
		if(!line.first.apply(setting)) return;

		//現在表示されている文字列を取得する
		String before = board.getScore(score);

		Localizer localizer = user.localizer;

		//表示する文字列を作成する
		String after = localizer.applyAll(line.third, line.fourth.apply(user));

		//指定されたスコアをアップデートする
		board.updateScore(line.second, after);

		//サーバーアドレスの表示を更新しないのであれば戻る
		if(!shouldUpdateServerAddress) return;

		//サーバーアドレス行のコンポーネントを取得する
		Quintuple<Function<StatusBoardSetting, Boolean>, Integer, String, String, Function<User, Object>> serverAddress = LINES.get(11);

		//サーバーアドレスを表示しない又は文字列長に差が無い場合は戻る
		if(!serverAddress.first.apply(setting) || (before != null && before.length() == after.length())) return;

		String serverAddressDisplayed = localizer.applyAll(serverAddress.third, serverAddress.fourth.apply(user));

		board.updateScore(serverAddress.second, serverAddressDisplayed);
	}

}
