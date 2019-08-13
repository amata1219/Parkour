package amata1219.parkour.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import amata1219.amalib.sound.SoundMetadata;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.user.User;

public class PassCheckAreaListener extends PassRegionBoundaryAbstractListener {

	private static final SoundMetadata IN_SE = new SoundMetadata(Sound.UI_BUTTON_CLICK, 0.8f, 2f);
	private static final SoundMetadata OUT_SE = new SoundMetadata(Sound.UI_BUTTON_CLICK, 1.2f, 0.5f);

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

}
