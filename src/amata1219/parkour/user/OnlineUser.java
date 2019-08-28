package amata1219.parkour.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import amata1219.parkour.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.ui.LastCheckpointSelectionUI;
import amata1219.parkour.ui.LatestCheckpointSelectionUI;
import amata1219.parkour.ui.UIType;
import amata1219.parkour.ui.parkour.CommonParkourSelectionUI;

public class OnlineUser {

	public final User user;
	public final Player player;
	public final StatusBoard board;
	private final ImmutableMap<UIType, InventoryUI> inventoryUIMap;

	public OnlineUser(User user){
		this.user = user;
		player = Bukkit.getPlayer(user.uuid);
		board = new StatusBoard(user);
		inventoryUIMap = new ImmutableMap.Builder<UIType, InventoryUI>()
		.put(UIType.NORMAL_PARKOURS, new CommonParkourSelectionUI(user, ParkourCategory.NORMAL))
		.put(UIType.SEGMENT_PARKOURS, new CommonParkourSelectionUI(user, ParkourCategory.SEGMENT))
		.put(UIType.BIOME_PARKOURS, new CommonParkourSelectionUI(user, ParkourCategory.BIOME))
		.put(UIType.UPDATE_PARKOURS, new CommonParkourSelectionUI(user, ParkourCategory.UPDATE))
		.put(UIType.EXTEND_PARKOURS, new CommonParkourSelectionUI(user, ParkourCategory.EXTEND))
		.put(UIType.LAST_CHECKPOINTS, new LastCheckpointSelectionUI(user))
		.put(UIType.LATEST_CHECKPOINTS, new LatestCheckpointSelectionUI(user))
		.build();
	}

}
