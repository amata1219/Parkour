package amata1219.parkour.task;

import amata1219.parkour.user.UserSet;

public class UpdatePingTask extends AsyncTask {

	public UpdatePingTask() {
		super(3000);
	}

	@Override
	public void run() {
		UserSet.getInstnace().getOnlineUsers().forEach(user -> user.board.updatePing());
	}

}
