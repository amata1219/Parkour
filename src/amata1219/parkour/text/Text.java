package amata1219.parkour.text;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Text implements TextStream {

	private static final String COLORS = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
	private static final String NULL = String.valueOf(Character.MIN_VALUE);

	private String text;

	public Text(String text){
		this.text = text;
	}

	@Override
	public TextStream color(char alternateColorCode) {
		//文字列を1文字ずつに分解する
		char[] characters = text.toCharArray();

		//各文字に対して処理をする
		for(int i = 0; i < characters.length - 1; i++){
			char color = characters[i + 1];

			//装飾コードでなければ戻る
			if(characters[i] != alternateColorCode || COLORS.indexOf(color) <= -1) continue;

			if(i > 0 && characters[i - 1] == '-') characters[i - 1] = Character.MIN_VALUE;

			characters[i] = '§';
			characters[i + 1] = Character.toLowerCase(color);

			if(i < characters.length - 2 && characters[i + 2] == '-'){
				characters[i + 2] = Character.MIN_VALUE;
				i += 2;
			}else{
				i++;
			}
		}

		text = new String(characters).replace(NULL, "");
		return this;
	}

	@Override
	public TextStream setAttribute(String name, Object value) {
		text = text.replaceAll(name, value.toString());
		return this;
	}

	@Override
	public void sendTo(Player player, MessageType type) {
		switch(type){
		case CHAT:
			player.sendMessage(text);
			break;
		case ACTION_BAR:
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
			break;
		default:
			break;
		}
	}

	@Override
	public String toString(){
		return text;
	}

}
