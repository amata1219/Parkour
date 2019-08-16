package amata1219.parkour.ui.parkour;

import java.util.HashMap;

import amata1219.parkour.parkour.ParkourCategory;

public class ParkoursMenus {

	private static ParkoursMenus instance;

	//UpdateとExtendは対応しない
	private static ParkourCategory[] CATEGORIES = new ParkourCategory[]{ParkourCategory.NORMAL, ParkourCategory.SEGMENT, ParkourCategory.BIOME};

	public static void load(){
		instance = new ParkoursMenus();
	}

	public static ParkoursMenus getInstance(){
		return instance;
	}

	private final HashMap<ParkourCategory, ParkourSelectionUI> parkourSelectionUIMap = new HashMap<>(CATEGORIES.length);

	private ParkoursMenus(){
		for(ParkourCategory category : CATEGORIES) parkourSelectionUIMap.put(category, new ParkourSelectionUI(category));
	}

	//カテゴリーに対応したステージリストを取得する
	public ParkourSelectionUI getInventoryUI(ParkourCategory category){
		return parkourSelectionUIMap.get(category);
	}

}
