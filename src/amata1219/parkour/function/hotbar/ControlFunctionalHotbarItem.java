package amata1219.parkour.function.hotbar;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import amata1219.parkour.function.PlayerLocaleChange;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.listener.PlayerQuitListener;

public class ControlFunctionalHotbarItem implements PlayerJoinListener, PlayerQuitListener {

	private static final Map<Integer, FunctionalHotbarItem> ITEMS = new HashMap<>(5);
	private static final ItemStack AIR = new ItemStack(Material.AIR);

	static{
		initialize(
			new CheckpointTeleporter(),
			new CheckpointsMenuOpener(),
			new ParkoursMenuOpener(),
			new HideModeToggler(),
			new MyMenuOpener()
		);
	}

	private static void initialize(FunctionalHotbarItem... items){
		for(int slotIndex = 0; slotIndex < 5; slotIndex++) ITEMS.put(slotIndex * 2, items[slotIndex]);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();

		initializeSlots(player);

		PlayerLocaleChange.applyIfLocaleChanged(player, 100, p -> ControlFunctionalHotbarItem.updateAllSlots(p));
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){
		initializeSlots(event.getPlayer());
	}

	@EventHandler
	public void clickSlot(PlayerInteractEvent event){
		Action action = event.getAction();

		//アイテムを持った状態でのメインハンドによるクリックでなければ戻る
		if(action == Action.PHYSICAL || event.getHand() != EquipmentSlot.HAND || !event.hasItem()) return;

		//クリックしたプレイヤーを取得する
		Player player = event.getPlayer();

		//ユーザーを取得する
		User user = toUser(player);

		//クリックされたスロットの番号を取得する
		Integer clickedSlotIndex = player.getInventory().getHeldItemSlot();

		//対応したアイテムが存在しなければ戻る
		if(!ITEMS.containsKey(clickedSlotIndex)) return;

		FunctionalHotbarItem item = ITEMS.get(clickedSlotIndex);

		//対応したアイテムでなければ戻る
		if(!item.isSimilar(event.getItem())) return;

		//右クリックかどうか
		ClickType click = action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK ? ClickType.RIGHT : ClickType.LEFT;

		//処理をする
		item.onClick(user, click);

		event.setCancelled(true);
	}

	@EventHandler
	public void controlSlot(InventoryClickEvent event){
		HumanEntity human = event.getWhoClicked();

		//クリックしたのがプレイヤーでなければ戻る
		if(!(human instanceof Player)) return;

		Player player = (Player) human;

		Inventory inventory = event.getClickedInventory();

		//クリックされたのがプレイヤーのインベントリでなければ戻る
		if(!(inventory instanceof PlayerInventory)) return;

		//クリエイティブモードでなければ全ての操作をキャンセルする
		if(player.getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event){
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
	}

	@EventHandler
	public void onPickUp(EntityPickupItemEvent event){
		Entity entity = event.getEntity();

		if(entity instanceof Player)
			if(((Player) entity).getGameMode() != GameMode.CREATIVE)
				event.setCancelled(true);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		clearSlots(event.getPlayer());
	}

	public static void initializeSlots(Player player){
		//スロットにアイテムを配置する
		ITEMS.forEach((slotIndex, item) -> player.getInventory().setItem(slotIndex, item.build(toUser(player))));
	}

	public static void updateAllSlots(Player player){
		applyToAllSlots(slotIndex -> updateSlot(player, slotIndex));
	}

	public static void updateSlot(Player player, Integer slotIndex){
		//対応したアイテムが存在すればそれを再配置する
		if(ITEMS.containsKey(slotIndex)) player.getInventory().setItem(slotIndex, ITEMS.get(slotIndex).build(toUser(player)));
	}

	public static void clearSlots(Player player){
		applyToAllSlots(slotIndex -> player.getInventory().setItem(slotIndex, AIR));
	}

	private static void applyToAllSlots(Consumer<Integer> apply){
		for(int slotIndex = 0; slotIndex <= 8; slotIndex += 2) apply.accept(slotIndex);
	}

	private static User toUser(Player player){
		return Users.getInstnace().getUser(player);
	}

}
