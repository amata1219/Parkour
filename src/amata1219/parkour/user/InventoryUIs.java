package amata1219.parkour.user;

import java.util.HashMap;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.ui.LastCheckpointSelectionUI;
import amata1219.parkour.ui.LatestCheckpointSelectionUI;
import amata1219.parkour.ui.MyMenuUI;
import amata1219.parkour.ui.parkour.ParkourSelectionUI;
import amata1219.parkour.ui.parkour.RankedParkourSelectionUI;

public class InventoryUIs {

	private static final ParkourCategory[] NORMAL_CATEGORIES = new ParkourCategory[]{ParkourCategory.NORMAL, ParkourCategory.SEGMENT, ParkourCategory.BIOME};

	public final InventoryUI myMenuUI;
	public final InventoryUI lastCheckpointSelectionUI;
	public final InventoryUI latestCheckpointSelectionUI;
	public final HashMap<ParkourCategory, InventoryUI> parkourSelectionUIs = new HashMap<>(5);

	public InventoryUIs(User user){
		myMenuUI = new MyMenuUI(user);
		lastCheckpointSelectionUI = new LastCheckpointSelectionUI(user);
		latestCheckpointSelectionUI = new LatestCheckpointSelectionUI(user);

		for(ParkourCategory category : NORMAL_CATEGORIES) parkourSelectionUIs.put(category, new ParkourSelectionUI(user, category));

		parkourSelectionUIs.put(ParkourCategory.UPDATE, new RankedParkourSelectionUI(user, ParkourCategory.UPDATE, () -> user.getUpdateRank()));
		parkourSelectionUIs.put(ParkourCategory.EXTEND, new RankedParkourSelectionUI(user, ParkourCategory.EXTEND, () -> user.getExtendRank()));
	}

}
