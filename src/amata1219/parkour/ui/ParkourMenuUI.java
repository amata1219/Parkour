package amata1219.parkour.ui;

import java.util.HashMap;

import amata1219.parkour.parkour.ParkourCategory;

public class ParkourMenuUI {

	private static ParkourMenuUI instance;

	public static void load(){
		instance = new ParkourMenuUI();
	}

	public static ParkourMenuUI getInstance(){
		return instance;
	}

	private final HashMap<ParkourCategory, CategorizedParkoursSelectionUI> uis = new HashMap<>(ParkourCategory.values().length);

	private ParkourMenuUI(){
		for(ParkourCategory category : ParkourCategory.values()) uis.put(category, new CategorizedParkoursSelectionUI(category));
	}

	//カテゴリーに対応したステージリストを取得する
	public CategorizedParkoursSelectionUI getInventoryUI(ParkourCategory category){
		return uis.get(category);
	}

}
