package amata1219.parkour.ui;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.amalib.item.skull.SkullMaker;
import amata1219.amalib.message.MessageTemplate;
import amata1219.amalib.tuplet.Triple;
import amata1219.parkour.message.SoundPlayer;
import amata1219.parkour.user.User;

public class BuyAndPutOnSkullUI implements InventoryUI {

	private static final Map<UUID, Triple<ItemStack, String, Integer>> HEADS;

	static{
		Builder<UUID, Triple<ItemStack, String, Integer>> builder = ImmutableMap.builder();

		initializeWithPlayerHeads(builder,
			//ledlaggazi
			"58becc44-c5b7-420f-8800-15ba88820973,1000000",

			//YukiLeafX
			"82669f11-f1e5-402c-9642-75aff8a47613,500000",

			//siloneco
			"7daf21e7-b275-43dd-bc0d-4762c73d6199,500000"
		);

		initializeWithCustomHeads(builder
			//UUID(こちらで設定)headName,base64,価格
		);

		HEADS = builder.build();
	}

	private static void initializeWithPlayerHeads(Builder<UUID, Triple<ItemStack, String, Integer>> builder, String... texts){
		for(String text : texts){
			//各データに分割する
			String[] data = text.split(",");

			//第1引数をUUIDに変換する
			UUID uuid = UUID.fromString(data[0]);

			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			String playerName = player.getName();

			//プレイヤーに基づきスカルを作成する
			ItemStack skull = SkullMaker.fromOfflinePlayer(player);

			//第2引数を整数型に変換する
			int value = Integer.parseInt(data[1]);

			//表示例: ledlaggazi > Coins @ 10000
			//meta.setDisplayName(StringTemplate.format("$0 > Coins @ $1", playerName, coins));
			//meta.setLore(Arrays.asList(StringTemplate.format("$0: Click > Buy $1 Head!", ChatColor.GRAY, playerName)));

			builder.put(uuid, new Triple<>(skull, playerName, value));
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

	public BuyAndPutOnSkullUI(User user){
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
				//スカルデータを取得する
				Triple<ItemStack, String, Integer> triple = entry.getValue();

				//各値を取り出して宣言する
				ItemStack skull = triple.first;
				String skullName = triple.second;
				int value = triple.third;

				l.put((s) -> {
					//購入したスカルの場合
					if(user.purchasedHeads.contains(entry.getKey())){
						s.icon((i) -> {
							//基となるアイテムを設定する
							i.basedItemStack = skull;

							//表示例: amata1219 > Coins@500000
							i.displayName = MessageTemplate.apply("$0 > $1Coins@$2!", skullName, ChatColor.STRIKETHROUGH, value);

							//説明文を設定する
							i.lore(MessageTemplate.apply("$0: Click > Put on $1's Skull!", ChatColor.GRAY, skullName));
						});

						s.onClick((event) -> {
							putOnSkull(event.player, skull);
						});
					//未購入のスカルの場合
					}else{
						s.icon((i) -> {
							//基となるアイテムを設定する
							i.basedItemStack = skull;

							//表示例: amata1219 > Coins@500000
							i.displayName = MessageTemplate.apply("$0 > Coins@$1", skullName, value);

							//説明文を設定する
							i.lore(MessageTemplate.apply("$0: Click > Buy $1's Skull!", ChatColor.GRAY, skullName));
						});

						s.onClick((event) -> {
							if(user.getCoins() < value){
								//進捗ポップアップライブラリを作成したらメッセージも表示する

								//警告音を再生する
								SoundPlayer.play(event.player, Sound.BLOCK_ANVIL_PLACE, 1, 1);
								return;
							}

							//スカルの価格分のコインを減らす
							user.withdrawCoins(value);

							//クリックしたスカルを取得する
							Icon clickedIcon = event.currentIcon;

							//表示例: amata1219 > Coins@500000
							clickedIcon.displayName = MessageTemplate.apply("$0 > $1Coins@$2!", skullName, ChatColor.STRIKETHROUGH, value);

							//説明文を変更する
							clickedIcon.lore(MessageTemplate.apply("$0: Click > Put on $1's Skull!", ChatColor.GRAY, skullName));

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
					i.displayName = ChatColor.RED + "Click > Take Off Skull!";
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

		//装備音を設定する
		SoundPlayer.play(player, Sound.ITEM_ARMOR_EQUIP_CHAIN, 1, 1);
	}

}
