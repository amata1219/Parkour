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
import amata1219.parkour.listener.IncrementJumpsListener;
import amata1219.parkour.listener.ApplyUserStateListener;
import amata1219.parkour.listener.PassFinishLineListener;
import amata1219.parkour.listener.PassStartLineListener;
import amata1219.parkour.listener.SetCheckpointListener;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.selection.RegionSelectionSet;
import amata1219.parkour.stage.StageSet;
import amata1219.parkour.task.AsyncTask;
import amata1219.parkour.task.UpdatePingTask;
import amata1219.parkour.task.UpdateTimePlayedTask;
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

	private final ArrayList<AsyncTask> activeTasks = new ArrayList<>(2);

	@Override
	public void onEnable(){
		plugin = this;
		nickAPI = BetterNick.getApi();

		//インスタンスを生成する
		ParkourSet.load();
		StageSet.load();
		UserSet.load();
		RegionSelectionSet.load();

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
			UserSet.getInstnace(),
			RegionSelectionSet.getInstance(),
			new ControlFunctionalItemListener(),
			new DisablePlayerCollisionListener(),
			new DisplayRegionBorderListener(),
			new GiveVoteRewardCoinsListener(),
			new HideNewPlayerListener(),
			new IncrementJumpsListener(),
			new ApplyUserStateListener(),
			new PassFinishLineListener(),
			new PassStartLineListener(),
			new SetCheckpointListener(),
			new DisableDamageListener(),
			new DisableFoodLevelChangeListener()
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
