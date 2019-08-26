package amata1219.parkour.text;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface TextStream {

	default public TextStream color(){
		return color('&');
	}

	//指定された代替カラーコードをセクションに置き換える
	public TextStream color(char alternateColorCode);

	//対応した属性名を値に置き換える
	public TextStream setAttribute(String name, Object value);

	default public void sendTo(Player player){
		sendTo(player, MessageType.CHAT);
	}

	public void sendTo(Player player, MessageType type);

	default public void sendTo(Collection<? extends Player> players){
		sendTo(players, MessageType.CHAT);
	}

	default public void sendTo(Collection<? extends Player> players, MessageType type){
		players.forEach(player -> sendTo(player, type));
	}

	default public void broadcast(){
		broadcast(MessageType.CHAT);
	}

	default public void broadcast(MessageType type){
		sendTo(Bukkit.getOnlinePlayers(), type);
	}

	public static enum MessageType {

		CHAT,
		ACTION_BAR;

	}

}
