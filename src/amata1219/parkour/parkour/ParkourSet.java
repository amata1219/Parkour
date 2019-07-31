package amata1219.parkour.parkour;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.ChatColor;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.amalib.region.Region;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;

public class ParkourSet {

	private final Main plugin = Main.getPlugin();
	private final File folder = new File(plugin.getDataFolder() + File.separator + "ParkourList");

	public final Map<String, Parkour> parkourMap = new HashMap<>();

	public final ChunksToObjectsMap<RegionBorderDisplayer> chunksToStartLinesMap = new ChunksToObjectsMap<>();
	public final ChunksToObjectsMap<RegionBorderDisplayer> chunksToFinishLinesMap = new ChunksToObjectsMap<>();
	public final ChunksToObjectsMap<RegionBorderDisplayer> chunksToCheckAreasMap = new ChunksToObjectsMap<>();

	public ParkourSet(){
		if(!folder.exists())
			folder.mkdir();

		//各パルクールコンフィグ毎に処理をする
		for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[]{})){
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
		registerChunksToRegionsMap(parkour.startLine, chunksToStartLinesMap);

		//ゴールラインを登録する
		registerChunksToRegionsMap(parkour.finishLine, chunksToFinishLinesMap);

		//チェックエリアを登録する
		for(RegionBorderDisplayer checkArea : parkour.checkAreas)
			registerChunksToRegionsMap(checkArea, chunksToCheckAreasMap);
	}

	public boolean isParkourExists(String parkourName){
		return getParkour(parkourName) != null;
	}

	public Parkour getParkour(String parkourName){
		return parkourMap.get(ChatColor.stripColor(parkourName));
	}

	private void registerChunksToRegionsMap(RegionBorderDisplayer graphicalRegion, ChunksToObjectsMap<RegionBorderDisplayer> chunksToRegionsMap){
		//領域を取得する
		Region region = graphicalRegion.region;

		//領域を登録する
		chunksToRegionsMap.putAll(region.lesserBoundaryCorner,  region.greaterBoundaryCorner, graphicalRegion);
	}

}
