package amata1219.parkour.ui;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.sound.SoundMetadata;
import amata1219.amalib.string.StringLocalize;
import amata1219.parkour.hat.Hat;
import amata1219.parkour.hat.Hats;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserHats;

public class WearHatUI implements InventoryUI {

	private static final SoundMetadata WEAR_SE = new SoundMetadata(Sound.ITEM_ARMOR_EQUIP_CHAIN, 2f, 0.25f);

	private final User user;
	private final UserHats hats;

	public WearHatUI(User user){
		this.user = user;
		this.hats = user.hats;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//購入済みハットのリスト
		List<Hat> hats = Hats.HATS.stream()
		.filter(this.hats::has)
		.collect(Collectors.toList());

		return build(hats.size(), l -> {
			Player player = l.player;

			l.title = StringLocalize.color("ハットの購入 | Buy Hats", player);

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			for(int index = 0; index < hats.size(); index++){
				Hat hat = hats.get(index);

				l.put(s -> {

					s.onClick(e -> {
						WEAR_SE.play(player);
						player.getInventory().setHelmet(hat.item);
					});

					s.icon(i -> {

					});

				}, index);
			}
		});
	}

}
