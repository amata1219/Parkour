package amata1219.parkour.user;

import amata1219.parkour.ui.OldLastCheckpointSelectionUI;
import amata1219.parkour.ui.MenuUI;
import amata1219.parkour.ui.parkour.ExtendParkourSelectionUI;
import amata1219.parkour.ui.parkour.UpdateParkourSelectionUI;

public class InventoryUserInterfaces {

	public final OldLastCheckpointSelectionUI lastCheckpointUI;
	public final MenuUI menuUI;
	public final UpdateParkourSelectionUI updateParkourSelectionUI;
	public final ExtendParkourSelectionUI extendParkourSelectionUI;

	public InventoryUserInterfaces(User user){
		lastCheckpointUI = new OldLastCheckpointSelectionUI(user);
		menuUI = new MenuUI(user);
		updateParkourSelectionUI = new UpdateParkourSelectionUI(user);
		extendParkourSelectionUI = new ExtendParkourSelectionUI(user);
	}

}
