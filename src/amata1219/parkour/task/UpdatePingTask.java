package amata1219.parkour.task;

import amata1219.parkour.user.Users;

public class UpdatePingTask extends AsyncTask {

	public UpdatePingTask() {
		super(1200);
	}

	@Override
	public void run() {
		Users.getInstnace().getOnlineUsers().forEach(user -> user.statusBoard.updatePing());
	}

}
