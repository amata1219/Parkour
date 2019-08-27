package amata1219.parkour.message;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar implements Messenger {

	@Override
	public void sendTo(Player player, String message) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}

}
