package amata1219.parkour.command;

import org.bukkit.Bukkit;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.inventory.ui.InventoryLine;

public class TestCommand implements Command {

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender)) return;

		if(!args.hasNextInt()){
			sender.warn("/test [インベントリの段数]");
			return;
		}

		int line = args.nextInt();

		sender.asPlayerCommandSender().openInventory(Bukkit.createInventory(null, InventoryLine.necessaryInventoryLine(line * 9).inventorySize()));
	}

}
