package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;

public class CheckpointSet {

	private final Map<String, List<ImmutableEntityLocation>> checkpoints = new HashMap<>();

	public CheckpointSet(Yaml yaml){
		//セクションが存在しなければ戻る
		if(!yaml.isConfigurationSection("Check points")) return;

		//セクションを取得する
		ConfigurationSection section = yaml.getConfigurationSection("Check points");

		ParkourSet parkourSet = ParkourSet.getInstance();

		//各アスレ名毎に処理する
		for(String parkourName : section.getKeys(false)){
			//アスレ名と対応したアスレを取得する
			Parkour parkour = parkourSet.getParkour(parkourName);

			//チェックポイントを取得する
			List<ImmutableEntityLocation> points = section.getStringList(parkourName).stream().map(ImmutableEntityLocation::deserialize).collect(Collectors.toList());

			//エリア番号と結び付けてチェックポイントをセットする
			for(int checkAreaNumber = 0; checkAreaNumber < points.size(); checkAreaNumber++)
				setCheckpoint(parkour, checkAreaNumber, points.get(checkAreaNumber));
		}
	}

	public boolean containsParkour(Parkour parkour){
		return containsParkour(parkour.name);
	}

	public boolean containsParkour(String parkourName){
		return checkpoints.containsKey(parkourName);
	}

	public List<ImmutableEntityLocation> getCheckpoints(Parkour parkour){
		return getCheckpoints(parkour.name);
	}

	public List<ImmutableEntityLocation> getCheckpoints(String parkourName){
		return checkpoints.get(parkourName);
	}

	public int getCheckpointSize(Parkour parkour){
		return getCheckpointSize(parkour.name);
	}

	public int getCheckpointSize(String parkourName){
		return getCheckpoints(parkourName).size();
	}

	public void setCheckpoint(Parkour parkour, int checkAreaNumber, ImmutableEntityLocation location){
		String parkourName = parkour.name;

		//パルクールに対応したチェックポイントリストを取得、存在しなければ新規作成する
		List<ImmutableEntityLocation> points = checkpoints.containsKey(parkourName) ? checkpoints.get(parkourName) : checkpoints.put(parkourName, new ArrayList<>());

		if(points.size() >= checkAreaNumber)
			//新しいチェックポイントであればそのまま追加
			points.add(location);
		else
			//既に存在しているチェックポイントであれば更新する
			points.set(checkAreaNumber, location);
	}

	public void save(Yaml yaml){
		//各チェックポイントを記録する
		for(Entry<String, List<ImmutableEntityLocation>> entry : checkpoints.entrySet()){
			//アスレ名を取得する
			String parkourName = entry.getKey();

			//座標を文字列に変換しリスト化する
			List<String> points = entry.getValue().stream().map(ImmutableEntityLocation::serialize).collect(Collectors.toList());

			//対応したアスレ名の階層にチェックポイントリストを記録する
			yaml.set(StringTemplate.apply("Check points.$0", parkourName), points);
		}
	}

}
