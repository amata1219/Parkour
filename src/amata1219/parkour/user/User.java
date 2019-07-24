package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;

public class User {

	//UUID
	public final UUID uuid;

	//ランク
	public final int rank;

	//個人設定
	public final UserSetting setting;

	//各アスレのチェックポイント
	public final Map<String, List<Location>> points = new HashMap<>();

	/*
	 * ParkourName#CheckPoints
	 * ステージいらない→stage,list<parkour>は他の管理クラスに作る
	 * containsKeyOfStage?
	 * containsKeyOfParkour?
	 * getCheckPoints.get(num)
	 *
	 * LastParkour: name
	 *
	 */

	public User(UUID uuid){
		//初回ログイン時にyamlを生成してそれを読み込む形にするか検討中
		this.uuid = uuid;
	}

	public User(Yaml yaml){
		//ファイル名に基づきUUIDを生成し代入する
		this.uuid = UUID.fromString(yaml.name);

		//個人設定はYamlに基づき生成する
		setting = new UserSetting(yaml);

		//セクションが存在しなければ戻る
		if(!yaml.isConfigurationSection("CheckPoints"))
			return;

		//セクションを取得する
		ConfigurationSection section = yaml.getConfigurationSection("CheckPoints");

		//各アスレ名毎に処理する
		for(String parkourName : section.getKeys(false)){
			Parkour parkour = Main.getParkourSet().parkourMap.get(parkourName);
			List<Location> points = section.getStringList(parkourName)
											.stream()
											.map(point -> point.split(","))
											.map(coordinates -> Arrays.stream(coordinates).mapToDouble(Double::parseDouble).toArray())
											.map(coordinates -> new Location(parkour.world, coordinates[0], coordinates[1], coordinates[2], (float) coordinates[3], (float) coordinates[4]))
											.collect(Collectors.toList());

			for(int number = 0; number < points.size(); number++)
				setCheckPoint(parkour, number, points.get(number));
		}
	}

	//numberは0から始まる
	public void setCheckPoint(Parkour parkour, int number, Location location){
		String parkourName = parkour.name;
		List<Location> points = this.points.containsKey(parkourName) ? this.points.get(parkourName) : this.points.put(parkourName, new ArrayList<>());
		if(points.size() >= number)
			points.add(location);
		else
			points.set(number, location);
	}

}
