package amata1219.parkour.message;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundPlayer {

	public static void play(Player player, Sound sound, float volume, float pitch){
		player.playSound(player.getLocation(), sound, volume, pitch);
	}

}
