package amata1219.parkour;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.amalib.Plugin;
import amata1219.amalib.inventory.ui.dsl.component.Icon;
import amata1219.parkour.command.CoinCommand;
import amata1219.parkour.command.ParkourCommand;
import amata1219.parkour.command.RegionSelectorCommand;
import amata1219.parkour.command.SetDirectionCommand;
import amata1219.parkour.command.StageCommand;
import amata1219.parkour.listener.CreateUserInstanceListener;
import amata1219.parkour.listener.DisablePlayerCollisionsListener;
import amata1219.parkour.listener.DisplayRegionBorderListener;
import amata1219.parkour.listener.InteractCheckSignListener;
import amata1219.parkour.listener.PassFinishLineListener;
import amata1219.parkour.listener.PassStartLineListener;
import amata1219.parkour.listener.PlaceCheckSignListener;
import amata1219.parkour.listener.SelectRegionListener;
import amata1219.parkour.listener.SetCheckpointListener;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.stage.StageSet;
import amata1219.parkour.user.UserSet;

public class Main extends Plugin {

	//https://twitter.com/share?url=https://minecraft.jp/servers/azisaba.net&text=ここにテキスト
	//アスレTP時にチャットに送信

	private static Main plugin;
	private static StageSet stageSet;
	private static ParkourSet parkourSet;
	private static UserSet userSet;

	public static final ItemStack SELECTOR;
	public static final ItemStack AT_SIGN;
	public static final String CP_AT_SIGN = "§b§lCP @ SIGN";
	public static final ItemStack AT_PLAYER;
	public static final String CP_AT_PLAYER = "§b§lCP @ PLAYER";

	static{
		SELECTOR = new ItemStack(Material.STONE_AXE);

		ItemMeta meta = SELECTOR.getItemMeta();

		meta.setDisplayName("§7$0 §b§l>§r §70,0,0 §b§l-§r §70,0,0 §b§l@§r §7world");
		meta.addEnchant(Icon.GLEAM_ENCHANTMENT, 1, true);

		SELECTOR.setItemMeta(meta);
	}

	static{
		AT_SIGN = new ItemStack(Material.SIGN);

		ItemMeta meta = AT_SIGN.getItemMeta();
		meta.setDisplayName("§bCP @ SIGN");
		meta.addEnchant(Icon.GLEAM_ENCHANTMENT, 1, true);

		AT_SIGN.setItemMeta(meta);
	}

	static{
		AT_PLAYER = new ItemStack(Material.SIGN);

		ItemMeta meta = AT_PLAYER.getItemMeta();
		meta.setDisplayName("§bCP @ PLAYER");
		meta.addEnchant(Icon.GLEAM_ENCHANTMENT, 1, true);

		AT_PLAYER.setItemMeta(meta);
	}

	@Override
	public void onEnable(){
		plugin = this;

		parkourSet = new ParkourSet();
		stageSet = new StageSet();
		userSet = new UserSet();

		registerCommands(
			new ParkourCommand(),
			new StageCommand(),
			new CoinCommand(),
			new SetDirectionCommand(),
			new RegionSelectorCommand()
		);

		registerListeners(
			new CreateUserInstanceListener(),
			new DisablePlayerCollisionsListener(),
			new SetCheckpointListener(),
			new PassStartLineListener(),
			new PassFinishLineListener(),
			new SelectRegionListener(),
			new DisplayRegionBorderListener(),
			new PlaceCheckSignListener(),
			new InteractCheckSignListener()
		);

	}

	@Override
	public void onDisable(){
		super.onDisable();
	}

	public static Main getPlugin(){
		return plugin;
	}

	public static StageSet getStageSet(){
		return stageSet;
	}

	public static ParkourSet getParkourSet(){
		return parkourSet;
	}

	public static UserSet getUserSet(){
		return userSet;
	}

	public static World getCreativeWorld(){
		return Bukkit.getWorld("Creative");
	}

}
