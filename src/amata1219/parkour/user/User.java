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
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;

public class User {

	//UUID
	public final UUID uuid;

	//ランク(表示例: amata1219 @ 12)
	public int rank;

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

	public User(Yaml yaml){
		//ファイル名に基づきUUIDを生成し代入する
		this.uuid = UUID.fromString(yaml.name);

		rank = yaml.getInt("Rank");

		coins = yaml.getInt("Coins");

		ParkourSet parkourSet = Main.getParkourSet();

		currentParkour = parkourSet.getParkour("Current parkour");
		currentlyPlayingParkour = parkourSet.getParkour("Currently playing parkour");
		timeToStartPlaying = yaml.getLong("Time to start playing");

		Map<String, Parkour> parkourMap = Main.getParkourSet().parkourMap;

		//最終ログアウト時にプレイしていたアスレを現在プレイ中のステージとする
		currentlyPlayingParkour = parkourMap.get(yaml.getString("Last played parkour name"));

		//個人設定はYamlに基づき生成する
		setting = new UserSetting(yaml);

		clearedParkourNames = new HashSet<>(yaml.getStringList("Cleared parkur names"));

		String[] components = yaml.getString("Checkpoint in creative world").split(",");
		checkpointInCreativeWorld = new Location(Main.getCreativeWorld(), Double.parseDouble(components[0]),
				Double.parseDouble(components[1]), Double.parseDouble(components[2]),
				Float.parseFloat(components[3]), Float.parseFloat(components[4]));

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
			Parkour parkour = parkourMap.get(parkourName);

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

	public void save(Yaml yaml){
		yaml.set("Rank", rank);
		yaml.set("Coins", coins);
		yaml.set("Current parkour", currentParkour != null ? currentParkour.name : null);
		yaml.set("Currently playing parkour", currentlyPlayingParkour != null ? currentlyPlayingParkour.name : null);
		yaml.set("Time to start playing", timeToStartPlaying);

		yaml.set("Hide users", setting.hideUsers);

		yaml.set("Cleared parkour names", clearedParkourNames);

		yaml.set("Purchased heads", purchasedHeads.stream()
													.map(UUID::toString)
													.collect(Collectors.toList()));

		yaml.set("Checkpoint in creative world", StringTemplate.format("$0,$1,$2,$3,$4",
				checkpointInCreativeWorld.getX(), checkpointInCreativeWorld.getY(), checkpointInCreativeWorld.getZ(),
				checkpointInCreativeWorld.getYaw(), checkpointInCreativeWorld.getPitch()));

		for(Entry<String, List<Location>> entry : checkPoints.entrySet()){
			//座標を文字列に変換しリスト化する
			List<String> points = entry.getValue()
					.stream()
					.map(location -> StringTemplate.format("$0,$1,$2,$3,$4", location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()))
					.collect(Collectors.toList());

			//対応したアスレ名の階層にチェックポイントリストをセットする
			yaml.set(StringTemplate.format("Check points.$0", entry.getKey()), points);
		}

		yaml.update();
	}

}
