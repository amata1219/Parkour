package amata1219.parkour.selection;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.string.StringColor;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.tuplet.Tuple;
import amata1219.parkour.parkour.Parkour;

public class RegionSelectionSet implements Listener {

	private static RegionSelectionSet instance;

	//範囲選択用のツール
	public static final ItemStack selectionTool;

	static{
		selectionTool = new ItemStack(Material.STONE_AXE);

		//発光用エンチャントを付与する
		selectionTool.addEnchantment(Icon.GLEAM_ENCHANTMENT, 1);
	}

	public static RegionSelectionSet getInstance(){
		return instance != null ? instance : (instance = new RegionSelectionSet());
	}

	private final HashMap<UUID, Tuple<Parkour, RegionSelection>> selections = new HashMap<>();

	private RegionSelectionSet(){

	}

	//新しいセレクションを作成する
	public void setNewSelection(UUID uuid, Parkour parkour){
		RegionSelection selection = new RegionSelection();

		//アスレのあるワールドをセットする
		selection.setWorld(parkour.world);

		//アスレとセレクションを結び付けてセットする
		selections.put(uuid, new Tuple<>(parkour, selection));
	}

	//選択中のアスレを取得する
	public Parkour getSelectedParkour(UUID uuid){
		return selections.containsKey(uuid) ? selections.get(uuid).first : null;
	}

	//セレクションを取得する
	public RegionSelection getSelection(UUID uuid){
		return selections.containsKey(uuid) ? selections.get(uuid).second : null;
	}

	//新しい範囲選択ツールを作成する
	public ItemStack makeNewSelectionTool(UUID uuid){
		return applySelectionInformationToDisplayName(uuid, selectionTool.clone());
	}

	//範囲選択ツールの表示名に選択情報を適用する
	public ItemStack applySelectionInformationToDisplayName(UUID uuid, ItemStack tool){
		if(!selections.containsKey(uuid))
			return tool;

		//選択中のアスレを取得する
		Parkour parkour = getSelectedParkour(uuid);

		//セレクションを取得する
		RegionSelection selection = getSelection(uuid);

		//カンマを灰色にする
		String selectionInformation = selection.toString().replace(",", StringColor.color("&7-,-&b"));

		//表示名を作成する
		String displayName = StringTemplate.applyWithColor("&b-$0 &7-@ &b", parkour.name, selectionInformation);

		ItemMeta meta = tool.getItemMeta();

		//表示名を設定する
		meta.setDisplayName(displayName);

		//変更を適用する
		tool.setItemMeta(meta);

		return tool;
	}

	@EventHandler
	public void setSelection(PlayerInteractEvent event){
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		//範囲選択中のプレイヤーでなければ戻る
		if(!selections.containsKey(uuid))
			return;

		Action action = event.getAction();

		//ブロックをクリックしていなければ戻る
		if(action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR || action == Action.PHYSICAL)
			return;

		//ブロックやアイテムをクリックしていなければ戻る
		if(!event.hasBlock() || !event.hasItem())
			return;

		ItemStack clickedItem = event.getItem();

		//範囲選択ツールでなければ戻る
		if(clickedItem.getType() != Material.STONE_AXE || clickedItem.containsEnchantment(Icon.GLEAM_ENCHANTMENT))
			return;

		//セレクションを取得する
		RegionSelection selection = getSelection(uuid);

		//クリックしたブロックの座標を取得する
		Location clickedLocation = event.getClickedBlock().getLocation();

		//明示的に条件分岐する
		switch(action){
		case LEFT_CLICK_BLOCK:
			selection.setBoundaryCorner1(clickedLocation);
			break;
		case RIGHT_CLICK_BLOCK:
			selection.setBoundaryCorner2(clickedLocation);
			break;
		default:
			return;
		}

		event.setCancelled(true);

		//範囲選択ツールの表示名を更新する
		applySelectionInformationToDisplayName(uuid, clickedItem);
	}

	@EventHandler
	public void clearSelection(PlayerQuitEvent event){
		clearSelection(event.getPlayer());
	}

	//選択をクリアする
	public void clearSelection(Player player){
		selections.remove(player);
	}

}
