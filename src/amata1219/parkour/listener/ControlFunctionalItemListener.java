package amata1219.parkour.listener;

import java.util.List;
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
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.message.MessageColor;
import amata1219.amalib.string.message.MessageTemplate;
import amata1219.amalib.tuplet.Triple;
import amata1219.amalib.tuplet.Tuple;

public class ControlFunctionalItemListener implements PlayerJoinListener, PlayerQuitListener {

	private static ControlFunctionalItemListener instance;

	public static void load(){
		instance = new ControlFunctionalItemListener();
	}

	public static ControlFunctionalItemListener getInstance(){
		return instance;
	}

	private final Triple<ItemStack, ItemStack, BiConsumer<User, Boolean>> teleporterToLastOrLatestCheckpoint;
	private final Tuple<ItemStack, Consumer<User>> checkpointSelector;
	private final Tuple<ItemStack, Consumer<User>> stageSelector;
	private final Tuple<ItemStack, Consumer<User>> hideModeToggler;
	private final Tuple<ItemStack, Consumer<User>> menuOpener;
	private final ItemStack empty = new ItemStack(Material.AIR);

	private final Users users = Users.getInstnace();

	public ControlFunctionalItemListener(){
		ItemStack itemOfTeleporterToLastCheckpoint = new ItemStack(Material.PRISMARINE_CRYSTALS);

		applyMetaToItem(itemOfTeleporterToLastCheckpoint, StringColor.color("&b-Teleporter to last or latest checkpoint"));

		//同様のアイテムの発光バージョンも作成する
		ItemStack gleamingItemOfTeleporterToLastCheckpoint = itemOfTeleporterToLastCheckpoint.clone();
		GleamEnchantment.gleam(gleamingItemOfTeleporterToLastCheckpoint);

		//最終チェックポイントにテレポートさせるアイテムの機能内容を定義する
		teleporterToLastOrLatestCheckpoint = new Triple<>(itemOfTeleporterToLastCheckpoint, gleamingItemOfTeleporterToLastCheckpoint, (user, clickRight) -> {
			Player player = user.asBukkitPlayer();

			//アスレをプレイ中でなければ戻る
			if(!user.isPlayingWithParkour()){
				MessageColor.color("&c-Operation blocked &7-@ &c-You are not playing with parkour").displayOnActionBar(player);
				return;
			}

			//プレイ中のアスレを取得する
			Parkour parkour = user.parkourPlayingNow;
			Checkpoints checkpoints = user.checkpoints;

			if(!checkpoints.containsParkour(parkour)){
				//表示例: Operation blocked @ Missing last checkpoint
				MessageColor.color("&c-Operation blocked &7-@ &c-Missing checkpoint").displayOnActionBar(player);
				return;
			}

			//右クリックしたのであれば最終チェックポイントを、左クリックしたんどえあれば最新チェックポイントを取得する
			ImmutableEntityLocation checkpoint = clickRight ? checkpoints.getLastCheckpoint(parkour) : checkpoints.getLatestCheckpoint(parkour);

			//チェックポイントが無ければ戻る
			if(checkpoint == null){
				//表示例: Operation blocked @ Missing last checkpoint
				MessageColor.color("&c-Operation blocked &7-@ &c-Missing checkpoint").displayOnActionBar(player);
				return;
			}

			//チェックエリアの番号を取得し表示用に+1する
			int displayCheckAreaNumber = (clickRight ? checkpoints.getLastCheckpointNumber(parkour) : checkpoints.getLatestCheckpointNumber(parkour)) + 1;

			//チェックポイントにテレポートさせる
			player.teleport(checkpoint.asBukkitLocation());

			//表示例: Teleported to checkpoint 1 @ Update1!
			MessageTemplate.capply("&b-Teleported to checkpoint &0 &7-@ &b-$1-&r-&b-!", displayCheckAreaNumber, parkour.name).displayOnActionBar(player);
		});

		ItemStack itemOfCheckpointSelector = new ItemStack(Material.PRISMARINE_CRYSTALS);

		applyMetaToItem(itemOfCheckpointSelector, StringColor.color("&b-Checkpoint selector"));

		//ステージ内の最終チェックポイント一覧を開くアイテムの機能内容を定義する
		checkpointSelector = new Tuple<>(itemOfCheckpointSelector, user -> {
			Player player = user.asBukkitPlayer();

			//どこかのステージにいれば最終チェックポイント一覧を開く
			if(user.currentParkour != null) user.inventoryUserInterfaces.lastCheckpointUI.openInventory(player);

			//無ければ警告する
			else MessageColor.color("&c-Operation blocked &7-@ &c-You are not on any parkour").displayOnActionBar(player);
		});

		ItemStack itemOfStageSelector = new ItemStack(Material.PRISMARINE_CRYSTALS);

		applyMetaToItem(itemOfStageSelector, StringColor.color("&b-Parkour selector"));

		//ステージ一覧を開くアイテムの機能内容を定義する
		stageSelector = new Tuple<>(itemOfStageSelector, user -> {
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

		});

		ItemStack itemOfHideModeToggler = new ItemStack(Material.PRISMARINE_CRYSTALS);

		applyMetaToItem(itemOfHideModeToggler, StringColor.color("&b-Hide mode toggler"));

		//非表示モードを切り替えるアイテムの機能内容を定義する
		hideModeToggler = new Tuple<>(itemOfHideModeToggler, user -> {
			ToggleHideMode.getInstance().change(user);
		});

		ItemStack itemOfMenuOpener = new ItemStack(Material.PRISMARINE_CRYSTALS);

		applyMetaToItem(itemOfMenuOpener, StringColor.color("&b-Menu opener"));

		//メニューを開くアイテムの機能内容を定義する
		menuOpener = new Tuple<>(itemOfMenuOpener, user -> user.inventoryUserInterfaces.menuUI.openInventory(user.asBukkitPlayer()));
	}

	private void applyMetaToItem(ItemStack item, String displayName){
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
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
			if(!checkpointSelector.first.equals(item)) return;

				checkpointSelector.second.accept(user);
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
		inventory.setItem(2, checkpointSelector.first);
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
