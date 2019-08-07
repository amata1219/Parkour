package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import amata1219.amalib.region.Region;
import amata1219.amalib.yaml.Yaml;

public class CheckAreas {

	private final Parkours parkourSet = Parkours.getInstance();
	private final List<OldParkourRegion> checkAreas = new ArrayList<>();

	public CheckAreas(Yaml yaml, Parkour parkour){
		for(String text : yaml.getStringList("Check areas"))
			checkAreas.add(new OldParkourRegion(parkour, Region.deserialize(parkour.world, text)));
	}

	public boolean isEmpty(){
		return checkAreas.isEmpty();
	}

	public List<OldParkourRegion> getCheckAreas(){
		return new ArrayList<>(checkAreas);
	}

	public int getCheckAreaNumber(OldParkourRegion checkArea){
		return checkAreas.indexOf(checkArea);
	}

	public OldParkourRegion getCheckArea(int checkAreaNumber){
		return checkAreaNumber < checkAreas.size() ? checkAreas.get(checkAreaNumber) : null;
	}

	public void addCheckArea(OldParkourRegion checkArea){
		setCheckArea(checkAreas.size(), checkArea);
	}

	public void setCheckArea(int checkAreaNumber, OldParkourRegion checkArea){
		//既存のチェックエリアの新しいそれに書き換える
		if(checkAreaNumber < checkAreas.size()){
			//既存のチェックエリアを取得する
			OldParkourRegion old = checkAreas.get(checkAreaNumber);

			//登録を解除する
			parkourSet.unregisterCheckArea(old);

			//チェックエリアを書き換える
			checkAreas.set(checkAreaNumber, checkArea);

		//新しいチェックエリアを追加する
		}else{
			checkAreas.add(checkArea);
		}

		//新しいチェックエリアを登録する
		parkourSet.registerCheckArea(checkArea);
	}

	public void displayAll(){
		checkAreas.forEach(OldParkourRegion::display);
	}

	public void undisplayAll(){
		checkAreas.forEach(OldParkourRegion::undisplay);
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
