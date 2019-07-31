package amata1219.parkour.command;

import org.bukkit.inventory.Inventory;
import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.parkour.Main;

public class CheckSignCommand implements Command {

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender))
			return;

		Inventory inventory = sender.asPlayerCommandSender().getInventory();
		switch(args.next()){
		case "sign":
			inventory.addItem(Main.AT_SIGN);
			sender.info(": Given > CP@SIGN看板を付与しました。");
			return;
		case "player":
			inventory.addItem(Main.AT_PLAYER);
			sender.info(": Given > CP@PLAYER看板を付与しました。");
			return;
		default:
			return;
		}
	}

}
