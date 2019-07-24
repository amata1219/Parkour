package amata1219.parkour;

import amata1219.amalib.Plugin;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.stage.StageSet;

public class Main extends Plugin {

	private static Main plugin;
	private static StageSet stageSet;
	private static ParkourSet parkourSet;

	@Override
	public void onEnable(){
		plugin = this;

		registerCommands(

		);

		registerListeners(

		);

		/*
		 * ロードの順番
		 *
		 * 1. パルクール
		 * 2. ステージ
		 * 3. ユーザー
		 *
		 */
	}

	@Override
	public void onDisable(){
		super.onDisable();
	}

	public static Main getPlugin(){
		return plugin;
	}

	public static StageSet getStageSet(){
		return stageSet;
	}

	public static ParkourSet getParkourSet(){
		return parkourSet;
	}

}
