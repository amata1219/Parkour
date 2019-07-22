package amata1219.parkour.user;

import amata1219.amalib.yaml.Yaml;

public class UserSetting {

	public boolean hideUsers;

	public UserSetting(Yaml yaml){
		hideUsers = yaml.getBoolean("Hide users");
	}

}
