package amata1219.parkour.ui;

import java.util.function.Function;

import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.User;

public class AllCheckpointsUI implements InventoryUI {

	private final User user;
	private final Parkour parkour;

	public AllCheckpointsUI(User user, Parkour parkour){
		this.user = user;
		this.parkour = parkour;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {


		return build()
	}

}
