package amata1219.parkour.text;

import java.util.Collection;

import org.bukkit.entity.Player;

import amata1219.parkour.message.Messenger;

public interface TextStream {

	default TextStream color(){
		return color('&');
	}

	//指定された代替カラーコードをセクションに置き換える
	TextStream color(char alternateColorCode);

	//対応した属性名を値に置き換える
	TextStream setAttribute(String name, Object value);

	void sendTo(Player player, Messenger messenger);

	default void sendTo(Collection<Player> players, Messenger messenger){
		players.forEach(player -> sendTo());
	}

}
