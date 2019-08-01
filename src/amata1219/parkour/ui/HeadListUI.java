package amata1219.parkour.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.inventory.ui.dsl.component.InventoryLayout;
import amata1219.amalib.inventory.ui.option.InventoryLine;
import amata1219.amalib.item.skull.SkullMaker;
import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.tuple.Tuple;
import amata1219.parkour.user.User;

public class HeadListUI implements InventoryUI {

	private static final Map<UUID, Tuple<ItemStack, Integer>> HEADS;

	static{
		Builder<UUID, Tuple<ItemStack, Integer>> mapBuilder = ImmutableMap.builder();

		initializeWithPlayerHeads(mapBuilder,
			//ledlaggazi
			"58becc44-c5b7-420f-8800-15ba88820973,1000000",

			//YukiLeafX
			"82669f11-f1e5-402c-9642-75aff8a47613,500000",

			//siloneco
			"7daf21e7-b275-43dd-bc0d-4762c73d6199,500000"
		);

		initializeWithCustomHeads(mapBuilder
			//UUID(こちらで設定),base64,価格
		);

		HEADS = mapBuilder.build();
	}

	private static void initializeWithPlayerHeads(Builder<UUID, Tuple<ItemStack, Integer>> mapBuilder, String... texts){
		for(String text : texts){
			//各データに分割する
			String[] data = text.split(",");

			UUID uuid = UUID.fromString(data[0]);
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			int coins = Integer.parseInt(data[1]);

			//プレイヤーに基づきヘッドを作成する
			ItemStack skull = SkullMaker.fromOfflinePlayer(player);
			ItemMeta meta = skull.getItemMeta();

			//表示例: ledlaggazi > Coins @ 10000
			meta.setDisplayName(StringTemplate.format("$0 > Coins @ $1", player.getName(), coins));
			skull.setItemMeta(meta);

			mapBuilder.put(uuid, new Tuple<>(skull, coins));
		}
	}

	private static void initializeWithCustomHeads(Builder<UUID, Tuple<ItemStack, Integer>> mapBuilder, String... texts){

	}

	private User user;

	public HeadListUI(User user){
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

			for(Entry<UUID, Tuple<ItemStack, Integer>> entry : HEADS.entrySet()){
				//ヘッドデータを取得する
				Tuple<ItemStack, Integer> head = entry.getValue();
				ItemStack skullItem = head.first;

				l.put((s) -> {
					s.icon((i) -> {
						i.basedItemStack = skullItem;

						//購入したヘッドの場合
						if(user.purchasedHeads.contains(entry.getKey())){
							ItemMeta meta = skullItem.getItemMeta();

							//表示名を名前と価格に分割する
							String[] parts = meta.getDisplayName().split(" > ");

							String playerName = parts[0];

							//価格を消して購入済みと表示する
							i.displayName = StringTemplate.format("$0 > 購入済み", playerName);

							i.lore(StringTemplate.format("$0: Click > Wear $1 Head!", ChatColor.GRAY, playerName));
						}
					});
				}, slotIndex.getAndIncrement());
			}

		});
	}

}
