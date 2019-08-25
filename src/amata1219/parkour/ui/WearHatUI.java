package amata1219.parkour.ui;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.parkour.hat.Hat;
import amata1219.parkour.hat.Hats;
import amata1219.parkour.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.sound.SoundMetadata;
import amata1219.parkour.string.StringTemplate;
import amata1219.parkour.string.message.Localizer;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserHats;

public class WearHatUI implements InventoryUI {

	private static final SoundMetadata WEAR_SE = new SoundMetadata(Sound.ITEM_ARMOR_EQUIP_CHAIN, 5f, 0.25f);
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

			l.title = localizer.color("帽子を被る | ?");

			l.defaultSlot(s -> s.icon(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i -> i.displayName = " "));

			int slotIndex = 0;
			int lastSlotIndex = l.option.size - 1;

			for(int index = 0; index < hats.size(); index++){
				Hat hat = hats.get(index);
				String hatName = hat.name;
				ItemStack hatItem = hat.item;

				//同じ帽子であれば処理しない
				if(isSameHat(hatName, player.getInventory().getHelmet())) continue;

				l.put(s -> {

					s.onClick(e -> {
						PlayerInventory inventory = player.getInventory();

						//帽子を被らせる
						inventory.setHelmet(hatItem);

						//被った帽子はNMS側でclone()されているので取得した物を書き換える
						ItemStack equippedHelmet = inventory.getHelmet();

						//表示名を帽子のプレイヤー名にする
						ItemMeta meta = equippedHelmet.getItemMeta();
						meta.setDisplayName(StringTemplate.capply("&f-$0", hatName));
						equippedHelmet.setItemMeta(meta);

						ItemStack putOnButton = new ItemStack(Material.CHEST);
						ItemMeta putOnMeta = putOnButton.getItemMeta();
						putOnMeta.setDisplayName(localizer.color("&b-帽子を仕舞う | &b-?"));
						putOnButton.setItemMeta(putOnMeta);

						e.clickedInventory.setItem(lastSlotIndex, putOnButton);

						localizer.mapplyAll("&b-$0の帽子を被りました | &b-?", hatName).displayOnActionBar(player);

						WEAR_SE.play(player);

						openInventory(player);
					});

					s.icon(i -> {
						i.basedItemStack = hatItem.clone();
						i.displayName = StringTemplate.capply("&b-$0", hatName);
						i.lore(localizer.color("&7-クリックすると被ります。 | &7-?"));
					});

				}, slotIndex++);
			}

			l.put(s -> {

				s.onClick(e -> {
					PlayerInventory inventory = player.getInventory();
					ItemStack helmet = inventory.getHelmet();

					//帽子を被っていなければ戻る
					if(helmet == null) return;

					inventory.setHelmet(AIR);
					e.clickedInventory.setItem(lastSlotIndex, AIR);
					localizer.mcolor("&b-帽子を仕舞いました | &b-?").displayOnActionBar(player);

					PUT_ON_SE.play(player);

					openInventory(player);
				});

				//帽子を被っていればそれを脱ぐ為のボタンをセットする
				if(player.getInventory().getHelmet() != null) s.icon(Material.CHEST, i -> i.displayName = localizer.color("&b-帽子を仕舞う | &b-?"));

			}, lastSlotIndex);
		});
	}

	private boolean isSameHat(String hatName, ItemStack helmet){
		return helmet != null && helmet.getType() == Material.PLAYER_HEAD && helmet.hasItemMeta() && hatName.endsWith(helmet.getItemMeta().getDisplayName());
	}

}
