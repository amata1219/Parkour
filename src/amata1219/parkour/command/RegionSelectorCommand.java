package amata1219.parkour.command;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.Main;
import amata1219.parkour.item.RegionSelector;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.selection.ParkourRegionSelection;

public class RegionSelectorCommand implements Command {

	private final ParkourSet parkourSet = Main.getParkourSet();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender))
			return;

		//第1引数をアスレ名として取得する
		String parkourName = args.next();

		//アスレが存在しなければエラーとする
		if(!parkourSet.isParkourExists(parkourName)){
			sender.warn(StringTemplate.apply(": Value error > [$0]は存在しません。", parkourName));
			return;
		}

		//アスレを取得する
		Parkour parkour = parkourSet.getParkour(parkourName);

		Player player = sender.asPlayerCommandSender();
		Inventory inventory = player.getInventory();

		//プレイヤーのインベントリから範囲選択ツールを削除する
		for(ItemStack item : inventory.getContents()){
			if(item != null && item.getType() == Material.STONE_AXE && item.containsEnchantment(Icon.GLEAM_ENCHANTMENT))
				player.getInventory().remove(item);
		}

		ParkourRegionSelection selector = Main.getUserSet().users.get(player.getUniqueId()).parkourRegionSelector = new ParkourRegionSelection(parkour);

		//範囲選択ツールを複製する
		ItemStack item = RegionSelector.SELECTOR.clone();

		selector.updateDisplayName(item);

		//セレクターを追加する
		inventory.addItem(item);

		sender.info(": Success > 範囲選択ツールを付与しました。");
	}

}
