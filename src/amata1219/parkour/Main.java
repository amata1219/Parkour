package amata1219.parkour;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import amata1219.amalib.Plugin;
import amata1219.parkour.command.CheckAreaCommand;
import amata1219.parkour.command.CoinCommand;
import amata1219.parkour.command.GiveSelectionToolCommand;
import amata1219.parkour.command.ParkourCommand;
import amata1219.parkour.command.SetDirectionCommand;
import amata1219.parkour.command.SetFinishLineCommand;
import amata1219.parkour.command.SetParkourRegionCommand;
import amata1219.parkour.command.SetStartLineCommand;
import amata1219.parkour.command.StageCommand;
import amata1219.parkour.listener.ControlFunctionalItemListener;
import amata1219.parkour.listener.DisableDamageListener;
import amata1219.parkour.listener.DisableFoodLevelChangeListener;
import amata1219.parkour.listener.DisablePlayerCollisionListener;
import amata1219.parkour.listener.DisplayRegionBorderListener;
import amata1219.parkour.listener.GiveVoteRewardCoinsListener;
import amata1219.parkour.listener.HideNewPlayerListener;
import amata1219.parkour.listener.LoadUserDataListener;
import amata1219.parkour.listener.PassFinishLineListener;
import amata1219.parkour.listener.PassStartLineListener;
import amata1219.parkour.listener.SetCheckpointListener;
import amata1219.parkour.listener.UnloadUserDataListener;
import amata1219.parkour.listener.UpdateInformationBoardListener;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.selection.RegionSelections;
import amata1219.parkour.stage.Stages;
import amata1219.parkour.task.AsyncTask;
import amata1219.parkour.task.UpdatePingTask;
import amata1219.parkour.task.UpdateTimePlayedTask;
import amata1219.parkour.user.Users;

public class Main extends Plugin {

	//https://twitter.com/share?url=https://minecraft.jp/servers/azisaba.net&text=ここにテキスト
	//アスレクリア時やランクアップ時など
	//各動作に音を付ける

	private static Main plugin;

	private final ArrayList<AsyncTask> activeTasks = new ArrayList<>(2);

	@Override
	public void onEnable(){
		plugin = this;

		//インスタンスを生成する
		Parkours.load();
		Stages.load();
		Users.load();
		RegionSelections.load();

		registerCommands(
			new StageCommand(),
			new ParkourCommand(),
			new GiveSelectionToolCommand(),
			new SetParkourRegionCommand(),
			new SetStartLineCommand(),
			new SetFinishLineCommand(),
			new CheckAreaCommand(),
			new CoinCommand(),
			new SetDirectionCommand()
		);

		registerListeners(
			Users.getInstnace(),
			RegionSelections.getInstance(),
			new ControlFunctionalItemListener(),
			new DisablePlayerCollisionListener(),
			new DisplayRegionBorderListener(),
			new GiveVoteRewardCoinsListener(),
			new HideNewPlayerListener(),
			new LoadUserDataListener(),
			new PassFinishLineListener(),
			new PassStartLineListener(),
			new SetCheckpointListener(),
			new DisableDamageListener(),
			new DisableFoodLevelChangeListener(),
			new UnloadUserDataListener(),
			new UpdateInformationBoardListener()
		);

		startTasks(
			new UpdateTimePlayedTask(),
			new UpdatePingTask()
		);

		super.onEnable();
	}

	@Override
	public void onDisable(){
		super.onDisable();

		cancelTasks();

		//Users.getInstance().～
		Parkours.getInstance().saveAll();
		Stages.getInstance().saveAll();
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
