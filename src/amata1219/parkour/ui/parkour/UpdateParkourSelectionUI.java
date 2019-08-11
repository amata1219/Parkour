package amata1219.parkour.ui.parkour;

import java.util.function.Function;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.user.User;

public class UpdateParkourSelectionUI implements InventoryUI {

	private final User user;

	public UpdateParkourSelectionUI(User user){
		this.user = user;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		return null;
	}

}
