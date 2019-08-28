package amata1219.parkour.message;

import org.bukkit.entity.Player;

import amata1219.parkour.text.Text;

public class Title implements MessageStyle {

	public final int fadeIn, stay, fadeOut;

	public Title(int fadeIn, int stay, int fadeOut){
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
	}

	@Override
	public void sendTo(Player player, Text text) {
	}

}
