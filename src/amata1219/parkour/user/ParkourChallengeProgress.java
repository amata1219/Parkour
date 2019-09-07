package amata1219.parkour.user;

public class ParkourChallengeProgress {

	private int currentCheckAreanumber = Integer.MAX_VALUE;

	public ParkourChallengeProgress(){

	}

	public ParkourChallengeProgress(int currentCheckAreaNumber){
		this.currentCheckAreanumber = Integer.MAX_VALUE;
	}

	public int currentCheckAreaNumber(){
		return currentCheckAreanumber;
	}

	public void incrementCurrentCheckAreaNumber(){
		currentCheckAreanumber++;
	}

}
