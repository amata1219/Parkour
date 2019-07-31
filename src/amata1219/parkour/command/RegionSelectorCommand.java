package amata1219.parkour.command;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.parkour.Main;

public class RegionSelectorCommand implements Command {

	private static final ItemStack SELECTOR;

	static{
		SELECTOR = new ItemStack(Material.STONE_AXE);

		ItemMeta meta = SELECTOR.getItemMeta();

		meta.setDisplayName("$0 > 0,0,0 - 0,0,0 @ world");
		meta.addEnchant(Icon.GLEAM_ENCHANTMENT, 1, true);

		SELECTOR.setItemMeta(meta);
	}

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender))
			return;

		//第1引数をアスレ名として取得する
		String parkourName = args.next();

		if(!Main.getParkourSet().parkourMap.containsKey(parkourName)){

		}
	}

}
