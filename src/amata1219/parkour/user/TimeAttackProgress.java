package amata1219.parkour.user;

import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.yaml.Yaml;

public class TimeAttackProgress {

	//タイムアタックが行われているパルクール
	public final Parkour parkour;

	//通過したチェックエリアの最大のメジャー番号を進捗度とする
	private int progress;

	public TimeAttackProgress(Parkour parkour){
		this.parkour = parkour;
	}

	public int getProgress(){
		return progress;
	}

	public void incrementProgress(){
		progress++;
	}

	public void save(Yaml yaml){
		yaml.set("TimeAttack.Parkour", parkour.name);
		yaml.set("TimeAttack.Progress", progress);
	}

}
