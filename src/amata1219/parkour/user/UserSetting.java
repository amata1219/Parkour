package amata1219.parkour.user;

import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.yaml.Yaml;

public class UserSetting {

	public boolean hideUsers;

	public boolean displayScoreboard;
	public boolean displayUpdateRank;
	public boolean displayExtendRank;
	public boolean displayJumps;
	public boolean displayCoins;
	public boolean displayPlayerName;
	public boolean displayOnlinePlayers;
	public boolean displayTimePlayed;
	public boolean displayPing;

	public UserSetting(Yaml yaml){
		hideUsers = yaml.getBoolean("Hide users");

		ConfigurationSection section = yaml.getConfigurationSection("Information displayed on scoreboard");

		displayScoreboard = section.getBoolean("Scoreboard");
		displayUpdateRank = section.getBoolean("Update rank");
		displayExtendRank = section.getBoolean("Extend rank");
		displayJumps = section.getBoolean("Jumps");
		displayCoins = section.getBoolean("Coins");
		displayPlayerName = section.getBoolean("Player name");
		displayOnlinePlayers = section.getBoolean("Online players");
		displayTimePlayed = section.getBoolean("Time played");
		displayPing = section.getBoolean("Ping");
	}

}
