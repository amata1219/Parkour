package amata1219.parkour.listener;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import amata1219.parkour.function.ImprintRank;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.parkour.RankUpParkour;
import amata1219.parkour.parkour.Records;
import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.user.StatusBoard;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;
import amata1219.parkour.util.TimeFormat;

public class FinishLinePassListener extends RegionBoundaryPassAbstractListener {

	private final UserSet users = UserSet.getInstnace();

	public FinishLinePassListener() {
		super(ParkourSet.getInstance().chunksToFinishLinesMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, ParkourRegion from, ParkourRegion to) {
		//アスレをゴールしたのでなければ戻る
		if(user.parkourPlayingNow == null || from != null || to == null) return;

		//アスレ名を取得する
		String parkourName = parkour.name;

		//タイムアタックが有効かどうか
		boolean enableTimeAttack = parkour.timeAttackEnable;

		//クリア済みのアスレ名リストを取得する
		Set<String> clearedParkourNames = user.clearedParkourNames;

		//クリアした事があるかどうか
		boolean haveCleared = clearedParkourNames.contains(parkourName);

		//クリア済みのアスレとして記録する(コレクションにはSetを用いているため要素の重複は起こらない)
		user.clearedParkourNames.add(parkourName);

		String playerName = player.getName();
		String colorlessParkourName = parkour.colorlessName();

		//タイムアタックが有効の場合
		if(enableTimeAttack){
			//ゴールタイムを計算する
			long time = System.currentTimeMillis() - user.startTime;

			//タイムを削除する
			user.startTime = 0;

			Records records = parkour.records;
			UUID uuid = user.uuid;

			long personalBest = records.personalBest(uuid);

			//ゴールタイムを記録してみてその結果を取得する
			boolean recorded = records.mightRecord(uuid, time);

			//自己最高記録を超えた場合
			if(personalBest > 0 && recorded){
				for(User onlineUser : users.getOnlineUsers()){
					onlineUser.localizer.mapplyAll("&b-&l-$0さんが$1を$2でクリアすると同時に自己最高記録の$3を超えました！ | &b-&l-$0 cleared $1 in $2 and beat $0's personal best of $4!",
							playerName, colorlessParkourName, TimeFormat.format(time), TimeFormat.format(personalBest))
							.display(onlineUser.asBukkitPlayer());
				}
			}else{
				for(User onlineUser : users.getOnlineUsers()){
					onlineUser.localizer.mapplyAll("&b-&l-$0さんが$1を$2でクリアしました！ | &b-&l-$0 cleared $1 in $2!",
							playerName, colorlessParkourName, TimeFormat.format(time))
							.display(onlineUser.asBukkitPlayer());
				}
			}

			records.sortAsync();
		}else{
			for(User onlineUser : users.getOnlineUsers()){
				onlineUser.localizer.mapplyAll("&b-&l-$0さんが$1をクリアしました！ | &b-&l-$0 cleared $1!", playerName, colorlessParkourName).display(onlineUser.asBukkitPlayer());
			}
		}

		user.parkourPlayingNow = null;

		Localizer localizer = user.localizer;

		//ランクアップアスレの場合
		if(parkour instanceof RankUpParkour){
			ParkourCategory category = parkour.category;

			//アスレのランクを取得する
			int rank = ((RankUpParkour) parkour).rank;

			StatusBoard board = user.statusBoard;

			//各タイプで分岐する
			switch(category){
			case UPDATE:
				//プレイヤーのランクの方が高ければ戻る
				if(user.updateRank() >= rank) return;

				//ランクを更新する
				user.incrementUpdateRank();

				//表示名を更新する
				ImprintRank.imprint(user);

				board.updateUpdateRank();
				break;
			case EXTEND:
				//プレイヤーのランクの方が高ければ戻る
				if(user.extendRank() >= rank) return;

				//ランクを更新する
				user.incrementExtendRank();

				board.updateExtendRank();
				break;
			default:
				throw new NullPointerException("Ranked parkour type can not be null");
			}

			for(User onlineUser : users.getOnlineUsers()){
				onlineUser.localizer.mapplyAll("&b-&l-$0さんの$1が$2に上がりました！ | &b-&l-$0's $1 rank went up to $2!", playerName, category.name, rank)
				.display(player);
			}

			//ツイートリンクを表示する
		}

		//クリア回数に基づき報酬を取得する
		int coins = parkour.rewards.getReward(haveCleared ? 1 : 0);

		//報酬のコインを与える
		user.depositCoins(coins);

		localizer.mapplyAll("&b-報酬として$0コインを与えました！ | &b-Gave you $0 coins as reward!", coins).display(player);
	}

}
