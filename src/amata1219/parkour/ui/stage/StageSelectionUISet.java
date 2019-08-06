package amata1219.parkour.ui.stage;

import java.util.HashMap;

import amata1219.parkour.stage.StageCategory;

public class StageSelectionUISet {

	private static StageSelectionUISet instance;

	public static StageSelectionUISet getInstance(){
		return instance != null ? instance : (instance = new StageSelectionUISet());
	}

	private final HashMap<StageCategory, StageSelectionUI> stageUIs = new HashMap<>(StageCategory.values().length);

	private StageSelectionUISet(){
		for(StageCategory category : StageCategory.values()) stageUIs.put(category, new StageSelectionUI(category));
	}

	//カテゴリーに対応したステージリストを取得する
	public StageSelectionUI getStagesUI(StageCategory category){
		return stageUIs.get(category);
	}

}
