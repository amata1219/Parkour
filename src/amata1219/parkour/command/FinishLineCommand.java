package amata1219.parkour.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.selection.RegionSelectionSet;

public class FinishLineCommand implements Command {

	private final ParkourSet parkourSet = ParkourSet.getInstance();
	private final RegionSelectionSet selections = RegionSelectionSet.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//プレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		Player player = sender.asPlayerCommandSender();
		UUID uuid = player.getUniqueId();

		if(!selections.hasSelection(uuid)){
			sender.warn("範囲を選択して下さい。");
			return;
		}

		//第1引数をアスレ名として取得する
		String parkourName = args.next();

		//ファイルを取得する
		Yaml yaml = parkourSet.makeYaml(parkourName);

		RegionSelection selection = selections.getSelection(uuid);

		yaml.set("Finish line", selection.toString());

		yaml.save();

		sender.info("領域を設定しました。");
	}

}
