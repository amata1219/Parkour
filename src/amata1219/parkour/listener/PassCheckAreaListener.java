package amata1219.parkour.listener;

import org.bukkit.entity.Player;

import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.user.User;

public class PassCheckAreaListener extends PassRegionBoundaryAbstractListener {

	protected PassCheckAreaListener() {
		super(Parkours.getInstance().chunksToCheckAreasMap);
	}

	@Override
	public void onMove(Player player, User user, Parkour parkour, ParkourRegion from, ParkourRegion to) {
		boolean existsFrom = from != null;
		boolean existsTo = to != null;

		//チェックエリアに入った場合
		if(!existsFrom && existsTo){

		//チェックエリアから出た場合
		}else if(existsFrom && !existsTo){

		}
	}

}
