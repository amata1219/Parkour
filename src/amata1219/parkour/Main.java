package amata1219.parkour;

import org.bukkit.Bukkit;
import org.bukkit.World;
import amata1219.amalib.Plugin;
import amata1219.parkour.command.CoinCommand;
import amata1219.parkour.command.ParkourCommand;
import amata1219.parkour.command.RegionSelectorCommand;
import amata1219.parkour.command.SetDirectionCommand;
import amata1219.parkour.command.StageCommand;
import amata1219.parkour.listener.CreateUserInstanceListener;
import amata1219.parkour.listener.DisablePlayerCollisionsListener;
import amata1219.parkour.listener.DisplayRegionBorderListener;
import amata1219.parkour.listener.InteractCheckSignListener;
import amata1219.parkour.listener.PassFinishLineListener;
import amata1219.parkour.listener.PassStartLineListener;
import amata1219.parkour.listener.PlaceCheckSignListener;
import amata1219.parkour.listener.SelectRegionListener;
import amata1219.parkour.listener.SetCheckpointListener;
import amata1219.parkour.listener.ToggleShowPlayersListener;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.stage.StageSet;
import amata1219.parkour.user.SaveUserDataTask;
import amata1219.parkour.user.UserSet;

public class Main extends Plugin {

	//https://twitter.com/share?url=https://minecraft.jp/servers/azisaba.net&text=ここにテキスト
	//アスレTP時にチャットに送信

	private static Main plugin;
	private static StageSet stageSet;
	private static ParkourSet parkourSet;
	private static UserSet userSet;

	@Override
	public void onEnable(){
		plugin = this;

		parkourSet = new ParkourSet();
		stageSet = new StageSet();
		userSet = new UserSet();

		registerCommands(
			new ParkourCommand(),
			new StageCommand(),
			new CoinCommand(),
			new SetDirectionCommand(),
			new RegionSelectorCommand()
		);

		registerListeners(
			new CreateUserInstanceListener(),
			new DisablePlayerCollisionsListener(),
			new SetCheckpointListener(),
			new PassStartLineListener(),
			new PassFinishLineListener(),
			new SelectRegionListener(),
			new DisplayRegionBorderListener(),
			new PlaceCheckSignListener(),
			new InteractCheckSignListener(),
			new ToggleShowPlayersListener()
		);

		SaveUserDataTask.run();
	}

	@Override
	public void onDisable(){
		super.onDisable();

		SaveUserDataTask.cancel();
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

	public static UserSet getUserSet(){
		return userSet;
	}

	public static World getCreativeWorld(){
		return Bukkit.getWorld("Creative");
	}

}
