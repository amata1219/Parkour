package amata1219.parkour.user;

import amata1219.parkour.ui.InformationBoardOptionsUI;
import amata1219.parkour.ui.LastCheckpointsUI;
import amata1219.parkour.ui.MenuUI;
import amata1219.parkour.ui.PersonalSettingsUI;
import amata1219.parkour.ui.SkullsUI;

public class InventoryUISet {

	public final LastCheckpointsUI lastCheckpointsUI;
	public final MenuUI menuUI;
	public final PersonalSettingsUI personalSettingsUI;
	public final InformationBoardOptionsUI informationBoardOptionsUI;
	public final SkullsUI skullsUI;

	public InventoryUISet(User user){
		lastCheckpointsUI = new LastCheckpointsUI(user);
		skullsUI = new SkullsUI(user);
	}

}
