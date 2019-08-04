package amata1219.parkour.command.parkour;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Sender;
import amata1219.amalib.region.Region;
import amata1219.amalib.selection.RegionSelection;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.RegionWithBorders;

public class StartLineCommand implements AbstractParkourCommand {

	@Override
	public void onCommand(Sender sender, Arguments args, Parkour parkour, RegionSelection selection) {
		Region region = new Region(selection.getLesserBoundaryCorner(), selection.getGreaterBoundaryCorner());
		RegionWithBorders startLine = new RegionWithBorders(parkour, region);
		parkour.setStartLine(startLine);
		sender.info(": Success > スタートラインを設定しました。");
	}

}
