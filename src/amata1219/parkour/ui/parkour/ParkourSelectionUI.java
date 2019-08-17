package amata1219.parkour.ui.parkour;

import java.util.stream.Collectors;

import amata1219.amalib.inventory.ui.InventoryLine;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.user.User;

public class ParkourSelectionUI extends AbstractParkourSelectionUI<Parkour> {

	public ParkourSelectionUI(User user, ParkourCategory category) {
		super(
			user,
			category,
			() -> Parkours.getInstance().getEnabledParkours(category).collect(Collectors.toList()),
			parkours -> InventoryLine.necessaryInventoryLine(parkours.size() + 9),
			layout -> {}
		);
	}

}
