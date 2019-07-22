package amata1219.parkour;

import amata1219.amalib.Plugin;

public class Main extends Plugin {

	private static Main plugin;

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

}
