package amata1219.parkour.user;

import amata1219.parkour.menu.LastCheckpointsMenu;
import amata1219.parkour.menu.LatestCheckpointsMenu;
import amata1219.parkour.menu.MyMenu;
import amata1219.parkour.menu.parkour.ExtendParkoursMenu;
import amata1219.parkour.menu.parkour.UpdateParkoursMenu;

public class InventoryUserInterfaces {

	public final MyMenu menuUI;
	public final UpdateParkoursMenu updateParkourSelectionUI;
	public final ExtendParkoursMenu extendParkourSelectionUI;
	public final LastCheckpointsMenu lastCheckpointSelectionUI;
	public final LatestCheckpointsMenu latestCheckpointSelectionUI;

	public InventoryUserInterfaces(User user){
		menuUI = new MyMenu(user);
		updateParkourSelectionUI = new UpdateParkoursMenu(user);
		extendParkourSelectionUI = new ExtendParkoursMenu(user);
		lastCheckpointSelectionUI = new LastCheckpointsMenu(user);
		latestCheckpointSelectionUI = new LatestCheckpointsMenu(user);
	}

}
