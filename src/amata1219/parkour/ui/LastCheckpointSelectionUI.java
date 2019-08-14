package amata1219.parkour.ui;

import amata1219.parkour.user.User;

public class LastCheckpointSelectionUI extends AbstractCheckpointSelectionUI {

	public LastCheckpointSelectionUI(User user) {
		super(user, "Last", (checkpoints, parkour) -> checkpoints.getLastCheckpoint(parkour), (checkpoints, parkour) -> checkpoints.getLastCheckpointNumber(parkour));
	}

}
