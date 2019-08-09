package amata1219.parkour.user;

import amata1219.parkour.ui.LastCheckpointSelectionUI;
import amata1219.parkour.ui.MenuUI;

public class InventoryUIs {

	public final LastCheckpointSelectionUI lastCheckpointUI;
	public final MenuUI menu;

	public InventoryUIs(User user){
		lastCheckpointUI = new LastCheckpointSelectionUI(user);
		menu = new MenuUI(user);
	}

}
