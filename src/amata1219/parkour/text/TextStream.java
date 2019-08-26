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

	public void sendTo(Player player);

	default public void sendTo(Collection<? extends Player> players){
		players.forEach(this::sendTo);
	}

	default public void broadcast(){
		sendTo(Bukkit.getOnlinePlayers());
	}

}
