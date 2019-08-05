package amata1219.parkour.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundPlayer {

	public static void play(Player player, Sound sound, float volume, float pitch){
		player.playSound(player.getLocation(), sound, volume, pitch);
	}

}
