package amata1219.parkour.command.parkour;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.selection.RegionSelection;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.ParkourRegionSelector;
import amata1219.parkour.user.User;

public interface AbstractParkourCommand extends Command {

	@Override
	default void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender))
			return;

		//ユーザーを取得する
		User user = Main.getUserSet().getUser(sender.asPlayerCommandSender());

		//アスレ用の範囲選択オブジェクトを取得する
		ParkourRegionSelector selector = user.parkourRegionSelector;

		//範囲選択されていない場合
		if(selector == null){
			sender.warn(": State error > 範囲を選択して下さい。");
			return;
		}

		//範囲選択オブジェクトを取得する
		RegionSelection selection = selector.selection;

		//範囲選択されていない場合
		if(selection.boundaryCorner1.world == null || selection.boundaryCorner2.world == null){
			sender.warn(": State error > 範囲を選択して下さい。");
			return;
		}

		onCommand(sender, args, selector.parkour, selection);
	}

	void onCommand(Sender sender, Arguments args, Parkour parkour, RegionSelection selection);

}
