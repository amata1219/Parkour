package amata1219.parkour.function.hotbar;

import java.util.HashMap;
import java.util.Map;
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

import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.listener.PlayerQuitListener;
import amata1219.amalib.schedule.Sync;

public class ControlFunctionalHotbarItem implements PlayerJoinListener, PlayerQuitListener {

	/*
	 * Checkpoint, To Spawn, Teleporter, User Config Athletic Selector
Return to Checkpoint, Invisible, Return to Spawn, Leaderborad, Profile
Teleport to Last Checkpoint
You reached Checkpoint #1 after 00:30.361
Congratulations on completing the parkour!
You finished int 01:0.4823!
Try again to ge an even better record!
You finished this part of the parkour in 00:09.603. //checkpoint
click to open
Kits & Perks
Hats
Click to browse! Click to select!
My Profile
Settings
~ joined the lobby!
Game Menu
Lobby Selector
説明は灰色、値に色を付ける
Click here to view it!
Return to your last checkpoint
You have received 15 mana from console
You have completed this course 1 times!

	last latest / all in category / parkours / hider / menu

	 */

	private static final Map<Integer, FunctionalHotbarItem> ITEMS = new HashMap<>(5);
	private static final ItemStack AIR = new ItemStack(Material.AIR);

	static{
		initialize(
			new QuickTeleporter(),
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
		Sync.define(() -> initializeSlots(event.getPlayer())).executeLater(10);
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
		User user = Users.getInstnace().getUser(player);

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
		ITEMS.forEach((slotIndex, item) -> player.getInventory().setItem(slotIndex, item.build(player)));
	}

	public static void updateSlot(Player player, Integer slotIndex){
		//対応したアイテムが存在すればそれを再配置する
		if(ITEMS.containsKey(slotIndex)) player.getInventory().setItem(slotIndex, ITEMS.get(slotIndex).build(player));
	}

	public static void clearSlots(Player player){
		Inventory inventory = player.getInventory();

		//ホットバーの偶数スロットをクリアする
		for(int slotIndex = 0; slotIndex <= 8; slotIndex += 2) inventory.setItem(slotIndex, AIR);
	}

}
