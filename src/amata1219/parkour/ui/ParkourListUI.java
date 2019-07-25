package amata1219.parkour.ui;

import java.util.function.Function;

import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;

public class ParkourListUI implements InventoryUI {

	@Override
	public Function<Player, InventoryLayout> layout() {
		return null;
	}

}
