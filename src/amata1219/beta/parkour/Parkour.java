package amata1219.beta.parkour;

import org.bukkit.plugin.java.JavaPlugin;

import graffiti.Command;

public class Parkour extends JavaPlugin {

	private static Parkour plugin;
	
	@Override
	public void onEnable(){
		plugin = this;
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public static Parkour plugin(){
		return plugin;
	}
	
	private void registerCommands(Command... commands){
		for(Command command : commands){
			String className = command.getClass().getSimpleName();
			String commandName = className.substring(0, className.length() - 7).toLowerCase();
			getCommand(commandName).setExecutor(command);
		}
	}
	
	private void registerListeners(){
		
	}
}
