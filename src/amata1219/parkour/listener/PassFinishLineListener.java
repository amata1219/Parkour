package amata1219.parkour.listener;

import java.util.Set;

import org.bukkit.entity.Player;

import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.Tweet;
import amata1219.parkour.function.ApplyRankToDisplayName;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.parkour.RankedParkour;
import amata1219.parkour.parkour.Records;
import amata1219.parkour.parkour.RankedParkour.RankedParkourType;
import amata1219.parkour.user.User;
import amata1219.parkour.util.TimeFormat;

public class PassFinishLineListener extends PassRegionBoundaryAbstractListener {

	public PassFinishLineListener() {
		super(Parkours.getInstance().chunksToFinishLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, ParkourRegion from, ParkourRegion to) {
		//アスレをゴールしたのでなければ戻る
		if(user.parkourPlayingNow == null || from != null || to == null) return;

		//アスレ名を取得する
		String parkourName = parkour.name;

		//タイムアタックが有効かどうか
		boolean enableTimeAttack = parkour.enableTimeAttack;

		//クリア済みのアスレ名リストを取得する
		Set<String> clearedParkourNames = user.clearedParkourNames;

		//クリアした事があるかどうか
		boolean haveCleared = clearedParkourNames.contains(parkourName);

		//クリア済みのアスレとして記録する(コレクションにはSetを用いているため要素の重複は起こらない)
		user.clearedParkourNames.add(parkourName);

		String playerName = player.getName();

		//タイムアタックが有効の場合
		if(enableTimeAttack){
			//ゴールタイムを計算する
			long time = System.currentTimeMillis() - user.timeToStartPlaying;

			//タイムを削除する
			user.timeToStartPlaying = 0;

			Records records = parkour.records;

			//ゴールタイムを記録する
			records.record(user.uuid, time);

			//記録をソートする
			records.sort();

			//表示例: amata1219 cleared Update11 @ 00:01:23.231!
			MessageTemplate.capply("&b-$0 cleared $1 @ $2!", playerName, parkourName, TimeFormat.format(time)).broadcast();
		}else{
			//表示例: amata1219 cleared Update6!
			MessageTemplate.capply("&b-$0 cleared $1!", playerName, parkourName).broadcast();
		}

		user.exitParkour();

		//クリア回数に基づき報酬を取得する
		int coins = parkour.rewards.getReward(haveCleared ? 1 : 0);

		//報酬のコインを与える
		user.depositCoins(coins);

		//表示例: Gave amata1219 1000 coins as a reward!
		MessageTemplate.capply("&b-Gave $0 $1 coins as a reward!", playerName, coins).display(player);

		//ランクアップアスレの場合
		if(parkour instanceof RankedParkour){
			RankedParkour rankedParkour = (RankedParkour) parkour;
			RankedParkourType type = rankedParkour.type;
			int rank = rankedParkour.rank;

			//各タイプで分岐する
			switch(type){
			case UPDATE:
				//プレイヤーのランクの方が高ければ戻る
				if(user.getUpdateRank() >= rank) return;

				//ランクを更新する
				user.incrementUpdateRank();

				//表示名を更新する
				ApplyRankToDisplayName.apply(user);
				break;
			case EXTEND:
				//プレイヤーのランクの方が高ければ戻る
				if(user.getExtendRank() >= rank) return;

				//ランクを更新する
				user.incrementExtendRank();
				break;
			default:
				throw new NullPointerException("Ranked parkour type can not be null");
			}

			//表示例: Rank up @ amata1219's update rank is 7!
			MessageTemplate.capply("&b-&l-Rank up &7-@ &b-$0's $1 rank is $2!", playerName, type.toString().toLowerCase(), rank).broadcast();

			//ツイートリンクを表示する
			Tweet.display(player, StringTemplate.apply("$0を初クリアしました！", parkour.getColorlessName()));
		}
	}

}
