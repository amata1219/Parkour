package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import amata1219.amalib.scoreboard.Scoreboard;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.tuplet.Quadruple;

public class UserScoreboard {

	private final User user;
	private Scoreboard board;

	//スコアボード用の情報群
	private final List<Quadruple<Supplier<Boolean>, Integer, String, Supplier<Object>>> components = new ArrayList<>(9);

	public UserScoreboard(User user){
		this.user = user;

		UserSetting setting = user.setting;

		//スコアボード用の情報群を詰め込む
		components.addAll(Arrays.asList(
			makeComponent(() -> setting.displayTraceur, 8, "Traceur", () -> user.asBukkitPlayer().getName()),
			makeComponent(() -> setting.displayUpdateRank, 7, "Update Rank", () -> user.updateRank),
			makeComponent(() -> setting.displayExtendRank, 6, "Extend Rank", () -> user.extendRank),
			makeComponent(() -> setting.displayJumps, 5, "Jumps", () -> user.asBukkitPlayer().getStatistic(Statistic.JUMP)),
			makeComponent(() -> setting.displayCoins, 4, "Coins", () -> user.getCoins()),
			makeComponent(() -> setting.displayTimePlayed, 3, "Time Played", () -> user.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE)),
			makeComponent(() -> setting.displayOnlinePlayers, 2, "Online Players", () -> Bukkit.getOnlinePlayers().size()),
			makeComponent(() -> setting.displayPing, 1, "Ping", () -> ((CraftPlayer) user.asBukkitPlayer()).getHandle().ping),
			makeComponent(() -> setting.displayServerAddress, 0, "Server Address", () -> Bukkit.getIp())
		));
	}

	private Quadruple<Supplier<Boolean>, Integer, String, Supplier<Object>> makeComponent(Supplier<Boolean> display, int score, String valueName, Supplier<Object> value){
		return new Quadruple<>(display, score, valueName, value);
	}

	public void loadScoreboard(){
		//プレイヤーを取得する
		Player player = user.asBukkitPlayer();

		//スコアボードを表示しない設定の場合
		if(user.setting.displayScoreboard){
			//スコアボードが表示されていれば非表示にする
			if(player.getScoreboard() != null)
				player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

			return;
		}

		//スコアボードを新しく作成する
		board = new Scoreboard(player, StringTemplate.apply("$0$1A$2zisaba $1N$2etwork", ChatColor.BOLD, ChatColor.BLUE, ChatColor.AQUA));

		for(Quadruple<Supplier<Boolean>, Integer, String, Supplier<Object>> component : components){
			//表示するかどうか
			Supplier<Boolean> display = component.first;

			//どこに表示するか
			int score = component.second;

			//表示名
			String valueName = component.third;

			//表示する値
			Supplier<Object> value = component.fourth;

			//無効であれば繰り返す
			if(!display.get())
				continue;

			//情報名と値を@で連結したテキスト(表示例: Jumps @ 100)
			String text = StringTemplate.apply("$0$2 $1@ $0$3", ChatColor.AQUA, ChatColor.GRAY, valueName, value.get());

			//指定されたスコアにテキストをセットする
			board.setScore(score, text);
		}

		board.setDisplay(true);
	}

	public void updateUpdateRank(){
		updateValue(7);
	}

	public void updateExtendRank(){
		updateValue(6);
	}

	public void updateJumps(){
		updateValue(5);
	}

	public void updateCoins(){
		updateValue(4);
	}

	public void updateTimePlayed(){
		updateValue(3);
	}

	public void updateOnlinePlayers(){
		updateValue(2);
	}

	public void updatePing(){
		updateValue(1);
	}

	private void updateValue(int componentIndex){
		Quadruple<Supplier<Boolean>, Integer, String, Supplier<Object>> component = components.get(componentIndex);

		//表示するかどうか
		Supplier<Boolean> display = component.first;

		//どこに表示するか
		int score = component.second;

		//表示名
		String valueName = component.third;

		//表示する値
		Supplier<Object> value = component.fourth;

		//無効であれば戻る
		if(!display.get())
			return;

		//情報名と値を@で連結したテキスト(表示例: Jumps @ 100)
		String text = StringTemplate.applyWithColor("&b-$0 &7-@ &b-$1", valueName, value.get());

		//指定されたスコアをアップデートする
		board.updateScore(score, text);
	}

}
