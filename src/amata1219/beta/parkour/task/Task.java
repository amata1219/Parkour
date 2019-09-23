package amata1219.beta.parkour.task;

import org.bukkit.scheduler.BukkitTask;

public abstract class Task implements Runnable {
	
	protected BukkitTask activeTask;
	private long count;

	@Override
	public void run(){
		exe();
		count++;
	}

	public abstract void exe();
	
	public void execute(){
		executeLater(0);
	}

	public void executeLater(long delay){
		executeTimer(delay, -1);
	}

	public void executeTimer(long delay){
		executeTimer(delay, delay);
	}

	public abstract void executeTimer(long delay, long period);

	public long count(){
		return count;
	}

	public void cancel(){
		if(!isCancelled()){
			activeTask.cancel();
			activeTask = null;
		}
	}

	public boolean isCancelled(){
		return activeTask == null;
	}


}
