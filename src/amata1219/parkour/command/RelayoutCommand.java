package amata1219.parkour.command;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.parkour.listener.ControlFunctionalItemListener;

public class RelayoutCommand implements Command {

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//プレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		//ホットバーのアイテムを再配置する
		ControlFunctionalItemListener.getInstance().initializeSlots(sender.asPlayerCommandSender());

		sender.displayMessageToActionbar = true;
		sender.info("Relayout items on the hotbar");
	}

}
