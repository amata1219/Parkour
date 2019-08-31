package amata1219.parkour.ui;

import amata1219.parkour.user.User;

public class LastCheckpointListUI extends AbstractCheckpointListUI {

	public LastCheckpointListUI(User user) {
		super(
			user,
			new LocaleFunction("最終", "Last"),
			(parkour, checkpoints) -> checkpoints.getLastCheckpoint(parkour),
			(parkour, checkpoints) -> checkpoints.getLastCheckpointNumber(parkour)
		);
	}

}
