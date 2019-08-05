package amata1219.parkour.ui.stage;

import java.util.HashMap;

import amata1219.parkour.stage.StageCategory;

public class StagesUISet {

	private static StagesUISet instance;

	public static StagesUISet getInstance(){
		return instance != null ? instance : (instance = new StagesUISet());
	}

	private final HashMap<StageCategory, StagesUI> stageUIs = new HashMap<>(StageCategory.values().length);

	private StagesUISet(){
		for(StageCategory category : StageCategory.values()) stageUIs.put(category, new StagesUI(category));
	}

	//カテゴリーに対応したステージリストを取得する
	public StagesUI getStagesUI(StageCategory category){
		return stageUIs.get(category);
	}

}
