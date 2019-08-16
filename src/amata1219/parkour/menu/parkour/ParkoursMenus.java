package amata1219.parkour.menu.parkour;

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

	private final HashMap<ParkourCategory, ParkoursMenu> parkourSelectionUIMap = new HashMap<>(CATEGORIES.length);

	private ParkoursMenus(){
		for(ParkourCategory category : CATEGORIES) parkourSelectionUIMap.put(category, new ParkoursMenu(category));
	}

	//カテゴリーに対応したステージリストを取得する
	public ParkoursMenu getInventoryUI(ParkourCategory category){
		return parkourSelectionUIMap.get(category);
	}

}
