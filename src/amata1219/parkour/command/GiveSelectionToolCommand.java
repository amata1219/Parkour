package amata1219.parkour.command;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.string.StringTemplate;
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

		//コンフィグが存在しなければ戻る
		if(!parkourSet.existsFile(parkourName)){
			sender.warn(StringTemplate.applyWithColor("$0-&r-&c-は存在しません。", parkourName));
			return;
		}

		//送信者をプレイヤーとして取得する
		Player player = sender.asPlayerCommandSender();

		UUID uuid = player.getUniqueId();

		//新しいセレクションを作成する
		selections.setNewSelection(uuid, parkourName);

		//対応した範囲選択ツールを作成する
		ItemStack selectionTool = selections.makeNewSelectionTool(uuid);

		player.getInventory().addItem(selectionTool);

		sender.info(StringTemplate.applyWithColor("$0-&r-&b-用の範囲選択ツールを与えました。", parkourName));
		return;
	}

}
