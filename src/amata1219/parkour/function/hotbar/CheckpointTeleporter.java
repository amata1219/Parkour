package amata1219.parkour.function.hotbar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.parkour.enchantment.GleamEnchantment;
import amata1219.parkour.location.ImmutableLocation;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.user.CheckpointSet;
import amata1219.parkour.user.User;

public class CheckpointTeleporter implements FunctionalItem {

	@Override
	public void onClick(User user, ClickType click) {
		Localizer localizer = user.localizer;
		Player player = user.asBukkitPlayer();

		//アスレをプレイ中でなければ戻る
		if(!user.isPlayingParkour()){
			localizer.mcolor("&c-アスレチックのプレイ中でないためテレポート出来ません | &c-You can't teleport because you aren't playing parkour now").displayOnActionBar(player);
			return;
		}

		//プレイ中のアスレを取得する
		Parkour parkour = user.parkourPlayingNow;
		CheckpointSet checkpoints = user.checkpoints;

		if(!checkpoints.containsParkour(parkour)){
			localizer.mcolor("&c-チェックポイントが設定されていないためテレポート出来ません | &c-You can't teleport because you have not set any checkpoints").displayOnActionBar(player);
			return;
		}

		//右クリックしたのであれば最終チェックポイントを、左クリックしたのであれば最新チェックポイントを取得する
		ImmutableLocation checkpoint = click == ClickType.RIGHT ? checkpoints.getLastCheckpoint(parkour) : checkpoints.getLatestCheckpoint(parkour);

		//チェックポイントが無ければ戻る
		if(checkpoint == null){
			localizer.mcolor("&c-チェックポイントが設定されていないためテレポート出来ません | &c-You can't teleport because you have not set any checkpoints").displayOnActionBar(player);
			return;
		}

		//チェックエリアの番号を取得し表示用に+1する
		int displayCheckAreaNumber = (click == ClickType.RIGHT ? checkpoints.getLastCheckpointNumber(parkour) : checkpoints.getLatestCheckpointNumber(parkour)) + 1;

		//チェックポイントにテレポートさせる
		player.teleport(checkpoint.asBukkit());

		localizer.mapplyAll("&b-チェックポイント$2にテレポートしました | $b-Teleported to Checkpoint$2", displayCheckAreaNumber).displayOnActionBar(player);
	}

	@Override
	public ItemStack build(User user) {
		Localizer localizer = user.localizer;

		ItemStack item = new ItemStack(Material.LIGHT_BLUE_DYE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(localizer.color("&b-最新/最終チェックポイントにテレポートする | &b-Teleport to Latest Checkpoint &7- @ Left Click &8-/ &b-Teleport to Last Checkpoint &7-@ Right Click"));

		item.setItemMeta(meta);

		//プレイヤーがチェックエリア内にいれば輝かせる
		if(user.onCheckArea) GleamEnchantment.gleam(item);

		return item;
	}

	@Override
	public boolean isSimilar(ItemStack item) {
		return item != null && item.getType() == Material.LIGHT_BLUE_DYE;
	}

}
