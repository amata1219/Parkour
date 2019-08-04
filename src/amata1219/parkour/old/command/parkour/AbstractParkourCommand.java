package amata1219.parkour.old.command.parkour;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.selection.RegionSelection;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

public abstract class AbstractParkourCommand implements Command {

	protected final UserSet userSet = UserSet.getInstnace();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//送信者がプレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		//ユーザーを取得する
		User user = userSet.getUser(sender.asPlayerCommandSender());

		//アスレ用の範囲選択オブジェクトを取得する
		ParkourRegionSelection selector = user.parkourRegionSelector;

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

	abstract void onCommand(Sender sender, Arguments args, Parkour parkour, RegionSelection selection);

}
