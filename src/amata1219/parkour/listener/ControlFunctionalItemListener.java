package amata1219.parkour.listener;

import java.util.function.Consumer;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import amata1219.parkour.function.ToggleHideMode;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.StageCategory;
import amata1219.parkour.ui.stage.StageSelectionUISet;
import amata1219.parkour.user.CheckpointSet;
import amata1219.parkour.user.User;
import amata1219.parkour.user.UserSet;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.enchantment.GleamEnchantment;
import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.listener.PlayerQuitListener;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.message.MessageColor;
import amata1219.amalib.message.MessageTemplate;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.tuplet.Tuple;

public class ControlFunctionalItemListener implements PlayerJoinListener, PlayerQuitListener {

	private final Tuple<ItemStack, Consumer<User>> teleporterToLastCheckpoint;
	private final Tuple<ItemStack, Consumer<User>> checkpointSelector;
	private final Tuple<ItemStack, Consumer<User>> stageSelector;
	private final Tuple<ItemStack, Consumer<User>> hideModeToggler;
	private final Tuple<ItemStack, Consumer<User>> menuOpener;
	private final ItemStack empty = new ItemStack(Material.AIR);

	private final UserSet users = UserSet.getInstnace();

	public ControlFunctionalItemListener(){
		ItemStack itemOfTeleporterToLastCheckpoint = new ItemStack(Material.FEATHER);

		applyMetaToItem(itemOfTeleporterToLastCheckpoint, StringColor.color("&b-Teleporter to last checkpoint"));

		//最終チェックポイントにテレポートさせるアイテムの機能内容を定義する
		teleporterToLastCheckpoint = new Tuple<>(itemOfTeleporterToLastCheckpoint, user -> {
			Player player = user.asBukkitPlayer();

			//アスレをプレイ中でなければ戻る
			if(!user.isPlayingWithParkour()){
				MessageColor.color("&c-Operation blocked &7-@ &c-You are not playing with parkour").displayOnActionBar(player);
				return;
			}

			//プレイ中のアスレを取得する
			Parkour parkourPlayingNow = user.parkourPlayingNow;

			CheckpointSet checkpoints = user.checkpoints;

			//最終チェックポイントを取得する
			ImmutableEntityLocation lastCheckpoint = checkpoints.getLastCheckpoint(parkourPlayingNow);

			//無ければ戻る
			if(lastCheckpoint == null){
				//表示例: Operation blocked @ Missing last checkpoint
				MessageColor.color("&c-Operation blocked &7-@ &c-Missing last checkpoint").displayOnActionBar(player);
				return;
			}

			//最終チェックポイントにテレポートさせる
			player.teleport(lastCheckpoint.asBukkitLocation());

			int displayCheckAreaNumber = checkpoints.getCheckpointSize(parkourPlayingNow);

			//アスレ名を取得する
			String parkourName = parkourPlayingNow.name;

			//表示例: Teleported to checkpoint 1 @ Update1!
			MessageTemplate.capply("&b-Teleported to a checkpoint &0 &7-@ &b-$1-&r-&b-!", displayCheckAreaNumber, parkourName).displayOnActionBar(player);
		});

		ItemStack itemOfCheckpointSelector = new ItemStack(Material.FEATHER);

		applyMetaToItem(itemOfCheckpointSelector, StringColor.color("&b-Checkpoint selector"));

		//ステージ内の最終チェックポイント一覧を開くアイテムの機能内容を定義する
		checkpointSelector = new Tuple<>(itemOfCheckpointSelector, user -> {
			Player player = user.asBukkitPlayer();

			//どこかのステージにいれば最終チェックポイント一覧を開く
			if(user.currentStage != null) user.inventoryUISet.lastCheckpointUI.openInventory(player);

			//無ければ警告する
			else MessageColor.color("&c-Operation blocked &7-@ &c-You are not on any stage").displayOnActionBar(player);
		});

		ItemStack itemOfStageSelector = new ItemStack(Material.FEATHER);

		applyMetaToItem(itemOfStageSelector, StringColor.color("&b-Stage selector"));

		//ステージ一覧を開くアイテムの機能内容を定義する
		stageSelector = new Tuple<>(itemOfStageSelector, user -> {
			//プレイヤーが今いるステージを取得する
			Stage stage = user.getCurrentStage();

			//ステージのカテゴリーを取得する
			StageCategory category = stage != null ? stage.category : StageCategory.NORMAL;

			//カテゴリーに対応したステージリストを開かせる
			StageSelectionUISet.getInstance().getStagesUI(category).openInventory(user.asBukkitPlayer());

		});

		ItemStack itemOfHideModeToggler = new ItemStack(Material.FEATHER);

		applyMetaToItem(itemOfHideModeToggler, StringColor.color("&b-Hide mode toggler"));

		//非表示モードを切り替えるアイテムの機能内容を定義する
		hideModeToggler = new Tuple<>(itemOfHideModeToggler, user -> {
			ToggleHideMode.getInstance().change(user);
		});

		ItemStack itemOfMenuOpener = new ItemStack(Material.FEATHER);

		applyMetaToItem(itemOfMenuOpener, StringColor.color("&b-Menu opener"));

		//メニューを開くアイテムの機能内容を定義する
		menuOpener = new Tuple<>(itemOfMenuOpener, user -> user.inventoryUISet.menu.openInventory(user.asBukkitPlayer()));
	}

	private void applyMetaToItem(ItemStack item, String displayName){
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
		GleamEnchantment.gleam(item);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		initializeSlots(event.getPlayer());
	}

	@EventHandler
	public void clickSlot(PlayerInteractEvent event){
		//クリック以外の動作であれば戻る
		if(event.getAction() == Action.PHYSICAL) return;

		//メインハンドのクリックでなければ戻る
		if(event.getHand() != EquipmentSlot.HAND) return;

		//アイテムをクリックしていなければ戻る
		if(!event.hasItem()) return;

		//クリックしたプレイヤーを取得する
		Player player = event.getPlayer();

		//ユーザーを取得する
		User user = users.getUser(player);

		//スロットに対応した処理をする
		switch(player.getInventory().getHeldItemSlot()){
		case 0:
			teleporterToLastCheckpoint.second.accept(user);
			break;
		case 2:
			checkpointSelector.second.accept(user);
			break;
		case 4:
			stageSelector.second.accept(user);
			break;
		case 6:
			hideModeToggler.second.accept(user);
			break;
		case 8:
			menuOpener.second.accept(user);
			break;
		default:
			return;
		}
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
	public void onQuit(PlayerQuitEvent event){
		clearSlots(event.getPlayer());
	}

	public void initializeSlots(Player player){
		Inventory inventory = player.getInventory();

		inventory.setItem(0, teleporterToLastCheckpoint.first);
		inventory.setItem(2, checkpointSelector.first);
		inventory.setItem(4, stageSelector.first);
		inventory.setItem(6, hideModeToggler.first);
		inventory.setItem(8, menuOpener.first);
	}

	public void clearSlots(Player player){
		Inventory inventory = player.getInventory();

		//ホットバーの偶数スロットをクリアする
		for(int slotIndex = 0; slotIndex <= 8; slotIndex += 2)
			inventory.setItem(slotIndex, empty);
	}

}
