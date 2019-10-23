package amata1219.obsolete.parkour.task;

import amata1219.obsolete.parkour.user.UserSet;

public class UpdateTimePlayedTask extends AsyncTask {

	public UpdateTimePlayedTask() {
		super(36000);
	}

	@Override
	public void run() {
		UserSet.getInstnace().getOnlineUsers().forEach(user -> user.statusBoard.updateTimePlayed());
	}

}
