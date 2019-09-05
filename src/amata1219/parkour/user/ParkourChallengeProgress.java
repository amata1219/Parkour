package amata1219.parkour.user;

public class ParkourChallengeProgress {

	private int currentCheckAreanumber;

	public ParkourChallengeProgress(){

	}

	public ParkourChallengeProgress(int currentCheckAreaNumber){
		this.currentCheckAreanumber = currentCheckAreaNumber;
	}

	public int currentCheckAreaNumber(){
		return currentCheckAreanumber;
	}

	public void incrementCurrentCheckAreaNumber(){
		currentCheckAreanumber++;
	}

}
