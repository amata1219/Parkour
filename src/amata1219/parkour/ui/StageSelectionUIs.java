package amata1219.parkour.ui;

import java.util.HashMap;

import amata1219.parkour.stage.StageCategory;

public class StageSelectionUIs {

	private static StageSelectionUIs instance;

	public static StageSelectionUIs getInstance(){
		return instance != null ? instance : (instance = new StageSelectionUIs());
	}

	private final HashMap<StageCategory, StageSelectionUI> stageUIs = new HashMap<>(StageCategory.values().length);

	private StageSelectionUIs(){
		for(StageCategory category : StageCategory.values()) stageUIs.put(category, new StageSelectionUI(category));
	}

	//カテゴリーに対応したステージリストを取得する
	public StageSelectionUI getStagesUI(StageCategory category){
		return stageUIs.get(category);
	}

}
