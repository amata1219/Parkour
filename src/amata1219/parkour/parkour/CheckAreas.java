package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.yaml.Yaml;

public class CheckAreas {

	private final Parkours parkours;

	private final Map<Integer, List<ParkourRegion>> checkAreas = new HashMap<>();

	public CheckAreas(Parkours parkours, Yaml yaml, Parkour parkour, ImmutableBlockLocation origin){
		this.parkours = parkours;

		//チェックエリアのセクションが存在しなければ戻る
		if(!yaml.isConfigurationSection("Check areas")) return;

		ConfigurationSection checkAreaSection = yaml.getConfigurationSection("Check areas");

		//各メインチェックエリア番号毎に処理をする
		for(String mainCheckAreaNumberText : checkAreaSection.getKeys(false)){
			//メインチェックエリア番号を整数型に変換する
			int mainCheckAreaNumber = Integer.parseInt(mainCheckAreaNumberText);

			//メインチェックエリアの領域データをデシリアライズしてリストにする
			List<ParkourRegion> areas = checkAreaSection.getStringList(mainCheckAreaNumberText).stream()
										.map(text -> new ParkourRegion(parkour, origin.add(Region.deserialize(text))))
										.collect(Collectors.toList());

			//メインチェックエリア番号とバインドする
			checkAreas.put(mainCheckAreaNumber, areas);
		}
	}

	//メインチェックエリア番号を取得する
	public int getCheckAreaMainNumber(ParkourRegion checkArea){
		for(Entry<Integer, List<ParkourRegion>> checkAreasEntry : checkAreas.entrySet()){
			//メインチェックエリア番号を取得する
			int mainCheckAreaNumber = checkAreasEntry.getKey();

			//サブチェックエリア番号を取得する
			int subCheckAreaNumber = checkAreasEntry.getValue().indexOf(checkArea);

			//リスト内に同じチェックエリアが存在すればそのメインチェックエリア番号を返す
			if(subCheckAreaNumber >= 0) return mainCheckAreaNumber;
		}

		return -1;
	}

	//メインチェックエリア番号にバインドされたチェックエリアリストを取得する
	public List<ParkourRegion> getCheckAreas(int mainCheckAreaNumber){
		return checkAreas.containsKey(mainCheckAreaNumber) ? checkAreas.get(mainCheckAreaNumber) : Collections.emptyList();
	}

	//チェックエリアをメインチェックエリア番号にバインドする
	public void bindCheckArea(int mainCheckAreaNumber, ParkourRegion checkArea){
		//チェックエリアリストを取得する
		List<ParkourRegion> areas = checkAreas.get(mainCheckAreaNumber);

		//リストが無ければ作成する
		if(areas == null) checkAreas.put(mainCheckAreaNumber, areas = new ArrayList<>());

		//リストにチェックエリアを追加する
		areas.add(checkArea);
	}

	public void setCheckArea(int mainCheckAreaNumber, int subCheckAreaNumber, ParkourRegion checkArea){

	}

	public void unbindCheckArea(ParkourRegion checkArea){

	}

	public void registerAll(){
		applyAllCheckAreas(parkours::registerCheckArea);
	}

	public void unregisterAll(){
		applyAllCheckAreas(parkours::unregisterCheckArea);
	}

	public void displayAll(){
		applyAllCheckAreas(ParkourRegion::displayBorders);
	}

	public void undisplayAll(){
		applyAllCheckAreas(ParkourRegion::undisplayBorders);
	}

	private void applyAllCheckAreas(Consumer<ParkourRegion> applier){
		for(List<ParkourRegion> areas : checkAreas.values())
			for(ParkourRegion area : areas) applier.accept(area);
	}

	/*public void setCheckArea(int checkAreaNumber, ParkourRegion checkArea){
		if(checkAreaNumber >= areas.size()) throw new IllegalArgumentException("Check area number must be less than or equal to number of check areas");

		//既存のチェックエリアを取得する
		ParkourRegion old = areas.get(checkAreaNumber);

		//登録を解除する
		parkours.unregisterCheckArea(old);

		//チェックエリアを書き換える
		areas.set(checkAreaNumber, checkArea);

		//新しいチェックエリアを登録する
		parkours.registerCheckArea(checkArea);
	}

	public void removeCheckArea(int checkAreaNumber){
		if(checkAreaNumber >= areas.size()) throw new IllegalArgumentException("Check area number must be less than or equal to number of check areas");

		//既存のチェックエリアを取得する
		ParkourRegion old = areas.get(checkAreaNumber);

		//登録を解除する
		parkours.unregisterCheckArea(old);
		areas.remove(old);
	}

	public void displayAll(){
		areas.forEach(ParkourRegion::displayBorders);
	}

	public void undisplayAll(){
		areas.forEach(ParkourRegion::undisplayBorders);
	}

	public void registerAll(){
		areas.forEach(parkours::registerCheckArea);
	}

	public void unregisterAll(){
		areas.forEach(parkours::unregisterCheckArea);
	}

	public void save(Yaml yaml, ImmutableBlockLocation origin){
		yaml.set("Check areas",
				areas.stream()
						.map(area -> origin.relative(area))
						.map(Region::serialize)
						.collect(Collectors.toList())
				);
	}*/

}
