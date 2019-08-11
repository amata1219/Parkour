package amata1219.parkour.ui.parkour;

import java.util.HashMap;

import amata1219.parkour.parkour.ParkourCategory;

public class ParkourMenuUI {

	private static ParkourMenuUI instance;

	//UpdateとExtendは対応しない
	private static ParkourCategory[] CATEGORIES = new ParkourCategory[]{ParkourCategory.NORMAL, ParkourCategory.SEGMENT, ParkourCategory.BIOME};

	public static void load(){
		instance = new ParkourMenuUI();
	}

	public static ParkourMenuUI getInstance(){
		return instance;
	}

	private final HashMap<ParkourCategory, ParkourSelectionUI> parkourSelectionUIMap = new HashMap<>(CATEGORIES.length);

	private ParkourMenuUI(){
		for(ParkourCategory category : CATEGORIES) parkourSelectionUIMap.put(category, new ParkourSelectionUI(category));
	}

	//カテゴリーに対応したステージリストを取得する
	public ParkourSelectionUI getInventoryUI(ParkourCategory category){
		return parkourSelectionUIMap.get(category);
	}

}
