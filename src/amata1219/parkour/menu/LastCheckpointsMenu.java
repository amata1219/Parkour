package amata1219.parkour.menu;

import amata1219.parkour.user.User;

public class LastCheckpointsMenu extends AbstractCheckpointSelectionUI {

	public LastCheckpointsMenu(User user) {
		super(user, "Last", (checkpoints, parkour) -> checkpoints.getLastCheckpoint(parkour), (checkpoints, parkour) -> checkpoints.getLastCheckpointNumber(parkour));
	}

}
