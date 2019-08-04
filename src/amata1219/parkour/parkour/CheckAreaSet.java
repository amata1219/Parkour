package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import amata1219.amalib.region.Region;
import amata1219.amalib.yaml.Yaml;

public class CheckAreaSet {

	private final ParkourSet parkourSet = ParkourSet.getInstance();
	private final List<RegionWithBorders> checkAreas = new ArrayList<>();

	public CheckAreaSet(Yaml yaml, Parkour parkour){
		for(String text : yaml.getStringList("Check areas"))
			checkAreas.add(new RegionWithBorders(parkour, Region.deserialize(parkour.world, text)));
	}

	public int getCheckAreaNumber(RegionWithBorders checkArea){
		return checkAreas.indexOf(checkArea);
	}

	public RegionWithBorders getCheckArea(int checkAreaNumber){
		return checkAreaNumber < checkAreas.size() ? checkAreas.get(checkAreaNumber) : null;
	}

	public void addCheckArea(RegionWithBorders checkArea){
		setCheckArea(checkAreas.size(), checkArea);
	}

	public void setCheckArea(int checkAreaNumber, RegionWithBorders checkArea){
		//既存のチェックエリアの新しいそれに書き換える
		if(checkAreaNumber < checkAreas.size()){
			//既存のチェックエリアを取得する
			RegionWithBorders old = checkAreas.set(checkAreaNumber, checkArea);

			//登録を解除する
			parkourSet.unregisterCheckArea(old);

			//境界線の表示を止める
			old.undisplay();

		//新しいチェックエリアを追加する
		}else{
			checkAreas.add(checkArea);
		}

		//新しいチェックエリアを登録する
		parkourSet.registerCheckArea(checkArea);

		//新しいチェックエリアの境界線の描画を開始する
		checkArea.display();
	}

	public void display(){
		checkAreas.forEach(RegionWithBorders::display);
	}

	public void undisplay(){
		checkAreas.forEach(RegionWithBorders::undisplay);
	}

	public void registerAll(){
		checkAreas.forEach(parkourSet::registerCheckArea);
	}

	public void unregisterAll(){
		checkAreas.forEach(parkourSet::unregisterCheckArea);
	}

	public void save(Yaml yaml){
		List<String> data = checkAreas.stream().map(checkArea -> checkArea.region.serialize()).collect(Collectors.toList());
		yaml.set("Check areas", data);
	}

}
