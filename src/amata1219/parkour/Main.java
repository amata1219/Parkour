package amata1219.parkour;

import org.bukkit.Bukkit;
import org.bukkit.World;
import amata1219.amalib.Plugin;
import amata1219.parkour.selection.RegionSelectionSet;
import amata1219.parkour.user.UserSet;
import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;

public class Main extends Plugin {

	//https://twitter.com/share?url=https://minecraft.jp/servers/azisaba.net&text=ここにテキスト
	//アスレTP時にチャットに送信

	private static Main plugin;
	private static BetterNickAPI nickAPI;

	@Override
	public void onEnable(){
		plugin = this;
		nickAPI = BetterNick.getApi();

		registerCommands(
		);

		registerListeners(
			UserSet.getInstnace(),
			RegionSelectionSet.getInstance()
		);
	}

	@Override
	public void onDisable(){
		super.onDisable();
	}

	public static Main getPlugin(){
		return plugin;
	}

	public static BetterNickAPI getNickAPI(){
		return nickAPI;
	}

	public static World getCreativeWorld(){
		return Bukkit.getWorld("Creative");
	}

}
