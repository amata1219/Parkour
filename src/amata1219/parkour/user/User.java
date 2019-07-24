package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;

public class User {

	//UUID
	public final UUID uuid;

	//ランク
	public int rank;

	//各アスレのチェックポイント
	public final Map<String, List<Location>> checkPoints = new HashMap<>();

	//個人設定
	public final UserSetting setting;

	public User(Yaml yaml){
		//ファイル名に基づきUUIDを生成し代入する
		this.uuid = UUID.fromString(yaml.name);

		rank = yaml.getInt("Rank");

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
		List<Location> points = checkPoints.containsKey(parkourName) ? checkPoints.get(parkourName) : checkPoints.put(parkourName, new ArrayList<>());
		if(points.size() >= number)
			points.add(location);
		else
			points.set(number, location);
	}

	public void save(Yaml yaml){
		yaml.set("Rank", rank);
		yaml.set("Hide users", setting.hideUsers);

		for(Entry<String, List<Location>> entry : checkPoints.entrySet()){
			List<String> points = entry.getValue()
					.stream()
					.map(location -> StringTemplate.format("$0,$1,$2,$3,$4", location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()))
					.collect(Collectors.toList());

			yaml.set(StringTemplate.format("CheckPoints.$0", entry.getKey()), points);
		}

		yaml.update();
	}

}
