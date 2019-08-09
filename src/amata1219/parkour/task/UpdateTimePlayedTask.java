package amata1219.parkour.task;

import amata1219.parkour.user.Users;

public class UpdateTimePlayedTask extends AsyncTask {

	public UpdateTimePlayedTask() {
		super(36000);
	}

	@Override
	public void run() {
		Users.getInstnace().getOnlineUsers().forEach(user -> user.board.updateTimePlayed());
	}

}
