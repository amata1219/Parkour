package amata1219.parkour.ui;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.hat.Hat;
import amata1219.parkour.hat.Hats;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserHats;

public class HatPurchaseUI implements InventoryUI {

	private final User user;
	private final UserHats hats;

	public HatPurchaseUI(User user){
		this.user = user;
		this.hats = user.hats;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//未購入ハットのリスト
		List<Hat> hats = Hats.HATS.stream()
		.filter(this.hats::has)
		.collect(Collectors.toList());

		return build(hats.size(), l -> {

		});
	}

}
