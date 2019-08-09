package amata1219.parkour.ui;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.amalib.message.MessageColor;
import amata1219.amalib.message.MessageTemplate;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.tuplet.Triple;
import amata1219.amalib.util.SkullMaker;
import amata1219.parkour.user.User;
import amata1219.parkour.util.SoundPlayer;

public class SkullUI implements InventoryUI {

	private static final Map<UUID, Triple<ItemStack, String, Integer>> HEADS;

	static{
		Builder<UUID, Triple<ItemStack, String, Integer>> builder = ImmutableMap.builder();

		initializeWithPlayerHeads(builder,
			"ledlaggazi,58becc44-c5b7-420f-8800-15ba88820973,1000000",
			"YukiLeafX,82669f11-f1e5-402c-9642-75aff8a47613,500000",
			"siloneco,7daf21e7-b275-43dd-bc0d-4762c73d6199,500000"
		);

		initializeWithCustomHeads(builder
			//headName,UUID(こちらで設定),base64,価格
		);

		HEADS = builder.build();
	}

	private static void initializeWithPlayerHeads(Builder<UUID, Triple<ItemStack, String, Integer>> builder, String... texts){
		for(String text : texts){
			//各データに分割する
			String[] data = text.split(",");

			//第1引数をUUIDに変換する
			UUID uuid = UUID.fromString(data[1]);

			builder.put(uuid, new Triple<>(SkullMaker.fromPlayerUniqueId(uuid), data[0], Integer.parseInt(data[2])));
		}
	}

	private static void initializeWithCustomHeads(Builder<UUID, Triple<ItemStack, String, Integer>> builder, String... texts){
		for(String text : texts){
			//各データに分割する
			String[] data = text.split(",");

			//第1引数をUUIDに変換する
			UUID uuid = UUID.fromString(data[0]);

			//第2引数をスカル名とする
			String skullName = data[1];

			//第3引数をbase64としてスカルを作成する
			ItemStack skull = SkullMaker.fromBase64(data[2]);

			//第4引数を整数型に変換する
			int value = Integer.parseInt(data[3]);

			builder.put(uuid, new Triple<>(skull, skullName, value));
		}
	}

	private User user;

	public SkullUI(User user){
		this.user = user;
	}

	@Override
	public Function<Player, InventoryLayout> layout() {
		return build(InventoryLine.x1, (l) -> {

			//デフォルトスロットは薄灰色のガラス板
			l.defaultSlot((s) -> {
				s.icon((i) -> {
					i.material = Material.LIGHT_GRAY_STAINED_GLASS_PANE;
					i.displayName = " ";
				});
			});

			//スロット番号用のカウンター
			AtomicInteger slotIndex = new AtomicInteger();

			for(Entry<UUID, Triple<ItemStack, String, Integer>> entry : HEADS.entrySet()){
				UUID uuid = entry.getKey();

				//スカルデータを取得する
				Triple<ItemStack, String, Integer> triple = entry.getValue();

				//各値を取り出して宣言する
				ItemStack skull = triple.first;
				String skullName = triple.second;
				int value = triple.third;

				l.put((s) -> {
					//購入したスカルの場合
					if(user.purchasedHeads.contains(uuid)){
						s.icon((i) -> {
							//基となるアイテムを設定する
							i.basedItemStack = skull;

							//表示例: amata1219 @ 500000 coins!
							i.displayName = StringTemplate.capply("&b-$0 &7-@ &c-&m-$1 coins", skullName, value);

							//表示例: Click to put on amata1219's skull!
							i.lore(StringTemplate.capply("&7-Click to put on $0's skull!", skullName));
						});

						s.onClick((event) -> {
							putOnSkull(event.player, skull);
						});
					//未購入のスカルの場合
					}else{
						s.icon((i) -> {
							//基となるアイテムを設定する
							i.basedItemStack = skull;

							//表示例: amata1219 @ 500000 coins!
							i.displayName = StringTemplate.capply("&b-$0 &7-@ &b-$1 coins", skullName, value);

							//表示例: Click to buy amata1219's skull!
							i.lore(StringTemplate.capply("&7-Click to buy $0's skull!", skullName));
						});

						s.onClick((event) -> {
							if(user.getCoins() < value){
								MessageColor.color("&c-Operation blocked &7-@ &c-Not enough money").displayOnActionBar(event.player);
								return;
							}

							//スカルの価格分のコインを減らす
							user.withdrawCoins(value);

							user.purchasedHeads.add(uuid);

							//クリックしたスカルを取得する
							Icon clickedIcon = event.currentIcon;

							MessageTemplate.capply("&b-Bought $0's skull", skullName).displayOnActionBar(event.player);

							SoundPlayer.play(event.player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

							//表示例: amata1219 @ 500000 coins!
							clickedIcon.displayName = StringTemplate.capply("&b-$0 &7-@ &c-&m-$1 coins", skullName, value);

							//表示例: Click to put on amata1219's skull!
							clickedIcon.lore(StringTemplate.capply("&7-Click to put on $0's skull!", skullName));

							//クリック時の処理を変更する
							s.onClick((ev) -> {
								putOnSkull(ev.player, skull);
							});
						});
					}
				}, slotIndex.getAndIncrement());
			}

			l.put((s) -> {

				s.icon((i) -> {
					i.material = Material.BARRIER;

					//表示名を設定する
					i.displayName = StringColor.color("&7-: &c-Click &7-@ &c-Take off the skull");
				});

				s.onClick((event) -> {
					event.player.getInventory().setHelmet(new ItemStack(Material.AIR));
				});

			}, 53);
		});
	}

	private void putOnSkull(Player player, ItemStack skull){
		//スカルを被らせる
		player.getInventory().setHelmet(skull);

		player.updateInventory();

		//装備音を設定する
		SoundPlayer.play(player, Sound.ITEM_ARMOR_EQUIP_CHAIN, 1, 1);
	}

}