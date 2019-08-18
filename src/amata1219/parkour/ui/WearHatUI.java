package amata1219.parkour.ui;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.sound.SoundMetadata;
import amata1219.amalib.string.StringLocalize;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.string.message.Localizer;
import amata1219.parkour.hat.Hat;
import amata1219.parkour.hat.Hats;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserHats;

public class WearHatUI implements InventoryUI {

	private static final SoundMetadata WEAR_SE = new SoundMetadata(Sound.ITEM_ARMOR_EQUIP_CHAIN, 2f, 0.25f);
	private static final SoundMetadata ERROR_SE = new SoundMetadata(Sound.BLOCK_ANVIL_PLACE, 0.75f, 0.5f);
	private static final SoundMetadata PUT_ON_SE = new SoundMetadata(Sound.ENTITY_CHICKEN_EGG, 1.5f, 1f);
	private static final ItemStack AIR = new ItemStack(Material.AIR);

	private final UserHats hats;
	private final Localizer localizer;

	public WearHatUI(User user){
		this.hats = user.hats;
		this.localizer = user.localizer;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//購入済みハットのリスト
		List<Hat> hats = Hats.HATS.stream()
		.filter(this.hats::has)
		.collect(Collectors.toList());

		return build(hats.size() + 1, l -> {
			Player player = l.player;

			l.title = StringLocalize.color("ハットの購入 | Buy Hats", player);

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			for(int index = 0; index < hats.size(); index++){
				Hat hat = hats.get(index);
				String hatName = hat.name;
				ItemStack itemName = hat.item;

				l.put(s -> {

					s.onClick(e -> {
						PlayerInventory inventory = player.getInventory();
						ItemStack helmet = inventory.getHelmet();

						//既に同じ帽子を被っている場合
						if(itemName.isSimilar(helmet)){
							localizer.mcolor("&c-既に同じ帽子を被っています | &c-?").displayOnActionBar(player);
							ERROR_SE.play(player);
							return;
						}

						inventory.setHelmet(itemName);
						localizer.mapplyAll("&b-$0の帽子を被りました | &b-?", hatName).displayOnActionBar(player);
						WEAR_SE.play(player);
					});

					s.icon(i -> {
						i.basedItemStack = itemName.clone();
						i.displayName = StringTemplate.capply("&b-$0", hatName);
						i.lore(localizer.color("&7-クリックすると被ります。 | &7-?"));
					});

				}, index);
			}

			l.put(s -> {

				s.onClick(e -> {
					PlayerInventory inventory = player.getInventory();
					ItemStack helmet = inventory.getHelmet();

					//帽子を被っていない場合
					if(helmet == null){
						localizer.mcolor("&c-あなたは帽子を被っていません | &c-?").displayOnActionBar(player);
						ERROR_SE.play(player);
						return;
					}

					inventory.setHelmet(AIR);
					e.currentIcon.tarnish();
					localizer.mcolor("&b-帽子を仕舞いました | &b-?").displayOnActionBar(player);
					PUT_ON_SE.play(player);
				});

				s.icon(Material.CHEST, i -> {
					i.displayName = localizer.color("&b-帽子を仕舞う | &b-?");

					//帽子を被っていたら輝かせる
					if(player.getInventory().getHelmet() != null) i.gleam();
				});

			},l.option.size - 1);
		});
	}

}
