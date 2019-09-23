package amata1219.beta.parkour.task;

import java.util.function.Consumer;

import org.bukkit.Bukkit;

import amata1219.masquerade.Masquerade;

public abstract class AsyncTask extends Task {
	
	public static AsyncTask define(Runnable processing){
		return define(self -> processing.run());
	}

	public static AsyncTask define(Consumer<AsyncTask> processing){
		AsyncTask task = new AsyncTask(){

			@Override
			public void exe() {
				processing.accept(this);
			}

		};
		return task;
	}
	
	@Override
	public void executeTimer(long delay, long period){
		activeTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Masquerade.plugin(), this, delay, period);
	}
	
}
