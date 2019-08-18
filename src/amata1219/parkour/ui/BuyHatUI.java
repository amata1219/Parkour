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

public class BuyHatUI implements InventoryUI {

	private static final SoundMetadata BUY_SE = new SoundMetadata(Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.75f);
	private static final SoundMetadata ERROR_SE = new SoundMetadata(Sound.BLOCK_ANVIL_PLACE, 1f, 1.75f);

	private final User user;
	private final UserHats hats;

	public BuyHatUI(User user){
		this.user = user;
		this.hats = user.hats;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//未購入ハットのリスト
		List<Hat> hats = Hats.HATS.stream()
		.filter(hat -> !this.hats.has(hat))
		.collect(Collectors.toList());

		return build(hats.size(), l -> {
			Player player = l.player;

			l.title = StringLocalize.color("ハットの購入 | Buy Hats", player);

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			for(int index = 0; index < hats.size(); index++){
				Hat hat = hats.get(index);
				int value = hat.value;
				String hatName = hat.name;
				ItemStack clonedItem = hat.item.clone();

				l.put(s -> {
					//ハットを購入出来るだけのコインを持っている場合
					if(value <= user.getCoins()){
						s.onClick(e -> {
							BUY_SE.play(player);

							//表示を更新する
							user.inventoryUIs.openBuyHatUI();
						});

						s.icon(i -> {
							i.basedItemStack = clonedItem;
							i.displayName = StringLocalize.ctemplate("&b-$0 &7-@ &6-$1-&7-コイン | ?", player, hatName, value);
							i.lore(StringLocalize.color("&7-クリックすると購入出来ます。 | ?", player));
						});
					}else{
						s.onClick(e -> ERROR_SE.play(player));

						s.icon(i -> {
							i.basedItemStack = clonedItem;
							i.displayName = StringLocalize.ctemplate("&c-$0 &7-@ &6-$1-&7-コイン | ?", player, hatName, value);
							i.lore(StringLocalize.color("&c-所持コイン数が足りないため購入出来ません。 | ?", player));
						});
					}

				}, index);
			}
		});
	}

}
