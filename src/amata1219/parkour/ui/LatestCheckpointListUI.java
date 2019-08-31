package amata1219.parkour.ui;

import amata1219.parkour.user.User;

public class LatestCheckpointListUI extends AbstractCheckpointListUI {

	public LatestCheckpointListUI(User user) {
		super(
			user,
			japanise -> japanise ? "最新" : "Latest",
			(parkour, checkpoints) -> checkpoints.getLatestCheckpoint(parkour),
			(parkour, checkpoints) -> checkpoints.getLatestCheckpointNumber(parkour)
		);
	}

}
