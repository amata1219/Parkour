package amata1219.beta.parkour;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

	/*
	 * コマンド登録
	 * ～Command→getCommand("～").setExecutor(new ～Command());
	 *
	 * リスナ登録
	 * Join/Quitリスナ対応
	 * →優先度で事故ってる
	 *
	 * エンチャント登録
	 *
	 * UI強制クローズ
	 * →Player#closeInventory()で問題無い
	 */

	@Override
	public void onEnable(){

	}

	@Override
	public void onDisable(){

	}

}
