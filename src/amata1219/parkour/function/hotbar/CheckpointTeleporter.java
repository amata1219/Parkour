package amata1219.parkour.function.hotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.parkour.enchantment.GleamEnchantment;
import amata1219.parkour.location.ImmutableLocation;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.text.BilingualText;
import amata1219.parkour.user.CheckpointSet;
import amata1219.parkour.user.User;

public class CheckpointTeleporter implements FunctionalItem {

	@Override
	public void onClick(User user, ClickType click) {
		Player player = user.asBukkitPlayer();

		//アスレをプレイ中でなければ戻る
		if(!user.isOnCurrentParkour()){
			BilingualText.stream("&c-アスレチックのプレイ中でないためテレポート出来ません",
					"&c-You can't teleport because you aren't playing parkour now")
					.color()
					.setReceiver(player)
					.sendActionBarMessage();
			return;
		}

		//プレイ中のアスレを取得する
		Parkour parkour = user.parkourPlayingNow;
		CheckpointSet checkpoints = user.checkpoints;

		if(!checkpoints.containsParkour(parkour)){
			BilingualText.stream("&c-チェックポイントが設定されていないためテレポート出来ません",
					"&c-You can't teleport because you have not set any checkpoints")
					.color()
					.setReceiver(player)
					.sendActionBarMessage();
			return;
		}

		//右クリックしたのであれば最終チェックポイントを、左クリックしたのであれば最新チェックポイントを取得する
		ImmutableLocation checkpoint = click == ClickType.RIGHT ? checkpoints.getLastCheckpoint(parkour) : checkpoints.getLatestCheckpoint(parkour);

		//チェックポイントが無ければ戻る
		if(checkpoint == null){
			BilingualText.stream("&c-チェックポイントが設定されていないためテレポート出来ません",
					"&c-You can't teleport because you have not set any checkpoints")
					.color()
					.setReceiver(player)
					.sendActionBarMessage();
			return;
		}

		//チェックエリアの番号を取得し表示用に+1する
		int displayCheckAreaNumber = (click == ClickType.RIGHT ? checkpoints.getLastCheckpointNumber(parkour) : checkpoints.getLatestCheckpointNumber(parkour)) + 1;

		//チェックポイントにテレポートさせる
		player.teleport(checkpoint.asBukkit());

		BilingualText.stream("$colorチェックポイント$numberにテレポートしました", "$colorTeleported to checkpoint$number")
		.setAttribute("$color", parkour.prefixColor)
		.setAttribute("$number", displayCheckAreaNumber)
		.setReceiver(player)
		.sendActionBarMessage();
	}

	@Override
	public ItemStack build(User user) {
		ItemStack item = new ItemStack(Material.LIGHT_BLUE_DYE);

		ItemMeta meta = item.getItemMeta();

		//最新@左 最終@右
		String displayName = BilingualText.stream("&b-チェックポイントにテレポートする &7-(最新 @ 左 / 最終 @ 右)",
				"&b-Teleport to Checkpoint &7-(Latest @ L / Last @ R)")
				.textBy(user.asBukkitPlayer())
				.color()
				.toString();

		meta.setDisplayName(displayName);

		item.setItemMeta(meta);

		//プレイヤーがチェックエリア内にいれば輝かせる
		if(user.isOnCheckArea()) GleamEnchantment.gleam(item);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.LIGHT_BLUE_DYE;
	}

}
