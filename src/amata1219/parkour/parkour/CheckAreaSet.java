package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.List;

import amata1219.amalib.yaml.Yaml;

public class CheckAreaSet {

	private final ParkourSet parkourSet = ParkourSet.getInstance();
	private final List<RegionWithBorders> checkAreas = new ArrayList<>();

	public CheckAreaSet(Yaml yaml, Parkour parkour){
		for(String text : yaml.getStringList("Check areas"))
			checkAreas.add(RegionWithBorders.deserialize(parkour, text));
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

			parkourSet.unregisterRegionWithBorders(old, parkourSet.chunksToCheckAreasMap);

			old.undisplay();

		//新しいチェックエリアを追加する
		}else{
			checkAreas.add(checkArea);
		}

		parkourSet.registerRegionWithBorders(checkArea, parkourSet.chunksToCheckAreasMap);

		//新しいチェックエリアの境界線の描画を開始する
		checkArea.display();
	}

	public void display(){
		checkAreas.forEach(RegionWithBorders::display);
	}

	public void undisplay(){
		checkAreas.forEach(RegionWithBorders::undisplay);
	}

}
