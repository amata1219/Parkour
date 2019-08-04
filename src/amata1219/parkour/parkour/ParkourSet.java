package amata1219.parkour.parkour;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.amalib.region.Region;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;

public class ParkourSet {

	private static ParkourSet instance;

	public static ParkourSet getInstance(){
		return instance != null ? instance : (instance = new ParkourSet());
	}

	private final Main plugin = Main.getPlugin();

	//アスレデータを保存するフォルダー
	public final File folder = new File(plugin.getDataFolder() + File.separator + "ParkourList");

	private final Map<String, Parkour> parkourMap = new HashMap<>();

	public final ChunksToObjectsMap<RegionWithBorders> chunksToStartLinesMap = new ChunksToObjectsMap<>();
	public final ChunksToObjectsMap<RegionWithBorders> chunksToFinishLinesMap = new ChunksToObjectsMap<>();
	public final ChunksToObjectsMap<RegionWithBorders> chunksToCheckAreasMap = new ChunksToObjectsMap<>();

	private ParkourSet(){
		//フォルダーが存在しなければ作成する
		if(!folder.exists()) folder.mkdir();

		//各アスレコンフィグ毎に処理をする
		for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
			//ファイルに基づきYamlを生成する
			Yaml yaml = new Yaml(plugin, file);

			//Yamlに基づきアスレを生成する
			Parkour parkour = new Parkour(yaml);

			//アスレを登録する
			registerParkour(parkour);
		}
	}

	public void registerParkour(Parkour parkour){
		parkourMap.put(parkour.name, parkour);

		//スタートラインを登録する
		registerRegionWithBorders(parkour.getStartLine(), chunksToStartLinesMap);

		//ゴールラインを登録する
		registerRegionWithBorders(parkour.getFinishLine(), chunksToFinishLinesMap);

		//チェックエリアを登録する
		for(RegionWithBorders checkArea : parkour.checkAreas)
			registerRegionWithBorders(checkArea, chunksToCheckAreasMap);
	}

	public void unregisterParkour(Parkour parkour){
		//スタートラインの登録を解除する
		registerRegionWithBorders(parkour.getStartLine(), chunksToStartLinesMap);

		//ゴールラインの登録を解除する
		registerRegionWithBorders(parkour.getFinishLine(), chunksToFinishLinesMap);

		//チェックエリアの登録を解除する
		for(RegionWithBorders checkArea : parkour.checkAreas)
			registerRegionWithBorders(checkArea, chunksToCheckAreasMap);

		parkourMap.remove(parkour.name);
	}

	public Parkour getParkour(String parkourName){
		return parkourMap.get(parkourName);
	}

	public boolean containsParkour(String parkourName){
		return parkourMap.containsKey(parkourName);
	}

	public void registerRegionWithBorders(RegionWithBorders regionWithBorders, ChunksToObjectsMap<RegionWithBorders> chunksToRegionsMap){
		//領域を取得する
		Region region = regionWithBorders.region;

		//領域を登録する
		chunksToRegionsMap.putAll(region.lesserBoundaryCorner,  region.greaterBoundaryCorner, regionWithBorders);
	}

	public void unregisterRegionWithBorders(RegionWithBorders regionWithBorders, ChunksToObjectsMap<RegionWithBorders> chunksToRegionsMap){
		//領域を取得する
		Region region = regionWithBorders.region;

		//領域の登録を解除する
		chunksToRegionsMap.removeAll(region.lesserBoundaryCorner,  region.greaterBoundaryCorner, regionWithBorders);
	}

}
