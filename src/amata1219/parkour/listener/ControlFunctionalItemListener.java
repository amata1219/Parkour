package amata1219.parkour.listener;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import amata1219.parkour.function.ToggleHideMode;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.user.CheckpointSet;
import amata1219.parkour.user.User;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.message.MessageColor;
import amata1219.amalib.message.MessageTemplate;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.tuplet.Tuple;

public class ControlFunctionalItemListener {

	/*
	 * last cp
	 *
	 * stage
	 *
	 * toggle hide mode change
	 *
	 * menu > setting
	 *
	 */

	private static final Tuple<ItemStack, Consumer<User>> teleporterToLastCheckpoint;
	private static final Tuple<ItemStack, Consumer<User>> checkpointSelector;
	private static final Tuple<ItemStack, Consumer<User>> stageSelector;
	private static final Tuple<ItemStack, Consumer<User>> hideModeToggler;
	private static final Tuple<ItemStack, Consumer<User>> menuOpener;
	private static final ItemStack empty = new ItemStack(Material.AIR);

	static{
		ItemStack itemOfTeleporterToLastCheckpoint = new ItemStack(Material.FEATHER);

		applyMetaToItem(itemOfTeleporterToLastCheckpoint, StringColor.color("&b-Teleporter to last checkpoint"));

		//最終チェックポイントにテレポートさせるアイテムの機能内容を定義する
		teleporterToLastCheckpoint = new Tuple<>(itemOfTeleporterToLastCheckpoint, user -> {
			//アスレをプレイ中でなければ戻る
			if(!user.isPlayingWithParkour()) return;

			//プレイ中のアスレを取得する
			Parkour parkourPlayingNow = user.parkourPlayingNow;

			CheckpointSet checkpoints = user.checkpoints;

			//最終チェックポイントを取得する
			ImmutableEntityLocation lastCheckpoint = checkpoints.getLastCheckpoint(parkourPlayingNow);

			//無ければ戻る
			if(lastCheckpoint == null){
				//表示例: Operation blocked @ Missing last checkpoint
				MessageColor.color("&c-Operation blocked &7-@ &c-Missing last checkpoint").displayOnActionBar(user.asBukkitPlayer());
				return;
			}

			Player player = user.asBukkitPlayer();

			//最終チェックポイントにテレポートさせる
			player.teleport(lastCheckpoint.asBukkitLocation());

			int displayCheckAreaNumber = checkpoints.getCheckpointSize(parkourPlayingNow);

			//アスレ名を取得する
			String parkourName = parkourPlayingNow.name;

			//表示例: Teleported to checkpoint 1 @ Update1!
			MessageTemplate.applyWithColor("&b-Teleported to a checkpoint &0 &7-@ &b-$1-&r-&b-!", displayCheckAreaNumber, parkourName).displayOnActionBar(player);
		});

		ItemStack itemOfCheckpointSelector = new ItemStack(Material.FEATHER);

		applyMetaToItem(itemOfCheckpointSelector, StringColor.color("&b-Checkpoint selector"));

		//ステージ内の最終チェックポイント一覧を開くアイテムの機能内容を定義する
		checkpointSelector = new Tuple<>(itemOfCheckpointSelector, user -> {

		});

		ItemStack itemOfStageSelector = new ItemStack(Material.FEATHER);

		applyMetaToItem(itemOfStageSelector, StringColor.color("&b-Stage selector"));

		//ステージ一覧を開くアイテムの機能内容を定義する
		stageSelector = new Tuple<>(itemOfStageSelector, user -> {

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
		menuOpener = new Tuple<>(itemOfMenuOpener, user -> {
			user.inventoryUIs.menuUI.openInventory(user.asBukkitPlayer());
		});
	}

	private static void applyMetaToItem(ItemStack item, String displayName){
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(displayName);

		//発光させる
		meta.addEnchant(Icon.GLEAM_ENCHANTMENT, 1, true);

		item.setItemMeta(meta);
	}

	@EventHandler
	public void initializeSlots(PlayerJoinEvent event){
		initializeSlots(event.getPlayer());
	}

	@EventHandler
	public void clickSlot(PlayerInteractEvent event){

	}

	@EventHandler
	public void controlSlot(InventoryClickEvent event){
		//スロットのアイテムを消せない様に、オフハンドにアイテムを入れられない様に、頭からアイテム取れない様に
	}

	@EventHandler
	public void changeSlots(PlayerGameModeChangeEvent event){

	}

	@EventHandler
	public void clearSlots(PlayerQuitEvent event){
		clearSlots(event.getPlayer());
	}

	public void initializeSlots(Player player){
		Inventory inventory = player.getInventory();

		inventory.setItem(0, teleporterToLastCheckpoint);
		inventory.setItem(2, checkpointSelector);
		inventory.setItem(4, stageSelector);
		inventory.setItem(6, hideModeToggler);
		inventory.setItem(8, menuOpener);
	}

	public void clearSlots(Player player){
		Inventory inventory = player.getInventory();

		//ホットバーの偶数スロットをクリアする
		for(int slotIndex = 0; slotIndex <= 8; slotIndex += 2)
			inventory.setItem(slotIndex, empty);
	}

}
