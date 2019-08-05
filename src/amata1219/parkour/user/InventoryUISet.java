package amata1219.parkour.user;

import amata1219.parkour.ui.LastCheckpointsUI;
import amata1219.parkour.ui.MenuUI;

public class InventoryUISet {

	public final LastCheckpointsUI lastCheckpointsUI;
	public final MenuUI menuUI;

	public InventoryUISet(User user){
		lastCheckpointsUI = new LastCheckpointsUI(user);
		menuUI = new MenuUI(user);
	}

}
