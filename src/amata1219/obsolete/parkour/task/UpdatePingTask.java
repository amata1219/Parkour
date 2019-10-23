package amata1219.obsolete.parkour.task;

import amata1219.obsolete.parkour.user.UserSet;

public class UpdatePingTask extends AsyncTask {

	public UpdatePingTask() {
		super(1200);
	}

	@Override
	public void run() {
		UserSet.getInstnace().getOnlineUsers().forEach(user -> user.statusBoard.updatePing());
	}

}
