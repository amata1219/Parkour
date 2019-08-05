package amata1219.parkour.command;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.parkour.selection.RegionSelectionSet;

public class GiveSelectionToolCommand implements Command {

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender)) return;

		sender.asPlayerCommandSender().getInventory().addItem(RegionSelectionSet.selectionTool);
	}

}
