package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class User {

	//UUID
	public final UUID uuid;

	//ランク
	public int rank;

	//コイン
	private int coins;

	//現在プレイ中のステージ
	public Parkour currentlyPlayingParkour;

	//プレイし始めた時間(ミリ秒)
	public long timeToStartPlaying;

	//各アスレのチェックポイント
	public final Map<String, List<Location>> checkPoints = new HashMap<>();

	//個人設定
	public final UserSetting setting;

	public User(Yaml yaml){
		//ファイル名に基づきUUIDを生成し代入する
		this.uuid = UUID.fromString(yaml.name);

		rank = yaml.getInt("Rank");

		coins = yaml.getInt("Coins");

		Map<String, Parkour> parkourMap = Main.getParkourSet().parkourMap;

		//最終ログアウト時にプレイしていたアスレを現在プレイ中のステージとする
		currentlyPlayingParkour = parkourMap.get(yaml.getString("Last played parkour name"));

		//個人設定はYamlに基づき生成する
		setting = new UserSetting(yaml);

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

	public void sendMessageToDisappearAutomatically(String message){
		Player player = Bukkit.getPlayer(uuid);
		if(player != null)
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}

	public void save(Yaml yaml){
		yaml.set("Rank", rank);
		yaml.set("Coins", coins);

		//現在プレイ中のアスレを最後に遊んだアスレとして記録する
		yaml.set("Last played parkour name", currentlyPlayingParkour != null ? currentlyPlayingParkour.name : null);

		yaml.set("Hide users", setting.hideUsers);

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
