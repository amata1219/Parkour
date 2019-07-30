package amata1219.parkour.message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class Messenger {

	public static void sendMessage(Player player, String text){
		player.sendMessage(text);
	}

	public static void sendActionBarMessage(Player player, String text){
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
	}

	public static void sendLinkedMessage(Player player, String text, String url){
		TextComponent component = new TextComponent(text);
		component.setClickEvent(new ClickEvent(Action.OPEN_URL, url));
		player.spigot().sendMessage(ChatMessageType.CHAT, component);
	}

	public static void broadcastMessage(String text){
		for(Player player : Bukkit.getOnlinePlayers()){
			player.sendMessage(text);
		}
	}

	/*public static void sendMessage(Player player, String text){
		String message = selectMessageByLocale(player, text);
		player.sendMessage(message);
	}

	public static void sendActionBarMessage(Player player, String text){
		String message = selectMessageByLocale(player, text);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}

	public static void sendLinkedMessage(Player player, String text, String url){
		String message = selectMessageByLocale(player, text);
		TextComponent component = new TextComponent(message);
		component.setClickEvent(new ClickEvent(Action.OPEN_URL, url));
		player.spigot().sendMessage(ChatMessageType.CHAT, component);
	}

	public static void broadcastMessage(String text){
		for(Player player : Bukkit.getOnlinePlayers()){
			String message = selectMessageByLocale(player, text);
			player.sendMessage(message);
		}
	}

	private static String selectMessageByLocale(Player player, String message){
		return message.split("|")[player.getLocale().equals("ja_jp") ? 0 : 1];
	}*/

}
