package amata1219.obsolete.parkour.task;

import amata1219.obsolete.parkour.parkour.ParkourSet;

public class SaveParkourDataTask extends AsyncTask {

	public SaveParkourDataTask() {
		//30分毎にセーブする
		super(36000);
	}

	@Override
	public void run() {
		ParkourSet.getInstance().saveAll();
	}

}
