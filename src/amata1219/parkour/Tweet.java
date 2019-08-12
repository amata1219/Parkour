package amata1219.parkour;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import amata1219.amalib.sound.SoundMetadata;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.Message;
import amata1219.amalib.string.message.Message.ClickAction;
import amata1219.amalib.string.message.Message.HoverAction;
import amata1219.amalib.string.tweet.IntentTweet;

public class Tweet {

	private static final SoundMetadata SE = new SoundMetadata(Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.75f);

	public static void display(Player player, String text){
		//テキストにアジ鯖のハッシュタグを追加してビルドする
		String tweet = IntentTweet.write(text).addHashtag("アジ鯖").build();

		SE.play(player);

		Message.wrap(StringTemplate.capply("&b-# Share on Twitter &7-@ &f-$0", text)).displayAsClickableAndHoverable(player, ClickAction.OPEN_URL, tweet, HoverAction.SHOW_TEXT, "&7-: &b-Click to tweet!");
	}

}
