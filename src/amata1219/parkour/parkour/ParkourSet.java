package amata1219.parkour.parkour;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.amalib.region.Region;
import amata1219.amalib.string.StringTemplate;
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

	//スタートラインのチャンクマップ
	public final ChunksToObjectsMap<RegionWithBorders> chunksToStartLinesMap = new ChunksToObjectsMap<>();

	//フィニッシュラインのチャンクマップ
	public final ChunksToObjectsMap<RegionWithBorders> chunksToFinishLinesMap = new ChunksToObjectsMap<>();

	//チェックエリアのチャンクマップ
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
		registerStartLine(parkour.getStartLine());

		//フィニッシュラインを登録する
		registerFinishLine(parkour.getFinishLine());

		//全チェックエリアを登録する
		parkour.checkAreas.registerAll();
	}

	public void unregisterParkour(Parkour parkour){
		//スタートラインの登録を解除する
		unregisterStartLine(parkour.getStartLine());

		//フィニッシュラインの登録を解除する
		unregisterFinishLine(parkour.getFinishLine());

		//全チェックエリアの登録を解除する
		parkour.checkAreas.unregisterAll();

		parkourMap.remove(parkour.name);
	}

	public Parkour getParkour(String parkourName){
		return parkourMap.get(parkourName);
	}

	public boolean containsParkour(Parkour parkour){
		return containsParkour(parkour.name);
	}

	public boolean containsParkour(String parkourName){
		return parkourMap.containsKey(parkourName);
	}

	public void registerStartLine(RegionWithBorders startLine){
		registerRegionWithBorders(startLine, chunksToStartLinesMap);
	}

	public void unregisterStartLine(RegionWithBorders startLine){
		unregisterRegionWithBorders(startLine, chunksToStartLinesMap);
	}

	public void registerFinishLine(RegionWithBorders finishLine){
		registerRegionWithBorders(finishLine, chunksToFinishLinesMap);
	}

	public void unregisterFinishLine(RegionWithBorders finishLine){
		unregisterRegionWithBorders(finishLine, chunksToFinishLinesMap);
	}

	public void registerCheckArea(RegionWithBorders checkArea){
		registerRegionWithBorders(checkArea, chunksToCheckAreasMap);
	}

	public void unregisterCheckArea(RegionWithBorders checkArea){
		unregisterRegionWithBorders(checkArea, chunksToCheckAreasMap);
	}

	public Yaml getYaml(String parkourName){
		return new Yaml(plugin, new File(folder, StringTemplate.apply("$0.yml", parkourName)), "parkour.yml");
	}

	private void registerRegionWithBorders(RegionWithBorders regionWithBorders, ChunksToObjectsMap<RegionWithBorders> chunksToRegionsMap){
		//領域を取得する
		Region region = regionWithBorders.region;

		//領域を登録する
		chunksToRegionsMap.putAll(region.lesserBoundaryCorner,  region.greaterBoundaryCorner, regionWithBorders);

		//境界線の描画を始める
		regionWithBorders.display();
	}

	private void unregisterRegionWithBorders(RegionWithBorders regionWithBorders, ChunksToObjectsMap<RegionWithBorders> chunksToRegionsMap){
		//境界線の描画を止める
		regionWithBorders.undisplay();

		//領域を取得する
		Region region = regionWithBorders.region;

		//領域の登録を解除する
		chunksToRegionsMap.removeAll(region.lesserBoundaryCorner,  region.greaterBoundaryCorner, regionWithBorders);
	}

}
