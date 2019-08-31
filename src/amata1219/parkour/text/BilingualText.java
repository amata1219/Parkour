package amata1219.parkour.text;

import org.bukkit.entity.Player;

public class BilingualText implements TextStream {

	private final Text japanise, english;

	public BilingualText(String japanise, String english){
		this.japanise = new Text(japanise);
		this.english = new Text(english);
	}

	public static BilingualText stream(String japanise, String english){
		return new BilingualText(japanise, english);
	}

	@Override
	public TextStream color(char alternateColorCode) {
		japanise.color(alternateColorCode);
		english.color(alternateColorCode);
		return this;
	}

	@Override
	public TextStream setAttribute(String name, Object value) {
		japanise.setAttribute(name, value);
		english.setAttribute(name, value);
		return this;
	}

	@Override
	public Text textBy(Player receiver) {
		return localize(receiver);
	}

	//使用言語に対応したTextを返す
	public Text localize(Player player){
		return isJapanise(player) ? japanise : english;
	}

	//使用言語が日本語か判定する
	private boolean isJapanise(Player player){
		return player.getLocale().equals("ja_jp");
	}

	//使用言語に対応したStringを返す
	public String toString(Player player){
		return localize(player).toString();
	}

}
