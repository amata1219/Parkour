package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import amata1219.amalib.scoreboard.Scoreboard;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.tuplet.Quadruple;

public class InformationBoard {

	private final User user;
	private Scoreboard board;

	//スコアボード用の情報群
	private final List<Quadruple<Supplier<Boolean>, Integer, String, Supplier<Object>>> components = new ArrayList<>(9);

	public InformationBoard(User user){
		this.user = user;

		UserSetting setting = user.setting;

		//スコアボード用の情報群を詰め込む
		components.addAll(Arrays.asList(
			makeComponent(() -> true, 10, "", () -> null),
			makeComponent(() -> setting.displayTraceur, 9, "&b-Traceur &7-@ &b-$0", () -> user.asBukkitPlayer().getName()),
			makeComponent(() -> setting.displayUpdateRank, 8, "&b-Update Rank &7-@ &b-$0", () -> user.updateRank),
			makeComponent(() -> setting.displayExtendRank, 7, "&b-Extend Rank &7-@ &b-$0", () -> user.extendRank),
			makeComponent(() -> setting.displayJumps, 6, "&b-Jumps &7-@ &b-$0", () -> user.asBukkitPlayer().getStatistic(Statistic.JUMP)),
			makeComponent(() -> setting.displayCoins, 5, "&b-Coins &7-@ &b-$0", () -> user.getCoins()),
			makeComponent(() -> setting.displayTimePlayed, 4, "&b-Time Played &7-@ &b-$0h", () -> user.asBukkitPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000),
			makeComponent(() -> setting.displayOnlinePlayers, 3, "&b-Online Players &7-@ &b-$0", () -> Bukkit.getOnlinePlayers().size()),
			makeComponent(() -> setting.displayPing, 2, "&b-Ping &7-@ &b-$0ms", () -> ((CraftPlayer) user.asBukkitPlayer()).getHandle().ping),
			makeComponent(() -> true, 1, "", () -> null),
			makeComponent(() -> setting.displayServerAddress, 0, "&b-$0", () -> Bukkit.getIp())
		));
	}

	private Quadruple<Supplier<Boolean>, Integer, String, Supplier<Object>> makeComponent(Supplier<Boolean> display, int score, String template, Supplier<Object> value){
		return new Quadruple<>(display, score, template, value);
	}

	public void loadScoreboard(){
		//プレイヤーを取得する
		Player player = user.asBukkitPlayer();

		//スコアボードを表示しない設定であれば戻る
		if(!user.setting.displayScoreboard) return;

		//スコアボードを新しく作成する
		board = new Scoreboard(player, StringColor.color("&9-&l-A-&r-&b-zisaba &9-&l-N-&r-&b-zetwork"));

		for(Quadruple<Supplier<Boolean>, Integer, String, Supplier<Object>> component : components){
			//表示しなければ繰り返す
			if(!component.first.get())
				continue;

			//テキストを作成する
			String text = StringTemplate.capply(component.third, component.fourth.get());

			//指定されたスコアにテキストをセットする
			board.setScore(component.second, text);
		}

		board.setDisplay(true);
	}

	public void clearScoreboard(){
		if(board == null) return;

		board.setDisplay(false);

		board = null;
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
		if(board == null) return;

		Quadruple<Supplier<Boolean>, Integer, String, Supplier<Object>> component = components.get(componentIndex);

		//表示しなければ戻る
		if(!component.first.get())
			return;

		//テキストを作成する
		String text = StringTemplate.capply(component.third, component.fourth);

		//指定されたスコアをアップデートする
		board.updateScore(component.second, text);
	}

}
