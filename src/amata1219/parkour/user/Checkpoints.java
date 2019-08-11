package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Collections;
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
import amata1219.parkour.parkour.Parkours;

public class Checkpoints {

	private final Parkours parkourSet = Parkours.getInstance();

	private final Map<String, Map<Integer, ImmutableEntityLocation>> checkpoints = new HashMap<>();

	public Checkpoints(Yaml yaml){
		//セクションが存在しなければ戻る
		if(!yaml.isConfigurationSection("Check points")) return;

		//セクションを取得する
		ConfigurationSection parkourSection = yaml.getConfigurationSection("Check points");

		//各アスレ名毎に処理する
		for(String parkourName : parkourSection.getKeys(false)){
			//アスレ名と対応したアスレを取得する
			Parkour parkour = parkourSet.getParkour(parkourName);

			//このアスレのセクションを取得する
			ConfigurationSection checkAreaSection = parkourSection.getConfigurationSection(parkourName);

			//各チェックエリア番号毎に処理をする
			for(String checkAreaNumberText : checkAreaSection.getKeys(false)){
				//チェックエリア番号を整数型に変換する
				int checkAreaNumber = Integer.parseInt(checkAreaNumberText);

				//チェックポイントのデータをデシリアライズする
				ImmutableEntityLocation point = ImmutableEntityLocation.deserialize(checkAreaSection.getString(checkAreaNumberText));

				//チェックポイントとして登録する
				setCheckpoint(parkour, checkAreaNumber, point);
			}
		}
	}

	public List<Parkour> getParkourList(){
		return checkpoints.keySet().stream().filter(parkourSet::containsParkour).map(parkourSet::getParkour).collect(Collectors.toList());
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
		return checkpoints.containsKey(parkourName) ? new ArrayList<>(checkpoints.get(parkourName).values()) : Collections.emptyList();
	}

	public int getCheckpointSize(Parkour parkour){
		return getCheckpointSize(parkour.name);
	}

	public int getCheckpointSize(String parkourName){
		return getCheckpoints(parkourName).size();
	}

	public ImmutableEntityLocation getLastCheckpoint(Parkour parkour){
		return getLastCheckpoint(parkour);
	}

	public ImmutableEntityLocation getLastCheckpoint(String parkourName){
		//チェックポイントのリストを取得する
		List<ImmutableEntityLocation> locations = getCheckpoints(parkourName);

		//空であればnull、そうでなければ最後のチェックポイントを返す
		return locations.isEmpty() ? null : locations.get(locations.size() - 1);
	}

	public void setCheckpoint(Parkour parkour, int checkAreaNumber, ImmutableEntityLocation location){
		String parkourName = parkour.name;

		//パルクールに対応したチェックポイントリストを取得、存在しなければ新規作成する
		Map<Integer, ImmutableEntityLocation> points = checkpoints.get(parkourName);
		if(points == null) checkpoints.put(parkourName, points = new HashMap<>());

		//対応した番号にチェックポイントをセットする
		points.put(checkAreaNumber, location);
	}

	public void save(Yaml yaml){
		//各チェックポイントを記録する
		for(Entry<String, Map<Integer, ImmutableEntityLocation>> eachParkourCheckpointsEntry : checkpoints.entrySet()){
			//アスレ名を取得する
			String parkourName = eachParkourCheckpointsEntry.getKey();

			for(Entry<Integer, ImmutableEntityLocation> eachCheckpointEntry : eachParkourCheckpointsEntry.getValue().entrySet()){
				//チェックエリア番号を取得する
				String checkAreaNumber = eachCheckpointEntry.getKey().toString();

				//チェックポイントを取得する
				ImmutableEntityLocation point = eachCheckpointEntry.getValue();

				//対応したアスレ、チェックエリア番号にセットする
				yaml.set(StringTemplate.apply("Check points.$0.$1", parkourName, checkAreaNumber), point.serialize());
			}
		}
	}

}
