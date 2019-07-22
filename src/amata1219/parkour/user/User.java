package amata1219.parkour.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.yaml.Yaml;

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
			//Parkourの管理クラスから対応したワールドを取得する
			//getList()→toLocation→put
		}
	}

}
