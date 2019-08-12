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
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;

public class CheckAreas {

	private final Parkours parkours;

	private final Map<Integer, List<ParkourRegion>> checkAreas = new HashMap<>();

	public CheckAreas(Parkours parkours, Yaml yaml, Parkour parkour, ImmutableBlockLocation origin){
		this.parkours = parkours;

		//チェックエリアのセクションが存在しなければ戻る
		if(!yaml.isConfigurationSection("Check areas")) return;

		ConfigurationSection checkAreaSection = yaml.getConfigurationSection("Check areas");

		//各メジャーチェックエリア番号毎に処理をする
		for(String majorCheckAreaNumberText : checkAreaSection.getKeys(false)){
			//メジャーチェックエリア番号を整数型に変換する
			int majorCheckAreaNumber = Integer.parseInt(majorCheckAreaNumberText);

			//メジャーチェックエリアの領域データをデシリアライズしてリストにする
			List<ParkourRegion> areas = checkAreaSection.getStringList(majorCheckAreaNumberText).stream()
										.map(text -> new ParkourRegion(parkour, origin.add(Region.deserialize(text))))
										.collect(Collectors.toList());

			//メジャーチェックエリア番号とバインドする
			checkAreas.put(majorCheckAreaNumber, areas);
		}
	}

	//メジャーチェックエリア番号を取得する
	public int getMajorCheckAreaNumber(ParkourRegion checkArea){
		for(Entry<Integer, List<ParkourRegion>> checkAreasEntry : checkAreas.entrySet()){
			//メジャーチェックエリア番号を取得する
			int majorCheckAreaNumber = checkAreasEntry.getKey();

			//マイナーチェックエリア番号を取得する
			int minorCheckAreaNumber = checkAreasEntry.getValue().indexOf(checkArea);

			//リスト内に同じチェックエリアが存在すればそのメジャーチェックエリア番号を返す
			if(minorCheckAreaNumber >= 0) return majorCheckAreaNumber;
		}

		return -1;
	}

	//マイナーチェックエリア番号を取得する
	public int getMinorCheckAreaNumber(ParkourRegion checkArea){
		int majorCheckAreaNumber = getMajorCheckAreaNumber(checkArea);

		//バインドされていないチェックエリアであれば-1を返す
		if(majorCheckAreaNumber <= -1) return -1;

		//メジャーチェックエリア番号にバインドされたチェックエリアのリストを取得する
		List<ParkourRegion> areas = getCheckAreas(majorCheckAreaNumber);

		return areas.indexOf(checkArea);
	}

	//メジャーチェックエリア番号にバインドされたチェックエリアリストを取得する
	public List<ParkourRegion> getCheckAreas(int majorCheckAreaNumber){
		return checkAreas.containsKey(majorCheckAreaNumber) ? checkAreas.get(majorCheckAreaNumber) : Collections.emptyList();
	}

	//チェックエリアをメジャーチェックエリア番号にバインドする
	public void bindCheckArea(int majorCheckAreaNumber, ParkourRegion checkArea){
		//チェックエリアリストを取得する
		List<ParkourRegion> areas = checkAreas.get(majorCheckAreaNumber);

		//リストが無ければ作成する
		if(areas == null) checkAreas.put(majorCheckAreaNumber, areas = new ArrayList<>());

		//リストにチェックエリアを追加する
		areas.add(checkArea);

		parkours.registerCheckArea(checkArea);
	}

	//指定されたチェックエリア番号のチェックエリアを書き換える
	public void setCheckArea(int majorCheckAreaNumber, int minorCheckAreaNumber, ParkourRegion checkArea){
		List<ParkourRegion> areas = getCheckAreas(majorCheckAreaNumber);

		//メジャーチェックエリア番号が未使用であれば戻る
		if(areas.isEmpty()) return;

		//マイナーチェックエリア番号が大きすぎれば戻る
		if(minorCheckAreaNumber >= areas.size()) return;

		//チェックエリアを書き換える
		ParkourRegion replacedCheckArea = areas.set(minorCheckAreaNumber, checkArea);

		parkours.unregisterCheckArea(replacedCheckArea);
		parkours.registerCheckArea(checkArea);
	}

	//チェックエリアをアンバインドする
	public void unbindCheckArea(ParkourRegion checkArea){
		int majorCheckAreaNumber = getMajorCheckAreaNumber(checkArea);

		//バインドされていないチェックエリアであれば戻る
		if(majorCheckAreaNumber <= -1) return;

		List<ParkourRegion> areas = getCheckAreas(majorCheckAreaNumber);

		areas.remove(checkArea);

		parkours.unregisterCheckArea(checkArea);
	}

	public void registerAll(){
		applyToAllCheckAreas(parkours::registerCheckArea);
	}

	public void unregisterAll(){
		applyToAllCheckAreas(parkours::unregisterCheckArea);
	}

	public void displayAll(){
		applyToAllCheckAreas(ParkourRegion::displayBorders);
	}

	public void undisplayAll(){
		applyToAllCheckAreas(ParkourRegion::undisplayBorders);
	}

	private void applyToAllCheckAreas(Consumer<ParkourRegion> applier){
		for(List<ParkourRegion> areas : checkAreas.values())
			for(ParkourRegion area : areas) applier.accept(area);
	}

	public void save(Yaml yaml, ImmutableBlockLocation origin){
		for(Entry<Integer, List<ParkourRegion>> checkAreasEntry : checkAreas.entrySet()){
			int majorCheckAreaNumber = checkAreasEntry.getKey();

			//チェックエリアをテキストデータにシリアライズする
			List<String> deserializedCheckAreas = checkAreasEntry.getValue().stream().map(ParkourRegion::serialize).collect(Collectors.toList());

			//指定階層にセットする
			yaml.set(StringTemplate.apply("Check areas.$0", majorCheckAreaNumber), deserializedCheckAreas);
		}
	}

}
