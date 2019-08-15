package amata1219.parkour.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.GameMode;
import org.bukkit.Location;
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

import amata1219.parkour.function.HotbarItem;
import amata1219.parkour.function.HotbarItem.ClickType;
import amata1219.parkour.function.ToggleHideMode;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.ui.parkour.ParkourMenuUI;
import amata1219.parkour.user.Checkpoints;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.enchantment.GleamEnchantment;
import amata1219.amalib.inventory.ui.dsl.InventoryUI;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.listener.PlayerQuitListener;
import amata1219.amalib.location.ImmutableLocation;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringLocalize;
import amata1219.amalib.string.message.MessageColor;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.amalib.tuplet.Triple;
import amata1219.amalib.tuplet.Tuple;

public class ControlFunctionalItemListener implements PlayerJoinListener, PlayerQuitListener {

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
	 */

	private static final Map<Integer, HotbarItem> ITEMS = new HashMap<>(5);

	static{
		initialize(
			new HotbarItem(Material.LIGHT_BLUE_DYE,
				//アイテムのビルド設定をする
				(item, user) -> {
					ItemMeta meta = item.getItemMeta();

					//プレイヤーを取得する
					Player player = user.asBukkitPlayer();

					meta.setDisplayName(StringLocalize.capply("&b-最新/最終チェックポイントにテレポートする | &b-Teleport to Latest/Last Checkpoint", player));
					meta.setLore(Arrays.asList(
						StringLocalize.capply("&7-左クリックすると最後に登録したチェックポイントにテレポートします。 | &7-Ri", player)
					));
				},

				//クリック時の処理を定義する
				(user, clickType) -> {

				}
			)
		);


		/*initialize(
			new HotbarItem(Material.LIGHT_BLUE_DYE, StringColor.color("&b-最新/最終チェックポイントにテレポートする | &b-Teleport to Latest/Last Checkpoint"), (user, clickType) -> {
				Player player = user.asBukkitPlayer();

				//アスレをプレイ中でなければ戻る
				if(!user.isPlayingWithParkour()){
					MessageColor.color("&c-アスレチックのプレイ中でないため実行出来ません | &c-Operation blocked &7-@ &c-You are not playing with parkour")
					.localize().displayOnActionBar(player);
					return;
				}

				//プレイ中のアスレを取得する
				Parkour parkour = user.parkourPlayingNow;
				Checkpoints checkpoints = user.checkpoints;

				if(!checkpoints.containsParkour(parkour)){
					MessageColor.color("&c-チェックポイントが設定されていないため実行出来ません | &c-Operation blocked &7-@ &c-Missing checkpoint")
					.localize().displayOnActionBar(player);
					return;
				}

				//右クリックしたのであれば最終チェックポイントを、左クリックしたのであれば最新チェックポイントを取得する
				ImmutableLocation checkpoint = clickType == ClickType.RIGHT ? checkpoints.getLastCheckpoint(parkour) : checkpoints.getLatestCheckpoint(parkour);

				//チェックポイントが無ければ戻る
				if(checkpoint == null){
					MessageColor.color("&c-チェックポイントが設定されていないため実行出来ません | &c-Operation blocked &7-@ &c-Missing checkpoint")
					.localize().displayOnActionBar(player);
					return;
				}

				//チェックエリアの番号を取得し表示用に+1する
				int displayCheckAreaNumber = (clickType == ClickType.RIGHT ? checkpoints.getLastCheckpointNumber(parkour) : checkpoints.getLatestCheckpointNumber(parkour)) + 1;

				//チェックポイントにテレポートさせる
				player.teleport(checkpoint.asBukkit());

				//表示例: Update7 の チェックポイント1 にテレポートしました | Teleported to checkpoint 1 @ Update7!
				MessageTemplate.capply("$1-&b-の チェックポイント$0 にテレポートしました | &b-Teleported to checkpoint &0 &7-@ &b-$1-&r-&b-!", displayCheckAreaNumber, parkour.name)
				.localize().displayOnActionBar(player);

			}, StringColor.color("&7-: &b-"), ""),

			new HotbarItem(Material.CYAN_DYE, StringColor.color("&b-Checkpoints in category"), (user, clickType) -> {
				Player player = user.asBukkitPlayer();

				//どこのアスレにもいなければ戻る
				if(user.currentParkour == null){
					MessageColor.color("&c-Operation blocked &7-@ &c-You are not on any parkour").displayOnActionBar(player);
					return;
				}

				//右クリックしたのであれば最終、左クリックしたのであれば最新のチェックポイントリストを表示する
				InventoryUI inventoryUI = clickType == ClickType.RIGHT ? user.inventoryUserInterfaces.lastCheckpointSelectionUI : user.inventoryUserInterfaces.latestCheckpointSelectionUI;
				inventoryUI.openInventory(player);
			}),

			new HotbarItem(Material.HEART_OF_THE_SEA, StringColor.color("&b-Teleporter to checkpoint"), (user, clickType) -> {
				//プレイヤーが今いるアスレを取得する
				Parkour parkour = user.currentParkour;

				//ステージのカテゴリーを取得する
				ParkourCategory category = parkour != null ? parkour.category : ParkourCategory.NORMAL;

				//カテゴリーに対応したステージリストを開かせる
				InventoryUI inventoryUI = null;

				//カテゴリーに対応したアスレリストを取得する
				switch(category){
				case UPDATE:
					inventoryUI = user.inventoryUserInterfaces.updateParkourSelectionUI;
					break;
				case EXTEND:
					inventoryUI = user.inventoryUserInterfaces.extendParkourSelectionUI;
					break;
				default:
					inventoryUI = ParkourMenuUI.getInstance().getInventoryUI(category);
					break;
				}

				inventoryUI.openInventory(user.asBukkitPlayer());
			}),

			new HotbarItem(Material.PRISMARINE_SHARD, StringColor.color("&b-Teleporter to checkpoint"), (user, clickType) -> {
				ToggleHideMode.getInstance().change(user);
			}),

			new HotbarItem(Material.FEATHER, StringColor.color("&b-Teleporter to checkpoint"), (user, clickType) -> {
				user.inventoryUserInterfaces.menuUI.openInventory(user.asBukkitPlayer());
			})
		);*/
	}

