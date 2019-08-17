package amata1219.parkour.ui;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.string.StringColor;
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
			Player player = l.player;

			l.title = StringColor.lcolor("ハットの購入 | Buy hats", player);

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			for(int slotIndex = 0; slotIndex < hats.size(); slotIndex++){
				l.put(s -> {

				}, slotIndex);
			}
		});
	}

	/*
	 * amata1219
	 */

}
