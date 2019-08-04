package amata1219.parkour.command.parkour;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Sender;
import amata1219.amalib.region.Region;
import amata1219.amalib.selection.RegionSelection;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.RegionWithBorders;

public class FinishLineCommand implements AbstractParkourCommand {

	@Override
	public void onCommand(Sender sender, Arguments args, Parkour parkour, RegionSelection selection) {
		Region region = new Region(selection.getLesserBoundaryCorner(), selection.getGreaterBoundaryCorner());
		RegionWithBorders finishLine = new RegionWithBorders(parkour, region);
		parkour.setFinishLine(finishLine);
		sender.info(": Success > フィニッシュラインを設定しました。");
	}

}
