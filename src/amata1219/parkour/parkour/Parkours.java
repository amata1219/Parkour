package amata1219.parkour.parkour;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.RankedParkour.RankedParkourType;

public class Parkours {

	private static Parkours instance;

	public static void load(){
		instance = new Parkours();
	}

	public static Parkours getInstance(){
		return instance;
	}

	private final Main plugin = Main.getPlugin();

	//アスレデータを保存するフォルダー
	public final File folder = new File(plugin.getDataFolder() + File.separator + "Parkours");

	//アスレのマップ
	private final Map<String, Parkour> parkours = new HashMap<>();

	//スタートラインのチャンクマップ
	public final ChunksToObjectsMap<ParkourRegion> chunksToStartLinesMap = new ChunksToObjectsMap<>();

	//フィニッシュラインのチャンクマップ
	public final ChunksToObjectsMap<ParkourRegion> chunksToFinishLinesMap = new ChunksToObjectsMap<>();

	//チェックエリアのチャンクマップ
	public final ChunksToObjectsMap<ParkourRegion> chunksToCheckAreasMap = new ChunksToObjectsMap<>();

	private Parkours(){
		//フォルダーが存在しなければ作成する
		if(!folder.exists()) folder.mkdirs();

		//各アスレコンフィグ毎に処理をする
		for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
			//ファイルに基づきYamlを生成する
			Yaml yaml = new Yaml(plugin, file);

			//Yamlに基づきアスレを生成する
			Parkour parkour = RankedParkour.isRankedParkour(yaml.name) ? new RankedParkour(this, yaml) : new Parkour(this, yaml);

			//アスレを登録する
			registerParkour(parkour);
		}
	}

	public void saveAll(){
		parkours.values().forEach(Parkour::save);
	}

	public boolean existsFile(String parkourName){
		return new File(folder, StringTemplate.apply("$0.yml", parkourName)).exists();
	}

	public void registerParkour(Parkour parkour){
		parkours.put(parkour.name, parkour);

		//有効化されていなければ戻る
		if(!parkour.enable) return;

		//スタートラインを登録する
		registerStartLine(parkour.startLine);

		//フィニッシュラインを登録する
		registerFinishLine(parkour.finishLine);

		//全チェックエリアを登録する
		parkour.checkAreas.registerAll();
	}

	public void registerParkour(String parkourName){
		if(!existsFile(parkourName)) return;

		//コンフィグを取得する
		Yaml yaml = makeYaml(parkourName);

		//コンフィグに基づきアスレを生成する
		Parkour parkour = new Parkour(this, yaml);

		registerParkour(parkour);
	}

	public void unregisterParkour(Parkour parkour){
		//スタートラインの登録を解除する
		if(parkour.startLine != null) unregisterStartLine(parkour.startLine);

		//フィニッシュラインの登録を解除する
		if(parkour.finishLine != null) unregisterFinishLine(parkour.finishLine);

		//全チェックエリアの登録を解除する
		parkour.checkAreas.unregisterAll();

		parkours.remove(parkour.name);
	}

	public void unregisterParkour(String parkourName){
		if(containsParkour(parkourName)) unregisterParkour(getParkour(parkourName));
	}

	public Collection<Parkour> getParkours(){
		return parkours.values();
	}

	public List<Parkour> getParkours(ParkourCategory category){
		return parkours.values().stream()
				.filter(parkour -> parkour.category == category)
				.collect(Collectors.toList());
	}

	public List<RankedParkour> getUpdateParkours(){
		return getRankedParkours(RankedParkourType.UPDATE);
	}

	public List<RankedParkour> getExtendParkours(){
		return getRankedParkours(RankedParkourType.EXTEND);
	}

	private List<RankedParkour> getRankedParkours(RankedParkourType type){
		return parkours.values().stream()
				.filter(parkour -> parkour instanceof RankedParkour)
				.map(parkour -> (RankedParkour) parkour)
				.filter(parkour -> parkour.type == type)
				.collect(Collectors.toList());
	}

	public Parkour getParkour(String parkourName){
		return parkours.get(parkourName);
	}

	public boolean containsParkour(Parkour parkour){
		return containsParkour(parkour.name);
	}

	public boolean containsParkour(String parkourName){
		return parkours.containsKey(parkourName);
	}

	public void registerStartLine(ParkourRegion startLine){
		registerParkourRegion(startLine, chunksToStartLinesMap);
	}

	public void unregisterStartLine(ParkourRegion startLine){
		unregisterParkourRegion(startLine, chunksToStartLinesMap);
	}

	public void registerFinishLine(ParkourRegion finishLine){
		registerParkourRegion(finishLine, chunksToFinishLinesMap);
	}

	public void unregisterFinishLine(ParkourRegion finishLine){
		unregisterParkourRegion(finishLine, chunksToFinishLinesMap);
	}

	public void registerCheckArea(ParkourRegion checkArea){
		registerParkourRegion(checkArea, chunksToCheckAreasMap);
	}

	public void unregisterCheckArea(ParkourRegion checkArea){
		unregisterParkourRegion(checkArea, chunksToCheckAreasMap);
	}

	public Yaml makeYaml(String parkourName){
		return new Yaml(plugin, new File(folder, StringTemplate.apply("$0.yml", parkourName)), "parkour.yml");
	}

	private void registerParkourRegion(ParkourRegion region, ChunksToObjectsMap<ParkourRegion> chunksToRegionsMap){
		if(region == null) return;

		//領域を登録する
		chunksToRegionsMap.putAll(region.lesserBoundaryCorner,  region.greaterBoundaryCorner, region);

		//境界線の描画を始める
		region.displayBorders();
	}

	private void unregisterParkourRegion(ParkourRegion region, ChunksToObjectsMap<ParkourRegion> chunksToRegionsMap){
		if(region == null) return;

		//境界線の描画を止める
		region.undisplayBorders();

		//領域の登録を解除する
		chunksToRegionsMap.removeAll(region.lesserBoundaryCorner,  region.greaterBoundaryCorner, region);
	}

}
