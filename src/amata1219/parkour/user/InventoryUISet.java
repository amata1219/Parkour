package amata1219.parkour.user;

import amata1219.parkour.ui.LastCheckpointSelectionUI;
import amata1219.parkour.ui.Menu;

public class InventoryUISet {

	public final LastCheckpointSelectionUI lastCheckpointUI;
	public final Menu menu;

	public InventoryUISet(User user){
		lastCheckpointUI = new LastCheckpointSelectionUI(user);
		menu = new Menu(user);
	}

}
