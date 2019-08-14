package amata1219.parkour;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;

import amata1219.amalib.Plugin;
import amata1219.parkour.command.CheckAreaCommand;
import amata1219.parkour.command.CoinCommand;
import amata1219.parkour.command.EditParkourCommand;
import amata1219.parkour.command.ParkourCommand;
import amata1219.parkour.command.RelayoutCommand;
import amata1219.parkour.command.TweetCommand;
import amata1219.parkour.command.DirectionCommand;
import amata1219.parkour.command.ParkourRegionCommand;
import amata1219.parkour.command.ParkourSettingCommand;
import amata1219.parkour.listener.ControlFunctionalItemListener;
import amata1219.parkour.listener.DisableDamageListener;
import amata1219.parkour.listener.DisableFoodLevelChangeListener;
import amata1219.parkour.listener.DisablePlayerCollisionListener;
import amata1219.parkour.listener.ControlRegionBorderDisplayerListener;
import amata1219.parkour.listener.GiveVoteRewardCoinsListener;
import amata1219.parkour.listener.HideNewPlayerListener;
import amata1219.parkour.listener.LoadUserDataListener;
import amata1219.parkour.listener.PassCheckAreaListener;
import amata1219.parkour.listener.PassFinishLineListener;
import amata1219.parkour.listener.PassStartLineListener;
import amata1219.parkour.listener.SetCheckpointListener;
import amata1219.parkour.listener.UnloadUserDataListener;
import amata1219.parkour.listener.UpdateInformationBoardListener;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.selection.RegionSelections;
import amata1219.parkour.task.AsyncTask;
import amata1219.parkour.task.SaveParkourDataTask;
import amata1219.parkour.task.SaveUserDataTask;
import amata1219.parkour.task.UpdatePingTask;
import amata1219.parkour.task.UpdateTimePlayedTask;
import amata1219.parkour.ui.parkour.ParkourMenuUI;
import amata1219.parkour.user.Users;

public class Main extends Plugin {

	//https://twitter.com/intent/tweet?text=ツイート本文
	//アスレクリア時やランクアップ時など
	//各動作に音を付ける

	private static Main plugin;

	private final ArrayList<AsyncTask> activeTasks = new ArrayList<>(5);

	@Override
	public void onEnable(){
		plugin = this;

		//インスタンスを生成する
		Parkours.load();
		Users.load();
		RegionSelections.load();
		ParkourMenuUI.load();
		ControlFunctionalItemListener.load();

		registerCommands(
			new ParkourCommand(),
			new ParkourRegionCommand(),
			new ParkourSettingCommand(),
			new EditParkourCommand(),
			new CheckAreaCommand(),
			new CoinCommand(),
			new RelayoutCommand(),
			new DirectionCommand(),
			new TweetCommand()
		);

		registerListeners(
			Users.getInstnace(),
			RegionSelections.getInstance(),
			ControlFunctionalItemListener.getInstance(),
			new DisablePlayerCollisionListener(),
			new ControlRegionBorderDisplayerListener(),
			new GiveVoteRewardCoinsListener(),
			new HideNewPlayerListener(),
			new LoadUserDataListener(),
			new PassFinishLineListener(),
			new PassStartLineListener(),
			new PassCheckAreaListener(),
			new SetCheckpointListener(),
			new DisableDamageListener(),
			new DisableFoodLevelChangeListener(),
			new UnloadUserDataListener(),
			new UpdateInformationBoardListener()
		);

		startTasks(
			new SaveParkourDataTask(),
			new SaveUserDataTask(),
			new UpdateTimePlayedTask(),
			new UpdatePingTask()
		);

		super.onEnable();
	}

	@Override
	public void onDisable(){
		super.onDisable();

		cancelTasks();

		Users.getInstnace().saveAll();
		Parkours.getInstance().saveAll();
	}

	public static Main getPlugin(){
		return plugin;
	}

	public static World getCreativeWorld(){
		return Bukkit.getWorld("Creative");
	}

	private void startTasks(AsyncTask... tasks){
		for(AsyncTask task : tasks){
			//稼働中のタスクリストに追加する
			activeTasks.add(task);

			task.start();
		}
	}

	private void cancelTasks(){
		for(AsyncTask task : activeTasks) task.cancel();

		//稼働中のタスクリストをクリアする
		activeTasks.clear();
	}

}