	private static void initialize(HotbarItem... items){
		for(int slotIndex = 0; slotIndex < 5; slotIndex++) ITEMS.put(slotIndex * 2, items[slotIndex]);
	}

	private static ControlFunctionalItemListener instance;

	public static void load(){
		instance = new ControlFunctionalItemListener();
	}

	public static ControlFunctionalItemListener getInstance(){
		return instance;
	}

	private final Users users = Users.getInstnace();

	public ControlFunctionalItemListener(){
	}

	public void setNotifierGleam(Player player, boolean gleam){
		ItemStack item = player.getInventory().getItem(0);

		if(!teleporterToLastOrLatestCheckpoint.first.equals(item) && !teleporterToLastOrLatestCheckpoint.second.equals(item)) return;

		if(gleam) GleamEnchantment.gleam(item);
		else GleamEnchantment.tarnish(item);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		initializeSlots(event.getPlayer());
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){
		initializeSlots(event.getPlayer());
	}

	@EventHandler
	public void clickSlot(PlayerInteractEvent event){
		Action action = event.getAction();

		//クリック以外の動作であれば戻る
		if(action == Action.PHYSICAL) return;

		//メインハンドのクリックでなければ戻る
		if(event.getHand() != EquipmentSlot.HAND) return;

		//アイテムをクリックしていなければ戻る
		if(!event.hasItem()) return;

		//クリックしたプレイヤーを取得する
		Player player = event.getPlayer();

		//ユーザーを取得する
		User user = users.getUser(player);

		//クリックしたアイテムを取得する
		ItemStack item = event.getItem();

		//スロットに対応した処理をする
		switch(player.getInventory().getHeldItemSlot()){
		case 0:
			if(!teleporterToLastOrLatestCheckpoint.first.equals(item) && !teleporterToLastOrLatestCheckpoint.second.equals(item)) return;

			teleporterToLastOrLatestCheckpoint.third.accept(user, action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK);
			break;
		case 2:
			if(!lastOrLatestCheckpointSelector.first.equals(item)) return;

				lastOrLatestCheckpointSelector.second.accept(user, action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK);
			break;
		case 4:
			if(!stageSelector.first.equals(item)) return;

			stageSelector.second.accept(user);
			break;
		case 6:
			if(!hideModeToggler.first.equals(item)) return;

			hideModeToggler.second.accept(user);
			break;
		case 8:
			if(!menuOpener.first.equals(item)) return;

			menuOpener.second.accept(user);
			break;
		default:
			return;
		}

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

	public void initializeSlots(Player player){
		Inventory inventory = player.getInventory();

		User user = users.getUser(player);

		boolean isInCheckArea = false;

		//アスレをプレイ中の場合
		if(user.isPlayingWithParkour()){
			Location location = player.getLocation();

			//現在地点にあるチェックエリアのリストを取得する
			List<ParkourRegion> checkAreas = Parkours.getInstance().chunksToCheckAreasMap.get(location);

			ParkourRegion result = null;
			for(ParkourRegion checkArea : checkAreas){
				if(!checkArea.isIn(location)) continue;

				result = checkArea;
				break;
			}

			isInCheckArea = result != null;
		}

		inventory.setItem(0, isInCheckArea ? teleporterToLastOrLatestCheckpoint.second : teleporterToLastOrLatestCheckpoint.first);
		inventory.setItem(2, lastOrLatestCheckpointSelector.first);
		inventory.setItem(4, stageSelector.first);
		inventory.setItem(6, hideModeToggler.first);
		inventory.setItem(8, menuOpener.first);
	}

	public void clearSlots(Player player){
		Inventory inventory = player.getInventory();

		//ホットバーの偶数スロットをクリアする
		for(int slotIndex = 0; slotIndex <= 8; slotIndex += 2) inventory.setItem(slotIndex, empty);
	}

}
