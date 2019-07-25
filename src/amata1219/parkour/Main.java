package amata1219.parkour;

import amata1219.amalib.Plugin;
import amata1219.parkour.command.SetDirectionCommand;
import amata1219.parkour.listener.CreateUserInstanceListener;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.stage.StageSet;
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
			new SetDirectionCommand()
		);

		registerListeners(
			new CreateUserInstanceListener()
		);

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

	public static UserSet getUserSet(){
		return userSet;
	}

}
