package amata1219.parkour.user;

import amata1219.parkour.ui.LastCheckpointSelectionUI;
import amata1219.parkour.ui.MenuUI;
import amata1219.parkour.ui.parkour.ExtendParkourSelectionUI;
import amata1219.parkour.ui.parkour.UpdateParkourSelectionUI;

public class InventoryUserInterfaces {

	public final LastCheckpointSelectionUI lastCheckpointUI;
	public final MenuUI menuUI;
	public final UpdateParkourSelectionUI updateParkourSelectionUI;
	public final ExtendParkourSelectionUI extendParkourSelectionUI;

	public InventoryUserInterfaces(User user){
		lastCheckpointUI = new LastCheckpointSelectionUI(user);
		menuUI = new MenuUI(user);
		updateParkourSelectionUI = new UpdateParkourSelectionUI(user);
		extendParkourSelectionUI = new ExtendParkourSelectionUI(user);
	}

}