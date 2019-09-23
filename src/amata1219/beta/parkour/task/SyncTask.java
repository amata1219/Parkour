package amata1219.beta.parkour.task;

import java.util.function.Consumer;

import org.bukkit.Bukkit;

import amata1219.masquerade.Masquerade;

public abstract class SyncTask extends Task {
	
	public static SyncTask define(Runnable processing){
		return define(self -> processing.run());
	}

	public static SyncTask define(Consumer<SyncTask> processing){
		SyncTask task = new SyncTask(){

			@Override
			public void exe() {
				processing.accept(this);
			}

		};
		return task;
	}
	
	@Override
	public void executeTimer(long delay, long period){
		activeTask = Bukkit.getScheduler().runTaskTimer(Masquerade.plugin(), this, delay, period);
	}

}
