package amata1219.parkour.user;

import amata1219.parkour.ui.LastCheckpointSelectionUI;
import amata1219.parkour.ui.LatestCheckpointSelectionUI;
import amata1219.parkour.ui.MyMenuUI;
import amata1219.parkour.ui.parkour.ExtendParkourSelectionUI;
import amata1219.parkour.ui.parkour.UpdateParkoursMenu;

public class InventoryUserInterfaces {

	public final MyMenuUI menuUI;
	public final UpdateParkoursMenu updateParkourSelectionUI;
	public final ExtendParkourSelectionUI extendParkourSelectionUI;
	public final LastCheckpointSelectionUI lastCheckpointSelectionUI;
	public final LatestCheckpointSelectionUI latestCheckpointSelectionUI;

	public InventoryUserInterfaces(User user){
		menuUI = new MyMenuUI(user);
		updateParkourSelectionUI = new UpdateParkoursMenu(user);
		extendParkourSelectionUI = new ExtendParkourSelectionUI(user);
		lastCheckpointSelectionUI = new LastCheckpointSelectionUI(user);
		latestCheckpointSelectionUI = new LatestCheckpointSelectionUI(user);
	}

}
