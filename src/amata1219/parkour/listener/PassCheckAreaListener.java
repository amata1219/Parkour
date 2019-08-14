package amata1219.parkour.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import amata1219.amalib.sound.SoundMetadata;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.user.User;

public class PassCheckAreaListener extends PassRegionBoundaryAbstractListener {

	private static final SoundMetadata IN_SE = new SoundMetadata(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 2f);
	private static final SoundMetadata OUT_SE = new SoundMetadata(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 0.5f);

	private final ControlFunctionalItemListener controlFunctionalItemListener = ControlFunctionalItemListener.getInstance();

	public PassCheckAreaListener() {
		super(Parkours.getInstance().chunksToCheckAreasMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, ParkourRegion from, ParkourRegion to) {
		boolean existsFrom = from != null;
		boolean existsTo = to != null;

		//チェックエリアに入った場合
		if(!existsFrom && existsTo){
			IN_SE.play(player);

			//通知アイテムを発光させる
			controlFunctionalItemListener.setNotifierGleam(player, true);
		//チェックエリアから出た場合
		}else if(existsFrom && !existsTo){
			OUT_SE.play(player);

			//通知アイテムの発光を削除する
			controlFunctionalItemListener.setNotifierGleam(player, false);
		}
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		//プラグインによるテレポートでなければ戻る
		if(event.getCause() != TeleportCause.PLUGIN) return;

		Player player = event.getPlayer();
		Location location = player.getLocation();

		//今いるチャンク内にあるチェックエリアのリストを取得する
		List<ParkourRegion> areas = Parkours.getInstance().chunksToCheckAreasMap.get(location);

		//今いるチェックエリア
		ParkourRegion result = null;

		for(ParkourRegion area : areas){
			//チェックエリア内でなければ繰り返す
			if(!area.isIn(location)) continue;

			result = area;
			break;
		}

		//チェックエリア内かどうかで通知アイテムの発光を制御する
		controlFunctionalItemListener.setNotifierGleam(player, result != null);
	}

}
