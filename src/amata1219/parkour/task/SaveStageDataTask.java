package amata1219.parkour.task;

import amata1219.parkour.stage.Stages;

public class SaveStageDataTask extends AsyncTask {

	public SaveStageDataTask() {
		//1時間毎にセーブする
		super(72000);
	}

	@Override
	public void run() {
		Stages.getInstance().saveAll();
	}

}
