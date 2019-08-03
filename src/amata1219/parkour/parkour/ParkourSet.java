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

	public final Map<String, Parkour> parkourMap = new HashMap<>();

	public final ChunksToObjectsMap<RegionBorderDisplayer> chunksToStartLinesMap = new ChunksToObjectsMap<>();
	public final ChunksToObjectsMap<RegionBorderDisplayer> chunksToFinishLinesMap = new ChunksToObjectsMap<>();
	public final ChunksToObjectsMap<RegionBorderDisplayer> chunksToCheckAreasMap = new ChunksToObjectsMap<>();

	private ParkourSet(){
		//フォルダーが存在しなければ作成する
		if(!folder.exists()) folder.mkdir();

		//各パルクールコンフィグ毎に処理をする
		for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
			//ファイルに基づきYamlを生成する
			Yaml yaml = new Yaml(plugin, file);

			//Yamlに基づきパルクールを生成する
			Parkour parkour = new Parkour(yaml);

			registerParkour(parkour);
		}
	}

	public void registerParkour(Parkour parkour){
		//パルクールマップに追加する
		parkourMap.put(parkour.name, parkour);

		//スタートラインを登録する
		registerChunksToRegionsMap(parkour.getStartLine(), chunksToStartLinesMap);

		//ゴールラインを登録する
		registerChunksToRegionsMap(parkour.getFinishLine(), chunksToFinishLinesMap);

		//チェックエリアを登録する
		for(RegionBorderDisplayer checkArea : parkour.checkAreas)
			registerChunksToRegionsMap(checkArea, chunksToCheckAreasMap);
	}

	public boolean isParkourExists(String colorlessParkourName){
		return parkourMap.containsKey(colorlessParkourName);
	}

	public Parkour getParkour(String colorlessParkourName){
		return parkourMap.get(colorlessParkourName);
	}

	private void registerChunksToRegionsMap(RegionBorderDisplayer displayer, ChunksToObjectsMap<RegionBorderDisplayer> chunksToRegionsMap){
		//領域を取得する
		Region region = displayer.region;

		//領域を登録する
		chunksToRegionsMap.putAll(region.lesserBoundaryCorner,  region.greaterBoundaryCorner, displayer);
	}

}
