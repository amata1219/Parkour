package amata1219.parkour.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.selection.RegionSelectionSet;

public class GiveSelectionToolCommand implements Command {

	private final RegionSelectionSet selections = RegionSelectionSet.getInstance();
	private final ParkourSet parkourSet = ParkourSet.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//プレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		//アスレ名が指定されていなければ戻る
		if(!args.hasNext()){
			sender.warn("範囲選択をするアスレの名前を指定して下さい。");
			return;
		}

		//第1引数をアスレ名として取得する
		String parkourName = args.next();

		if(!parkourSet.existsFile(parkourName)){
			
		}

		//送信者をプレイヤーとして取得する
		Player player = sender.asPlayerCommandSender();

		UUID uuid = player.getUniqueId();

		selections.setNewSelection(uuid, parkour);

		//範囲選択ツール
		sender.asPlayerCommandSender().getInventory().addItem(RegionSelectionSet.selectionTool);
	}

}
