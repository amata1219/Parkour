package amata1219.parkour.parkour;

import java.util.List;
import java.util.stream.Collectors;

import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.yaml.Yaml;

public class CheckAreas {

	private final Parkours parkours = Parkours.getInstance();

	public final List<ParkourRegion> areas;

	public CheckAreas(Yaml yaml, Parkour parkour, ImmutableBlockLocation origin){
		areas = yaml.getStringList("Check areas").stream()
							.map(text -> new ParkourRegion(parkour, origin.add(Region.deserialize(text))))
							.collect(Collectors.toList());
	}

	public int getCheckAreaNumber(ParkourRegion checkArea){
		return areas.indexOf(checkArea);
	}

	public ParkourRegion getCheckArea(int checkAreaNumber){
		return checkAreaNumber < areas.size() ? areas.get(checkAreaNumber) : null;
	}

	public int addCheckArea(ParkourRegion checkArea){
		areas.add(checkArea);

		//新しいチェックエリアを登録する
		parkours.registerCheckArea(checkArea);

		return areas.size() - 1;
	}

	public void setCheckArea(int checkAreaNumber, ParkourRegion checkArea){
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
	}

}
