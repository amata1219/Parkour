package amata1219.parkour.ui;

import amata1219.parkour.user.User;

public class LatestCheckpointSelectionUI extends AbstractCheckpointSelectionUI {

	public LatestCheckpointSelectionUI(User user) {
		super(user, "最新 | Latest", (checkpoints, parkour) -> checkpoints.getLatestCheckpoint(parkour), (checkpoints, parkour) -> checkpoints.getLatestCheckpointNumber(parkour));
	}

}
