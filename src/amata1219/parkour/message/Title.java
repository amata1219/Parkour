package amata1219.parkour.message;

import org.bukkit.entity.Player;

public class Title implements Messenger {

	private String title, subtitle;
	private int fadeIn, stay, fadeOut;

	public Title(){

	}

	@Override
	public void sendTo(Player player, String message) {
	}

}
