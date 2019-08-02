package amata1219.parkour.user;

import org.bukkit.configuration.ConfigurationSection;

import amata1219.amalib.yaml.Yaml;

public class UserSetting {

	public boolean hideTraceurs;

	public boolean displayScoreboard;
	public boolean displayTraceur;
	public boolean displayUpdateRank;
	public boolean displayExtendRank;
	public boolean displayJumps;
	public boolean displayCoins;
	public boolean displayTimePlayed;
	public boolean displayOnlinePlayers;
	public boolean displayPing;
	public boolean displayServerAddress;

	public UserSetting(Yaml yaml){
		hideTraceurs = yaml.getBoolean("Hide traceurs");

		ConfigurationSection section = yaml.getConfigurationSection("Values displayed on scoreboard");

		displayScoreboard = section.getBoolean("Scoreboard");
		displayTraceur = section.getBoolean("Traceur");
		displayUpdateRank = section.getBoolean("Update rank");
		displayExtendRank = section.getBoolean("Extend rank");
		displayJumps = section.getBoolean("Jumps");
		displayCoins = section.getBoolean("Coins");
		displayTimePlayed = section.getBoolean("Time played");
		displayOnlinePlayers = section.getBoolean("Online players");
		displayPing = section.getBoolean("Ping");
		displayServerAddress = section.getBoolean("Server address");
	}

}
