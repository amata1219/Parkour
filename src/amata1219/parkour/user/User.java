package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.parkour.Rank;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;

public class User {

	//UUID
	public final UUID uuid;

	//Updateのランク
	public int updateRank;

	//Extendのランク
	public int extendRank;

	//コイン
	private int coins;

	//現在いるアスレ
	public Parkour currentParkour;

	//現在プレイ中のアスレ
	public Parkour parkourPlayingNow;

	//プレイし始めた時間(ミリ秒)
	public long timeToStartPlaying;

	//各アスレのチェックポイント
	public final Map<String, List<ImmutableBlockLocation>> checkpoints = new HashMap<>();

	//個人設定
	public final UserSetting setting;

	//クリア済みのアスレの名前リスト
	public final Set<String> clearedParkourNames;

	//クリエイティブワールドでのチェックポイント
	public ImmutableBlockLocation creativeWorldCheckpoint;

	//購入したヘッドのセット
	public final Set<UUID> purchasedHeads;

	//スコアボードの管理インスタンス
	public final InformationBoard scoreboard;

	public User(Yaml yaml){
		//ファイル名に基づきUUIDを生成し代入する
		this.uuid = UUID.fromString(yaml.name);

		//Updateランクを取得する
		updateRank = yaml.getInt("Update rank");

		//Extendランクを取得する
		extendRank = yaml.getInt("Extend rank");

		//コイン数を取得する
		coins = yaml.getInt("Coins");

		ParkourSet parkourSet = ParkourSet.getInstance();

		//最後にいたアスレを取得する
		currentParkour = parkourSet.getParkour("Current parkour");

		//最後に遊んでいたアスレを取得する
		parkourPlayingNow = parkourSet.getParkour("Currently playing parkour");

		//最後にアスレをプレイし始めた時間を取得する
		timeToStartPlaying = yaml.getLong("Time to start playing");

		//個人設定はYamlに基づき生成する
		setting = new UserSetting(yaml);

		//クリア済みのアスレ名リストを取得してセットでラップする
		clearedParkourNames = new HashSet<>(yaml.getStringList("Cleared parkur names"));

		//データを基に座標を作成する
		creativeWorldCheckpoint = ImmutableBlockLocation.deserialize( yaml.getString("Creative world checkpoint"));

		//購入済みのスカルのIDをUUIDに変換したリストを作成する
		purchasedHeads = yaml.getStringList("Purchased skulls")
								.stream()
								.map(UUID::fromString)
								.collect(Collectors.toSet());

		//スコアボードの管理インスタンスを作成する
		scoreboard = new InformationBoard(this);

		//セクションが存在しなければ戻る
		if(yaml.isConfigurationSection("Check points")){
			//セクションを取得する
			ConfigurationSection section = yaml.getConfigurationSection("Check points");

			//各アスレ名毎に処理する
			for(String parkourName : section.getKeys(false)){
				//アスレ名と対応したアスレを取得する
				Parkour parkour = parkourSet.getParkour(parkourName);

				//チェックポイントを取得する
				List<ImmutableBlockLocation> points = section.getStringList(parkourName).stream().map(ImmutableBlockLocation::deserialize).collect(Collectors.toList());

				//エリア番号と結び付けてチェックポイントをセットする
				for(int checkAreaNumber = 0; checkAreaNumber < points.size(); checkAreaNumber++)
					setCheckpoint(parkour, checkAreaNumber, points.get(checkAreaNumber));
			}
		}
	}

	public void setCheckpoint(Parkour parkour, int checkAreaNumber, ImmutableBlockLocation location){
		String parkourName = parkour.name;

		//パルクールに対応したチェックポイントリストを取得、存在しなければ新規作成する
		List<ImmutableBlockLocation> points = checkpoints.containsKey(parkourName) ? checkpoints.get(parkourName) : checkpoints.put(parkourName, new ArrayList<>());

		if(points.size() >= checkAreaNumber)
			//新しいチェックポイントであればそのまま追加
			points.add(location);
		else
			//既に存在しているチェックポイントであれば更新する
			points.set(checkAreaNumber, location);
	}

	public void applyRankToPlayerName(){
		BetterNickAPI api = Main.getNickAPI();
		Player player = asBukkitPlayer();
		String name = StringTemplate.apply("$0$1@$2", Rank.values()[updateRank].color, player.getName(), updateRank);

		api.setPlayerDisplayName(player, name, "", "");
		api.setPlayerChatName(player, name, "", "");
		api.setPlayerTablistName(player, name, "", "");
	}

	public int getCoins(){
		return coins;
	}

	public void depositCoins(int coins){
		this.coins += coins;
	}

	public void withdrawCoins(int coins){
		this.coins = Math.max(this.coins - coins, 0);
	}

	public boolean isPlayignWithParkour(){
		return parkourPlayingNow != null;
	}

	public Player asBukkitPlayer(){
		return Bukkit.getPlayer(uuid);
	}

	public void save(){
		Yaml yaml = UserSet.getInstnace().getYaml(uuid);

		//Updateランクを記録する
		yaml.set("Update rank", updateRank);

		//Extendランクを取得する
		yaml.set("Extend rank", extendRank);

		//コイン数を記録する
		yaml.set("Coins", coins);

		//最後にいたアスレの名前を記録する
		yaml.set("Current parkour", currentParkour != null ? currentParkour.name : null);

		//最後にプレイしていたアスレの名前を記録する
		yaml.set("Currently playing parkour", parkourPlayingNow != null ? parkourPlayingNow.name : null);

		//最後にアスレをプレイし始めた時間を記録する
		yaml.set("Time to start playing", timeToStartPlaying);

		//クリア済みのアスレの名前リストを記録する
		yaml.set("Cleared parkour names", clearedParkourNames);

		//購入済みのスカルのIDを文字列に変換したリストを記録する
		yaml.set("Purchased skulls", purchasedHeads.stream().map(UUID::toString).collect(Collectors.toList()));

		//クリエイティブワールドのチェックポイントを記録する
		yaml.set("Creative world checkpoint", creativeWorldCheckpoint.serialize());

		//各チェックポイントを記録する
		for(Entry<String, List<ImmutableBlockLocation>> entry : checkpoints.entrySet()){
			//アスレ名を取得する
			String parkourName = entry.getKey();

			//座標を文字列に変換しリスト化する
			List<String> points = entry.getValue().stream().map(ImmutableBlockLocation::serialize).collect(Collectors.toList());

			//対応したアスレ名の階層にチェックポイントリストを記録する
			yaml.set(StringTemplate.apply("Check points.$0", parkourName), points);
		}

		//ファイルをセーブする
		yaml.save();
	}

}
