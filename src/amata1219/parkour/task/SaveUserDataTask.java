package amata1219.parkour.task;

import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

public class SaveUserDataTask extends AsyncTask {

	private final Users users = Users.getInstnace();
	private int count;

	public SaveUserDataTask() {
		//12分毎にオンラインプレイヤー、1時間毎に全プレイヤーのデータをセーブする
		super(14400);
	}

	@Override
	public void run() {
		if(count++ >= 4){
			count = 0;
			users.saveAll();
		}else{
			users.getOnlineUsers().forEach(User::save);
		}

	}

}
