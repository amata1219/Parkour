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
	public void sendTo(Player player) {
		localize(player).sendTo(player);
	}

	@Override
	public void actionbar(Player player) {
		localize(player).actionbar(player);
	}

	@Override
	public void title(String subTitle, int fadeIn, int stay, int fadeOut, Player player) {
		localize(player).title(subTitle, fadeIn, stay, fadeOut, player);
	}

	//使用言語に対応したTextを返す
	public TextStream localize(Player player){
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
