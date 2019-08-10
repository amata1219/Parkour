package amata1219.parkour.ui;

import java.util.HashMap;

import amata1219.parkour.stage.StageCategory;

public class ParkourMenuUI {

	private static ParkourMenuUI instance;

	public static ParkourMenuUI getInstance(){
		return instance != null ? instance : (instance = new ParkourMenuUI());
	}

	private final HashMap<StageCategory, CategorizedParkoursSelectionUI> stageUIs = new HashMap<>(StageCategory.values().length);

	private ParkourMenuUI(){
		for(StageCategory category : StageCategory.values()) stageUIs.put(category, new CategorizedParkoursSelectionUI(category));
	}

	//カテゴリーに対応したステージリストを取得する
	public CategorizedParkoursSelectionUI getStagesUI(StageCategory category){
		return stageUIs.get(category);
	}

}
