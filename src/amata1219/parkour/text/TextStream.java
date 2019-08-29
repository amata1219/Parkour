package amata1219.parkour.text;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import amata1219.parkour.command.Sender;
import amata1219.parkour.message.Messenger;
import amata1219.parkour.tuplet.Tuple;

public interface TextStream {

	/*
	 * 下記の様な使い方を想定した設計にしています。
	 *
	 * Text.stream("&b-$player_name-&7-: &f-$text")
	 * .setAttribute("player_name", player.getName())
	 * .setAttribute("text", text)
	 * .color()
	 * .setTargets(Bukkit.getOnlinePlayers())
	 * .sendActionBarMessage();
	 */

	default TextStream color(){
		return color('&');
	}

	//指定された代替カラーコードを§に置き換える
	TextStream color(char alternateColorCode);

	//対応した属性名を値に置き換える
	TextStream setAttribute(String name, Object value);

	//テキストを送信するプレイヤーを設定する
	default Messenger setReceiver(Sender target){
		return setReceivers(Collections.singletonList(target));
	}

	default Messenger setReceivers(Collection<Sender> receivers){
		//各プレイヤーとテキストをマップする
		Collection<Tuple<Sender, Text>> tuples = receivers.stream()
				.map(player -> new Tuple<Sender, Text>(player, correspondingTo(player)))
				.collect(Collectors.toList());

		return new Messenger(tuples);
	}

	@Deprecated
	Text correspondingTo(Sender receiver);

}
