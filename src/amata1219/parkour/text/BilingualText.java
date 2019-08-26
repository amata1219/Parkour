package amata1219.parkour.text;

import org.bukkit.entity.Player;

public class BilingualText implements TextStream {

	private final Text japanise, english;

	public BilingualText(String japanise, String english){
		this.japanise = new Text(japanise);
		this.english = new Text(english);
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
	public void sendTo(Player player) {
		if(isJapanise(player)) japanise.sendTo(player);
		else english.sendTo(player);
	}

	//使用言語に対応したTextを返す
	public Text localize(Player player){
		return isJapanise(player) ? japanise : english;
	}

	//使用言語に対応したStringを返す
	public String toString(Player player){
		return localize(player).toString();
	}

	//使用言語が日本語か判定する
	private boolean isJapanise(Player player){
		return player.getLocale().equals("ja_jp");
	}

}
