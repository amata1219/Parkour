package amata1219.parkour.parkour;

import java.util.List;
import java.util.stream.Collectors;

import amata1219.amalib.region.Region;
import amata1219.amalib.yaml.Yaml;

public class CheckAreas {

	private final Parkours parkours = Parkours.getInstance();

	public final List<ParkourRegion> checkAreas;

	public CheckAreas(Parkour parkour, Yaml yaml){
		checkAreas = yaml.getStringList("Check areas").stream()
							.map(text -> new ParkourRegion(parkour, Region.deserializeToCorners(text)))
							.collect(Collectors.toList());
	}

	public int getCheckAreaNumber(ParkourRegion checkArea){
		return checkAreas.indexOf(checkArea);
	}

	public ParkourRegion getCheckArea(int checkAreaNumber){
		return checkAreaNumber < checkAreas.size() ? checkAreas.get(checkAreaNumber) : null;
	}

	public void addCheckArea(ParkourRegion checkArea){
		setCheckArea(checkAreas.size(), checkArea);
	}

	public void setCheckArea(int checkAreaNumber, ParkourRegion checkArea){
		//既存のチェックエリアの新しいそれに書き換える
		if(checkAreaNumber < checkAreas.size()){
			//既存のチェックエリアを取得する
			ParkourRegion old = checkAreas.get(checkAreaNumber);

			//登録を解除する
			parkours.unregisterCheckArea(old);

			//チェックエリアを書き換える
			checkAreas.set(checkAreaNumber, checkArea);

		//新しいチェックエリアを追加する
		}else{
			checkAreas.add(checkArea);
		}

		//新しいチェックエリアを登録する
		parkours.registerCheckArea(checkArea);
	}

	public void displayAll(){
		checkAreas.forEach(ParkourRegion::displayBorders);
	}

	public void undisplayAll(){
		checkAreas.forEach(ParkourRegion::undisplayBorders);
	}

	public void registerAll(){
		checkAreas.forEach(parkours::registerCheckArea);
	}

	public void unregisterAll(){
		checkAreas.forEach(parkours::unregisterCheckArea);
	}

	public void save(Yaml yaml){
		yaml.set("Check areas", checkAreas.stream().map(ParkourRegion::serialize).collect(Collectors.toList()));
	}

}
