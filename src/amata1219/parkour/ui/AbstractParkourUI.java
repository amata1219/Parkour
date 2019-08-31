package amata1219.parkour.ui;

import amata1219.parkour.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.user.User;

public abstract class AbstractParkourUI implements InventoryUI {

	protected final User user;

	public AbstractParkourUI(User user){
		this.user = user;
	}

}
