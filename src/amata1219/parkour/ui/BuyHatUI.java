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
import amata1219.amalib.string.message.Localizer;
import amata1219.parkour.hat.Hat;
import amata1219.parkour.hat.Hats;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserHats;

public class BuyHatUI implements InventoryUI {

	private static final SoundMetadata BUY_SE = new SoundMetadata(Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.75f);
	private static final SoundMetadata ERROR_SE = new SoundMetadata(Sound.BLOCK_ANVIL_PLACE, 1f, 1.75f);

	private final User user;
	private final UserHats hats;
	private final Localizer localizer;

	public BuyHatUI(User user){
		this.user = user;
		this.hats = user.hats;
		this.localizer = user.localizer;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//未購入ハットのリスト
		List<Hat> hats = Hats.HATS.stream()
		.filter(hat -> !this.hats.has(hat))
		.collect(Collectors.toList());

		return build(hats.size(), l -> {
			Player player = l.player;

			l.title = localizer.color("ハットの購入 | Buy Hats");

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			for(int index = 0; index < hats.size(); index++){
				Hat hat = hats.get(index);
				int value = hat.value;
				String hatName = hat.name;
				ItemStack clonedHatItem = hat.item.clone();

				l.put(s -> {
					if(this.hats.canBuy(hat)){
						s.onClick(e -> {
							this.hats.buy(hat);
							localizer.mapplyAll("&b-$0の帽子を購入しました。 | &b-?", hatName).displayOnActionBar(player);
							BUY_SE.play(player);

							//表示を更新する
							user.inventoryUserInterfaces.openBuyHatUI();
						});

						s.icon(i -> {
							i.basedItemStack = clonedHatItem;
							i.displayName = localizer.applyAll("&b-$0 &7-@ &6-$1-&7-コイン | &b-?", hatName, value);
							i.lore(localizer.color("&7-クリックすると購入出来ます。 | &7-?"));
						});
					}else{
						s.onClick(e -> {
							localizer.mcolor("&c-所持コイン数が足りないため購入出来ません。 | &c-?");
							ERROR_SE.play(player);
						});

						s.icon(i -> {
							i.basedItemStack = clonedHatItem;
							i.displayName = localizer.applyAll("&c-$0 &7-@ &6-$1-&7-コイン | &c-?", hatName, value);
							i.lore(localizer.color("&7-所持コイン数が足りないため購入出来ません。 | &f-?"));
						});
					}

				}, index);
			}
		});
	}

}
