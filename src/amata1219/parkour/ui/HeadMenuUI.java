package amata1219.parkour.ui;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.message.MessageColor;
import amata1219.amalib.message.MessageTemplate;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.head.Head;
import amata1219.parkour.head.Heads;
import amata1219.parkour.user.PurchasedHeads;
import amata1219.parkour.util.SoundPlayer;

public class HeadMenuUI implements InventoryUI {

	private final PurchasedHeads heads;

	public HeadMenuUI(PurchasedHeads heads){
		this.heads = heads;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		return build(Heads.HEADS.size(), (l) -> {
			//タイトルを設定する
			l.title = StringColor.color("&b-Head Menu");

			AtomicInteger slotIndex = new AtomicInteger();

			Heads.HEADS.values().forEach(head -> {

				String headName = head.name;
				int headValue = head.value;
				ItemStack headItem = head.item;

				l.put((s) -> {
					//購入済みのヘッドの場合
					if(heads.has(head)){

						s.icon((i) -> {
							//基となるアイテムを設定する
							i.basedItemStack = headItem.clone();

							//表示名と説明文を設定する
							setPurchasedHeadText(i, head);
						});

						s.onClick((event) -> setHead(event.player, headItem));

					//未購入のヘッドの場合
					}else{
						s.icon((i) -> {
							//基となるアイテムを設定する
							i.basedItemStack = headItem;

							//表示例: amata1219 @ 500000 coins!
							i.displayName = StringTemplate.capply("&b-$0 &7-@ &f-$1 coins", headName, headValue);

							//表示例: Click to buy amata1219's skull!
							i.lore(StringTemplate.capply("&7-Click to buy $0's skull!", headName));
						});

						s.onClick((event) -> {
							//コインが足りなければ戻る
							if(!heads.canBuy(head)){
								MessageColor.color("&c-Operation blocked &7-@ &c-Not enough money").displayOnActionBar(event.player);
								return;
							}

							//表示名と説明文を変更する
							setPurchasedHeadText(event.currentIcon, head);

							//クリック時の処理を変更する
							s.onClick((e) -> setHead(e.player, headItem));

							MessageTemplate.capply("&b-Bought $0's skull", headName).displayOnActionBar(event.player);
							SoundPlayer.play(event.player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
						});

					}

				}, slotIndex.getAndIncrement());

			});

		});
	}

	private void setPurchasedHeadText(Icon icon, Head head){
		String headName = head.name;

		//表示例: amata1219 @ 500000 coins!
		icon.displayName = StringTemplate.capply("&b-$0 &7-@ &c-&m-$1 coins", headName, head.value);

		//表示例: Click to put on amata1219's skull!
		icon.lore(StringTemplate.capply("&7-Click to put on $0's skull!", headName));
	}

	private void setHead(Player player, ItemStack item){
		//装備させる
		player.getInventory().setHelmet(item);
		player.updateInventory();

		//装備音を設定する
		SoundPlayer.play(player, Sound.ITEM_ARMOR_EQUIP_CHAIN, 1, 1);
	}

}
