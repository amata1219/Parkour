package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;

import amata1219.parkour.location.ImmutableLocation;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.string.StringTemplate;
import amata1219.parkour.yaml.Yaml;

public class CheckpointSet {

	private final ParkourSet parkours = ParkourSet.getInstance();

	private final Map<String, Map<Integer, ImmutableLocation>> checkpoints = new HashMap<>();
	private final Map<String, Integer> latestCheckpoints = new HashMap<>();

	public CheckpointSet(Yaml yaml){
		//セクションが存在しなければ戻る
		if(!yaml.isConfigurationSection("Check points")) return;

		//セクションを取得する
		ConfigurationSection parkourSection = yaml.getConfigurationSection("Check points");

		//各アスレ名毎に処理する
		for(String parkourName : parkourSection.getKeys(false)){
			//存在しないアスレであれば繰り返す
			if(!parkours.containsParkour(parkourName)) continue;

			//アスレ名と対応したアスレを取得する
			Parkour parkour = parkours.getParkour(parkourName);
			ImmutableLocation origin = parkour.originLocation();

			//このアスレのセクションを取得する
			ConfigurationSection checkAreaSection = parkourSection.getConfigurationSection(parkourName);

			//各チェックエリア番号毎に処理をする
			for(String checkAreaNumberText : checkAreaSection.getKeys(false)){
				if(checkAreaNumberText.equals("Latest")) continue;

				//チェックエリア番号を整数型に変換する
				int checkAreaNumber = Integer.parseInt(checkAreaNumberText);

				//チェックポイントのデータをデシリアライズする
				ImmutableLocation point = origin.add(ImmutableLocation.deserialize(checkAreaSection.getString(checkAreaNumberText)));

				//チェックポイントとして登録する
				setCheckpoint(parkour, checkAreaNumber, point);
			}

			//最新のチェックエリア番号を取得する
			int latestCheckAreaNumber = checkAreaSection.getInt("Latest");
			latestCheckpoints.put(parkourName, latestCheckAreaNumber);
		}
	}

	public List<Parkour> getParkourList(){
		return checkpoints.keySet().stream().filter(parkours::containsParkour).map(parkours::getParkour).collect(Collectors.toList());
	}

	public boolean containsParkour(Parkour parkour){
		return containsParkour(parkour.name);
	}

	public boolean containsParkour(String parkourName){
		return checkpoints.containsKey(parkourName);
	}

	public Map<Integer, ImmutableLocation> getMajorCheckAreaNumbersAndCheckpoints(Parkour parkour){
		return getMajorCheckAreaNumbersAndCheckpoints(parkour.name);
	}

	public Map<Integer, ImmutableLocation> getMajorCheckAreaNumbersAndCheckpoints(String parkourName){
		return checkpoints.containsKey(parkourName) ? new HashMap<>(checkpoints.get(parkourName)) : Collections.emptyMap();
	}

	public List<ImmutableLocation> getCheckpoints(Parkour parkour){
		return getCheckpoints(parkour.name);
	}

	public List<ImmutableLocation> getCheckpoints(String parkourName){
		return checkpoints.containsKey(parkourName) ? new ArrayList<>(checkpoints.get(parkourName).values()) : Collections.emptyList();
	}

	public ImmutableLocation getLastCheckpoint(Parkour parkour){
		return getLastCheckpoint(parkour.name);
	}

	public ImmutableLocation getLastCheckpoint(String parkourName){
		int lastCheckpointNumber = getLastCheckpointNumber(parkourName);

		return lastCheckpointNumber != -1 ? checkpoints.get(parkourName).get(lastCheckpointNumber) : null;
	}

	public int getLastCheckpointNumber(Parkour parkour){
		return getLastCheckpointNumber(parkour.name);
	}

	public int getLastCheckpointNumber(String parkourName){
		return checkpoints.containsKey(parkourName) ? checkpoints.get(parkourName).keySet().stream().mapToInt(Integer::intValue).max().orElse(-1) : -1;
	}

	public ImmutableLocation getLatestCheckpoint(Parkour parkour){
		return getLatestCheckpoint(parkour.name);
	}

	public ImmutableLocation getLatestCheckpoint(String parkourName){
		//最新のチェックポイントが存在しなければnullを返す
		if(!latestCheckpoints.containsKey(parkourName)) return null;

		//最新のチェックエリア番号を取得する
		int latestCheckAreaNumber = latestCheckpoints.get(parkourName);

		//念の為にチェックポイントマップが存在しなければnullを返す
		if(!checkpoints.containsKey(parkourName)) return null;

		//アスレ内のチェックポイントマップを取得する
		Map<Integer, ImmutableLocation> points = checkpoints.get(parkourName);

		return points.get(latestCheckAreaNumber);
	}

	public int getLatestCheckpointNumber(Parkour parkour){
		return getLatestCheckpointNumber(parkour.name);
	}

	public int getLatestCheckpointNumber(String parkourName){
		return latestCheckpoints.containsKey(parkourName) ? latestCheckpoints.get(parkourName) : -1;
	}

	public void setCheckpoint(Parkour parkour, int checkAreaNumber, ImmutableLocation location){
		String parkourName = parkour.name;

		//パルクールに対応したチェックポイントリストを取得、存在しなければ新規作成する
		Map<Integer, ImmutableLocation> points = checkpoints.get(parkourName);
		if(points == null) checkpoints.put(parkourName, points = new HashMap<>());

		//対応した番号にチェックポイントをセットする
		points.put(checkAreaNumber, location);

		//最新のチェックポイントを更新する
		latestCheckpoints.put(parkourName, checkAreaNumber);
	}

	public void save(Yaml yaml){
		//各チェックポイントを記録する
		for(Entry<String, Map<Integer, ImmutableLocation>> eachParkourCheckpointsEntry : checkpoints.entrySet()){
			//アスレ名を取得する
			String parkourName = eachParkourCheckpointsEntry.getKey();

			//存在しないアスレであれば繰り返す
			if(!parkours.containsParkour(parkourName)) continue;

			ImmutableLocation origin = parkours.getParkour(parkourName).originLocation();

			for(Entry<Integer, ImmutableLocation> eachCheckpointEntry : eachParkourCheckpointsEntry.getValue().entrySet()){
				//チェックエリア番号を取得する
				String checkAreaNumber = eachCheckpointEntry.getKey().toString();

				//チェックポイントを取得し相対座標化する
				ImmutableLocation point = origin.relative(eachCheckpointEntry.getValue());

				//対応したアスレ、チェックエリア番号にセットする
				yaml.set(StringTemplate.apply("Check points.$0.$1", parkourName, checkAreaNumber), point.serialize());
			}

			//最新のチェックポイントをセットする
			yaml.set(StringTemplate.apply("Check points.$0.Latest", parkourName), latestCheckpoints.get(parkourName));
		}
	}

}
