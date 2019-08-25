package amata1219.parkour.ui;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import amata1219.parkour.hat.Hats;
import amata1219.parkour.inventory.ui.InventoryLine;
import amata1219.parkour.inventory.ui.dsl.InventoryUI;
import amata1219.parkour.inventory.ui.dsl.component.Icon;
import amata1219.parkour.inventory.ui.dsl.component.InventoryLayout;
import amata1219.parkour.sound.SoundMetadata;
import amata1219.parkour.string.StringColor;
import amata1219.parkour.string.StringTemplate;
import amata1219.parkour.string.message.MessageColor;
import amata1219.parkour.string.message.MessageTemplate;
import amata1219.parkour.enchantment.GleamEnchantment;
import amata1219.parkour.hat.Hat;
import amata1219.parkour.user.UserHats;

public class PurchaseAndWearHatUI implements InventoryUI {

	private static final SoundMetadata BUY_SE = new SoundMetadata(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.5f, 1);
	private static final SoundMetadata PUT_ON_SE = new SoundMetadata(Sound.ITEM_ARMOR_EQUIP_CHAIN, 2f, 1f);

	private final UserHats heads;

	public PurchaseAndWearHatUI(UserHats heads){
		this.heads = heads;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		//ヘッドの数から必要な行数を計算する
		InventoryLine line = InventoryLine.necessaryInventoryLine(Hats.HATS.size());

		return build(line, (l) -> {
			//タイトルを設定する
			l.title = StringColor.color("&b-Head Menu");

			AtomicInteger slotIndex = new AtomicInteger();

			Hats.HATS.forEach(head -> {

				String headName = head.name;
				int headValue = head.value;
				ItemStack headItem = head.item;

				l.put(s -> {
					//購入済みのヘッドの場合
					if(heads.has(head)){

						s.icon(i -> {
							//基となるアイテムを設定する
							i.basedItemStack = headItem.clone();

							//表示名と説明文を設定する
							setPurchasedHeadText(i, head);
						});

						s.onClick(e -> setHead(e.clickedInventory, e.player, head));

					//未購入のヘッドの場合
					}else{
						s.icon(i -> {
							//基となるアイテムを設定する
							i.basedItemStack = headItem;

							//表示例: amata1219 @ 500000 coins!
							i.displayName = StringTemplate.capply("&b-$0 &7-@ &f-$1 coins", headName, headValue);

							//表示例: Click to buy amata1219's skull!
							i.lore(StringTemplate.capply("&7-Click to buy $0's skull!", headName));
						});

						s.onClick(e -> {
							//コインが足りなければ戻る
							if(!heads.canBuy(head)){
								MessageColor.color("&c-Operation blocked &7-@ &c-Not enough money").displayOnActionBar(e.player);
								return;
							}

							heads.buy(head);

							//表示名と説明文を変更する
							setPurchasedHeadText(e.currentIcon, head);

							//クリック時の処理を変更する
							s.onClick((ev) -> setHead(ev.clickedInventory, ev.player, head));

							BUY_SE.play(e.player);

							MessageTemplate.capply("&b-Bought $0's skull", headName).displayOnActionBar(e.player);
						});

					}

				}, slotIndex.getAndIncrement());

			});

			//ヘッドを外すボタンを設ける
			l.put((s) -> {

				s.icon(Material.BARRIER, i -> {
					i.displayName = StringColor.color("&b-Take the head off");

					ItemStack head = l.player.getInventory().getHelmet();

					//ヘッドを被っていれば輝かせる
					if(head != null && head.getType() != Material.AIR) i.gleam();
				});

				s.onClick(e -> {
					Player player = e.player;
					PlayerInventory inventory = player.getInventory();
					ItemStack head = inventory.getHelmet();

					//何も被っていなければ戻る
					if(head == null || head.getType() == Material.AIR) return;

					//ヘッドを外す
					inventory.setHelmet(new ItemStack(Material.AIR));

					//何も被っていない状態になったので発光を削除する
					e.currentIcon.tarnish();

					MessageColor.color("&b-Take the head off").displayOnActionBar(player);
				});

			}, line.inventorySize() - 1);

		});
	}

	private void setPurchasedHeadText(Icon icon, Hat head){
		String headName = head.name;

		//表示例: amata1219 @ 500000 coins!
		icon.displayName = StringTemplate.capply("&b-$0 &7-@ &c-&m-$1 coins", headName, head.value);

		//表示例: Click to put on amata1219's skull!
		icon.lore(StringTemplate.capply("&7-Click to put on $0's skull!", headName));
	}

	private void setHead(Inventory clickedInventory, Player player, Hat head){
		//被らせる
		player.getInventory().setHelmet(head.item);
		player.updateInventory();

		PUT_ON_SE.play(player);

		//ヘッドを被った状態になったので発光させる
		ItemStack taker = clickedInventory.getItem(clickedInventory.getSize() - 1);
		GleamEnchantment.gleam(taker);

		MessageTemplate.capply("&b-Put on $0's skull", head.name).displayOnActionBar(player);
	}

}
