package amata1219.parkour.listener;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import amata1219.amalib.sound.SoundMetadata;
import amata1219.parkour.function.hotbar.ControlFunctionalHotbarItem;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.user.User;

public class PassCheckAreaListener extends PassRegionBoundaryAbstractListener {

	private static final SoundMetadata IN_SE = new SoundMetadata(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 2f);
	private static final SoundMetadata OUT_SE = new SoundMetadata(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 0.5f);

	public PassCheckAreaListener() {
		super(Parkours.getInstance().chunksToCheckAreasMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, ParkourRegion from, ParkourRegion to) {
		boolean existsFrom = from != null;
		boolean existsTo = to != null;

		//チェックエリアに入った場合
		if(!existsFrom && existsTo){
			user.onCheckArea = true;

			IN_SE.play(player);

			//通知アイテムを輝かせる
			ControlFunctionalHotbarItem.updateSlot(player, 0);
		//チェックエリアから出た場合
		}else if(existsFrom && !existsTo){
			user.onCheckArea = false;

			OUT_SE.play(player);

			//通知アイテムの輝きを失わせる
			ControlFunctionalHotbarItem.updateSlot(player, 0);
		}
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		Player player = event.getPlayer();

		//プラグインによるテレポートであれば通知アイテムを更新する
		if(player.getGameMode() != GameMode.CREATIVE && event.getCause() == TeleportCause.PLUGIN) ControlFunctionalHotbarItem.updateSlot(player, 0);
	}

}
