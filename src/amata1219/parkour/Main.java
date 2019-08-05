package amata1219.parkour;

import org.bukkit.Bukkit;
import org.bukkit.World;
import amata1219.amalib.Plugin;
import amata1219.parkour.command.CheckAreaCommand;
import amata1219.parkour.command.CoinCommand;
import amata1219.parkour.command.FinishLineCommand;
import amata1219.parkour.command.GiveSelectionToolCommand;
import amata1219.parkour.command.ParkourCommand;
import amata1219.parkour.command.ParkourRegionCommand;
import amata1219.parkour.command.SetDirectionCommand;
import amata1219.parkour.command.StageCommand;
import amata1219.parkour.command.StartLineCommand;
import amata1219.parkour.listener.ControlFunctionalItemListener;
import amata1219.parkour.listener.DisablePlayerCollisionListener;
import amata1219.parkour.listener.DisplayRegionBorderListener;
import amata1219.parkour.listener.GiveVoteRewardCoinsListener;
import amata1219.parkour.listener.HideNewPlayerListener;
import amata1219.parkour.listener.IncrementJumpsListener;
import amata1219.parkour.listener.LoadUserDataListener;
import amata1219.parkour.listener.PassFinishLineListener;
import amata1219.parkour.listener.PassStartLineListener;
import amata1219.parkour.listener.SetCheckpointListener;
import amata1219.parkour.selection.RegionSelectionSet;
import amata1219.parkour.user.UserSet;
import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;

public class Main extends Plugin {

	//https://twitter.com/share?url=https://minecraft.jp/servers/azisaba.net&text=ここにテキスト
	//アスレTP時にチャットに送信
	/*
	 * update time played
	 * my state
	 * cancel damage
	 */

	private static Main plugin;
	private static BetterNickAPI nickAPI;

	@Override
	public void onEnable(){
		plugin = this;
		nickAPI = BetterNick.getApi();

		registerCommands(
			new CheckAreaCommand(),
			new CoinCommand(),
			new FinishLineCommand(),
			new GiveSelectionToolCommand(),
			new ParkourCommand(),
			new ParkourRegionCommand(),
			new SetDirectionCommand(),
			new StageCommand(),
			new StartLineCommand()
		);

		registerListeners(
			UserSet.getInstnace(),
			RegionSelectionSet.getInstance(),
			new ControlFunctionalItemListener(),
			new DisablePlayerCollisionListener(),
			new DisplayRegionBorderListener(),
			new GiveVoteRewardCoinsListener(),
			new HideNewPlayerListener(),
			new IncrementJumpsListener(),
			new LoadUserDataListener(),
			new PassFinishLineListener(),
			new PassStartLineListener(),
			new SetCheckpointListener()
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
