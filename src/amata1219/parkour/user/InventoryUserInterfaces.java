package amata1219.parkour.user;

import amata1219.parkour.ui.LastCheckpointSelectionUI;
import amata1219.parkour.ui.MenuUI;

public class InventoryUserInterfaces {

	public final LastCheckpointSelectionUI lastCheckpointUI;
	public final MenuUI menuUI;

	public InventoryUserInterfaces(User user){
		lastCheckpointUI = new LastCheckpointSelectionUI(user);
		menuUI = new MenuUI(user);
	}

}
