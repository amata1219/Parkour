package amata1219.parkour.user;

import java.util.HashMap;

import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.ui.LastCheckpointSelectionUI;
import amata1219.parkour.ui.LatestCheckpointSelectionUI;
import amata1219.parkour.ui.MyProfileUI;
import amata1219.parkour.ui.parkour.CommonParkourSelectionUI;
import amata1219.parkour.ui.parkour.RankedParkourSelectionUI;

public class InventoryUIs {

	private static final ParkourCategory[] COMMON_CATEGORIES = new ParkourCategory[]{ParkourCategory.NORMAL, ParkourCategory.SEGMENT, ParkourCategory.BIOME};

	private final Player player;
	private final InventoryUI myProfileUI;
	private final InventoryUI lastCheckpointSelectionUI;
	private final InventoryUI latestCheckpointSelectionUI;
	private final HashMap<ParkourCategory, InventoryUI> parkourSelectionUIs = new HashMap<>(5);

	public InventoryUIs(User user){
		player = user.asBukkitPlayer();

		myProfileUI = new MyProfileUI(user);
		lastCheckpointSelectionUI = new LastCheckpointSelectionUI(user);
		latestCheckpointSelectionUI = new LatestCheckpointSelectionUI(user);

		for(ParkourCategory category : COMMON_CATEGORIES) parkourSelectionUIs.put(category, new CommonParkourSelectionUI(user, category));

		parkourSelectionUIs.put(ParkourCategory.UPDATE, new RankedParkourSelectionUI(user, ParkourCategory.UPDATE, () -> user.getUpdateRank()));
		parkourSelectionUIs.put(ParkourCategory.EXTEND, new RankedParkourSelectionUI(user, ParkourCategory.EXTEND, () -> user.getExtendRank()));
	}

	public void openMyProfileUI(){
		myProfileUI.openInventory(player);
	}

	public void openLastCheckpointSelectionUI(){
		lastCheckpointSelectionUI.openInventory(player);
	}

	public void openLatestCheckpointSelectionUI(){
		latestCheckpointSelectionUI.openInventory(player);
	}

	public void openParkourSelectionUI(ParkourCategory category){
		parkourSelectionUIs.get(category).openInventory(player);
	}

}
