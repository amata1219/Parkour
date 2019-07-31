package amata1219.parkour.command;

import org.bukkit.inventory.Inventory;
import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.parkour.item.CheckSign;

public class CheckSignCommand implements Command {

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender))
			return;

		Inventory inventory = sender.asPlayerCommandSender().getInventory();
		switch(args.next()){
		case "sign":
			inventory.addItem(CheckSign.AT_SIGN);
			sender.info(": Success > CP @ SIGN 看板を付与しました。");
			return;
		case "player":
			inventory.addItem(CheckSign.AT_PLAYER);
			sender.info(": Success > CP @ PLAYER 看板を付与しました。");
			return;
		default:
			return;
		}
	}

}
