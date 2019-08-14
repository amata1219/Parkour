package amata1219.parkour.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.Parkours;

public class Checkpoints {

	private final Parkours parkours = Parkours.getInstance();

	private final Map<String, Map<Integer, ImmutableEntityLocation>> checkpoints = new HashMap<>();
	private final Map<String, Integer> latestCheckpoints = new HashMap<>();

	public Checkpoints(Yaml yaml){
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
			ImmutableBlockLocation origin = parkour.getOrigin();

			//このアスレのセクションを取得する
			ConfigurationSection checkAreaSection = parkourSection.getConfigurationSection(parkourName);

			//各チェックエリア番号毎に処理をする
			for(String checkAreaNumberText : checkAreaSection.getKeys(false)){
				if(checkAreaNumberText.equals("Latest")) continue;

				//チェックエリア番号を整数型に変換する
				int checkAreaNumber = Integer.parseInt(checkAreaNumberText);

				//チェックポイントのデータをデシリアライズする
				ImmutableEntityLocation point = (ImmutableEntityLocation) ImmutableEntityLocation.deserialize(checkAreaSection.getString(checkAreaNumberText)).add(origin);

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

	public Map<Integer, ImmutableEntityLocation> getMajorCheckAreaNumbersAndCheckpoints(Parkour parkour){
		return getMajorCheckAreaNumbersAndCheckpoints(parkour.name);
	}

	public Map<Integer, ImmutableEntityLocation> getMajorCheckAreaNumbersAndCheckpoints(String parkourName){
		return checkpoints.containsKey(parkourName) ? new HashMap<>(checkpoints.get(parkourName)) : Collections.emptyMap();
	}

	public List<ImmutableEntityLocation> getCheckpoints(Parkour parkour){
		return getCheckpoints(parkour.name);
	}

	public List<ImmutableEntityLocation> getCheckpoints(String parkourName){
		return checkpoints.containsKey(parkourName) ? new ArrayList<>(checkpoints.get(parkourName).values()) : Collections.emptyList();
	}

	public ImmutableEntityLocation getLastCheckpoint(Parkour parkour){
		return getLastCheckpoint(parkour.name);
	}

	public ImmutableEntityLocation getLastCheckpoint(String parkourName){
		//チェックポイントのリストを取得する
		List<ImmutableEntityLocation> locations = getCheckpoints(parkourName);

		//空であればnull、そうでなければ最後のチェックポイントを返す
		return locations.isEmpty() ? null : locations.get(locations.size() - 1);
	}

	public int getLastCheckpointNumber(Parkour parkour){
		return getLastCheckpointNumber(parkour.name);
	}

	public int getLastCheckpointNumber(String parkourName){
		return getCheckpoints(parkourName).size() - 1;
	}

	public ImmutableEntityLocation getLatestCheckpoint(Parkour parkour){
		return getLatestCheckpoint(parkour.name);
	}

	public ImmutableEntityLocation getLatestCheckpoint(String parkourName){
		//最新のチェックポイントが存在しなければnullを返す
		if(!latestCheckpoints.containsKey(parkourName)) return null;

		//最新のチェックエリア番号を取得する
		int latestCheckAreaNumber = latestCheckpoints.get(parkourName);

		//念の為にチェックポイントマップが存在しなければnullを返す
		if(!checkpoints.containsKey(parkourName)) return null;

		//アスレ内のチェックポイントマップを取得する
		Map<Integer, ImmutableEntityLocation> points = checkpoints.get(parkourName);

		return points.get(latestCheckAreaNumber);
	}

	public int getLatestCheckpointNumber(Parkour parkour){
		return getLatestCheckpointNumber(parkour.name);
	}

	public int getLatestCheckpointNumber(String parkourName){
		return latestCheckpoints.containsKey(parkourName) ? latestCheckpoints.get(parkourName) : -1;
	}

	public void setCheckpoint(Parkour parkour, int checkAreaNumber, ImmutableEntityLocation location){
		String parkourName = parkour.name;

		//パルクールに対応したチェックポイントリストを取得、存在しなければ新規作成する
		Map<Integer, ImmutableEntityLocation> points = checkpoints.get(parkourName);
		if(points == null) checkpoints.put(parkourName, points = new HashMap<>());

		//対応した番号にチェックポイントをセットする
		points.put(checkAreaNumber, location);

		//最新のチェックポイントを更新する
		latestCheckpoints.put(parkourName, checkAreaNumber);
	}

	public void save(Yaml yaml){
		//各チェックポイントを記録する
		for(Entry<String, Map<Integer, ImmutableEntityLocation>> eachParkourCheckpointsEntry : checkpoints.entrySet()){
			//アスレ名を取得する
			String parkourName = eachParkourCheckpointsEntry.getKey();

			//存在しないアスレであれば繰り返す
			if(!parkours.containsParkour(parkourName)) continue;

			ImmutableBlockLocation origin = parkours.getParkour(parkourName).getOrigin();

			for(Entry<Integer, ImmutableEntityLocation> eachCheckpointEntry : eachParkourCheckpointsEntry.getValue().entrySet()){
				//チェックエリア番号を取得する
				String checkAreaNumber = eachCheckpointEntry.getKey().toString();

				//チェックポイントを取得し相対座標化する
				ImmutableEntityLocation point = (ImmutableEntityLocation) eachCheckpointEntry.getValue().relative(origin);

				//対応したアスレ、チェックエリア番号にセットする
				yaml.set(StringTemplate.apply("Check points.$0.$1", parkourName, checkAreaNumber), point.serialize());
			}

			//最新のチェックポイントをセットする
			yaml.set(StringTemplate.apply("Check points.$0.Latest", parkourName), latestCheckpoints.get(parkourName));
		}
	}

}
