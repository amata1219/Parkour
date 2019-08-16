package amata1219.parkour.menu;

import amata1219.parkour.user.User;

public class LatestCheckpointsMenu extends AbstractCheckpointSelectionUI {

	public LatestCheckpointsMenu(User user) {
		super(user, "Latest", (checkpoints, parkour) -> checkpoints.getLatestCheckpoint(parkour), (checkpoints, parkour) -> checkpoints.getLatestCheckpointNumber(parkour));
	}

}
