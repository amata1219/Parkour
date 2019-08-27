package amata1219.parkour.text;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.entity.Player;

import amata1219.parkour.tuplet.Tuple;

public interface TextStream {



	/*
	 * 下記の様な使い方を想定した設計になっています。
	 *
	 * Text("&b-$player_name-&7-: &f-$chat_message")
	 * .setAttribute("$player_name", player.getName())
	 * .setAttribute("$chat_message", message)
	 * .color()
	 * .map(Bukkit.getOnlinePlayers())
	 * .forEach(ActionBar::sendTo)
	 *
	 */

	default TextStream color(){
		return color('&');
	}

	//指定された代替カラーコードをセクションに置き換える
	TextStream color(char alternateColorCode);

	//対応した属性名を値に置き換える
	TextStream setAttribute(String name, Object value);

	default Collection<Tuple<Player, Text>> map(Player player){
		return map(Collections.singleton(player));
	}

	Collection<Tuple<Player, Text>> map(Collection<? extends Player> players);

}
