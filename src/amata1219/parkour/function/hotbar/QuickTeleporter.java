package amata1219.parkour.function.hotbar;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.location.ImmutableLocation;
import amata1219.amalib.string.StringLocalize;
import amata1219.amalib.string.message.MessageColor;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.Checkpoints;
import amata1219.parkour.user.User;

public class QuickTeleporter implements FunctionalHotbarItem {

	@Override
	public void onClick(User user, ClickType click) {
		Player player = user.asBukkitPlayer();

		//アスレをプレイ中でなければ戻る
		if(!user.isPlayingWithParkour()){
			MessageColor.color("&c-アスレチックのプレイ中でないため実行出来ません | &c-?")
			.localize().displayOnActionBar(player);
			return;
		}

		//プレイ中のアスレを取得する
		Parkour parkour = user.parkourPlayingNow;
		Checkpoints checkpoints = user.checkpoints;

		if(!checkpoints.containsParkour(parkour)){
			MessageColor.color("&c-チェックポイントが設定されていないため実行出来ません | &c-?t")
			.localize().displayOnActionBar(player);
			return;
		}

		//右クリックしたのであれば最終チェックポイントを、左クリックしたのであれば最新チェックポイントを取得する
		ImmutableLocation checkpoint = click == ClickType.RIGHT ? checkpoints.getLastCheckpoint(parkour) : checkpoints.getLatestCheckpoint(parkour);

		//チェックポイントが無ければ戻る
		if(checkpoint == null){
			MessageColor.color("&c-チェックポイントが設定されていないため実行出来ません | &c-?")
			.localize().displayOnActionBar(player);
			return;
		}

		//チェックエリアの番号を取得し表示用に+1する
		int displayCheckAreaNumber = (click == ClickType.RIGHT ? checkpoints.getLastCheckpointNumber(parkour) : checkpoints.getLatestCheckpointNumber(parkour)) + 1;

		//チェックポイントにテレポートさせる
		player.teleport(checkpoint.asBukkit());

		//表示例: Update7 の チェックポイント1 にテレポートしました
		MessageTemplate.capply("$1-&b-の チェックポイント$0 にテレポートしました | &b-?", displayCheckAreaNumber, parkour.name)
		.localize().displayOnActionBar(player);
	}

	@Override
	public ItemStack build(User user) {
		//ユーザーに対応したプレイヤーを取得する
		Player player = user.asBukkitPlayer();

		ItemStack item = new ItemStack(Material.LIGHT_BLUE_DYE);

		ItemMeta meta = item.getItemMeta();

		//使用言語に対応したテキストを表示名に設定する
		meta.setDisplayName(StringLocalize.capply("&b-最新/最終チェックポイントにテレポートする | &b-?", player));

		//使用言語に対応したテキストを説明文に設定する
		meta.setLore(Arrays.asList(
			StringLocalize.capply("&7-左クリックすると最新チェックポイントにテレポートします。 | &7-?", player),
			StringLocalize.capply("&7-右クリックすると最終チェックポイントにテレポートします。 | &7-?", player)
		));

		item.setItemMeta(meta);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.LIGHT_BLUE_DYE;
	}

}
