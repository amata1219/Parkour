package amata1219.obsolete.parkour.ui.parkour;

import java.util.stream.Collectors;

import amata1219.obsolete.parkour.inventory.ui.InventoryLine;
import amata1219.obsolete.parkour.parkour.Parkour;
import amata1219.obsolete.parkour.parkour.ParkourCategory;
import amata1219.obsolete.parkour.parkour.ParkourSet;
import amata1219.obsolete.parkour.user.User;

public class CommonParkourListUI extends AbstractParkourListUI<Parkour> {

	public CommonParkourListUI(User user, ParkourCategory category) {
		super(
			user,
			category,
			() -> ParkourSet.getInstance().getEnabledParkours(category).collect(Collectors.toList()),
			parkours -> InventoryLine.necessaryInventoryLine(parkours.size() + 9),
			layout -> {}
		);
	}

}
