package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import amata1219.amalib.scoreboard.Scoreboard;
import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;

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
	public Parkour currentlyPlayingParkour;

	//プレイし始めた時間(ミリ秒)
	public long timeToStartPlaying;

	//各アスレのチェックポイント
	public final Map<String, List<Location>> checkPoints = new HashMap<>();

	//個人設定
	public final UserSetting setting;

	//クリア済みのアスレの名前リスト
	public final Set<String> clearedParkourNames;

	//アスレ製作者用の指定範囲を表現するオブジェクト
	public ParkourRegionSelector selector;

	//クリエイティブワールドでのチェックポイント
	public Location checkpointInCreativeWorld;

	//購入したヘッドのセット
	public final Set<UUID> purchasedHeads;

	public Scoreboard board;

	public User(Yaml yaml){
		//ファイル名に基づきUUIDを生成し代入する
		this.uuid = UUID.fromString(yaml.name);

		//Updateランクを取得する
		updateRank = yaml.getInt("Update rank");

		//Extendランクを取得する
		extendRank = yaml.getInt("Extend rank");

		//コイン数を取得する
		coins = yaml.getInt("Coins");

		ParkourSet parkourSet = Main.getParkourSet();

		//最後にいたアスレを取得する
		currentParkour = parkourSet.getParkour("Current parkour");

		//最後に遊んでいたアスレを取得する
		currentlyPlayingParkour = parkourSet.getParkour("Currently playing parkour");

		//最後にアスレをプレイし始めた時間を取得する
		timeToStartPlaying = yaml.getLong("Time to start playing");

		//個人設定はYamlに基づき生成する
		setting = new UserSetting(yaml);

		//クリア済みのアスレ名リストを取得してセットでラップする
		clearedParkourNames = new HashSet<>(yaml.getStringList("Cleared parkur names"));

		//クリエイティブワールドのチェックポイントデータを取得して分割する
		String[] components = yaml.getString("Checkpoint in creative world").split(",");

		//データを基に座標を作成する
		checkpointInCreativeWorld = new Location(Main.getCreativeWorld(), Double.parseDouble(components[0]),
				Double.parseDouble(components[1]), Double.parseDouble(components[2]),
				Float.parseFloat(components[3]), Float.parseFloat(components[4]));

		//購入済みのスカルのIDをUUIDに変換したリストを作成する
		purchasedHeads = yaml.getStringList("Purchased heads")
								.stream()
								.map(UUID::fromString)
								.collect(Collectors.toSet());

		//セクションが存在しなければ戻る
		if(!yaml.isConfigurationSection("Check points"))
			return;

		//セクションを取得する
		ConfigurationSection section = yaml.getConfigurationSection("Check points");

		//各アスレ名毎に処理する
		for(String parkourName : section.getKeys(false)){
			//アスレ名と対応したアスレを取得する
			Parkour parkour = parkourSet.getParkour(parkourName);

			//チェックポイントを文字列から座標に変換してリスト化する
			List<Location> points = section.getStringList(parkourName)
											.stream()
											.map(point -> point.split(","))
											.map(coordinates -> Arrays.stream(coordinates).mapToDouble(Double::parseDouble).toArray())
											.map(coordinates -> new Location(parkour.world, coordinates[0], coordinates[1], coordinates[2], (float) coordinates[3], (float) coordinates[4]))
											.collect(Collectors.toList());

			//エリア番号と結び付けてチェックポイントをセットする
			for(int number = 0; number < points.size(); number++)
				setCheckPoint(parkour, number, points.get(number));
		}
	}

	//numberは0から始まる
	public void setCheckPoint(Parkour parkour, int number, Location location){
		String parkourName = parkour.name;

		//パルクールに対応したチェックポイントリストを取得、存在しなければ新規作成する
		List<Location> points = checkPoints.containsKey(parkourName) ? checkPoints.get(parkourName) : checkPoints.put(parkourName, new ArrayList<>());

		if(points.size() >= number)
			//新しいチェックポイントであればそのまま追加
			points.add(location);
		else
			//既に存在しているチェックポイントであれば更新する
			points.set(number, location);
	}

	public Player asPlayer(){
		return Bukkit.getPlayer(uuid);
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

	public boolean isPlayingParkour(){
		return currentlyPlayingParkour != null;
	}

	public void createScoreboard(){
		Player player = asPlayer();

		//テキストと値を@区切りで連結して返す
		BiFunction<String, Object, String> combine = (text, value) -> StringTemplate.format("$0$2 $1@ $0$3", ChatColor.AQUA, ChatColor.GRAY, text, value.toString());
	}

	public void save(Yaml yaml){
		//Updateランクを記録する
		yaml.set("Update rank", updateRank);

		//Extendランクを取得する
		yaml.set("Extend rank", extendRank);

		//コイン数を記録する
		yaml.set("Coins", coins);

		//最後にいたアスレの名前を記録する
		yaml.set("Current parkour", currentParkour != null ? currentParkour.name : null);

		//最後にプレイしていたアスレの名前を記録する
		yaml.set("Currently playing parkour", currentlyPlayingParkour != null ? currentlyPlayingParkour.name : null);

		//最後にアスレをプレイし始めた時間を記録する
		yaml.set("Time to start playing", timeToStartPlaying);

		//他プレイヤーを表示するかどうかを記録する
		yaml.set("Hide users", setting.hideUsers);

		//クリア済みのアスレの名前リストを記録する
		yaml.set("Cleared parkour names", clearedParkourNames);

		//購入済みのスカルのIDを文字列に変換したリストを記録する
		yaml.set("Purchased skulls", purchasedHeads.stream()
													.map(UUID::toString)
													.collect(Collectors.toList()));

		//クリエイティブワールドのチェックポイントを記録する
		yaml.set("Checkpoint in creative world", StringTemplate.format("$0,$1,$2,$3,$4",
				checkpointInCreativeWorld.getX(), checkpointInCreativeWorld.getY(), checkpointInCreativeWorld.getZ(),
				checkpointInCreativeWorld.getYaw(), checkpointInCreativeWorld.getPitch()));

		//各チェックポイントを記録する
		for(Entry<String, List<Location>> entry : checkPoints.entrySet()){
			//座標を文字列に変換しリスト化する
			List<String> points = entry.getValue()
					.stream()
					.map(location -> StringTemplate.format("$0,$1,$2,$3,$4", location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()))
					.collect(Collectors.toList());

			//対応したアスレ名の階層にチェックポイントリストを記録する
			yaml.set(StringTemplate.format("Check points.$0", entry.getKey()), points);
		}

		//ファイルをセーブする
		yaml.save();
	}

}
