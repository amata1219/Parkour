package amata1219.parkour.command;

import static graffiti.Maybe.*;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.selection.RegionSelectionSet;
import graffiti.Command;
import graffiti.SafeCast;
import graffiti.Text;

public class NewParkourEditCommand implements Command {

	private final RegionSelectionSet selections = RegionSelectionSet.getInstance();

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		unit(SafeCast.down(sender, Player.class)).flatBind(
			p -> unit(args[0]).flatBind(
			n -> unit(ParkourSet.getInstance().getParkour(n)).ifJust(
			c -> execute(p, n)
		))).ifNothing(this::failure);
	}

	public void execute(Player player, String parkourName){
		UUID uuid = player.getUniqueId();

		selections.setNewSelection(uuid, parkourName);

		ItemStack selectionTool = selections.makeNewSelectionTool(uuid);
		player.getInventory().addItem(selectionTool);

		Text.of("%s-&r-用の範囲選択ツールを与えました。").format(parkourName).accept(player::sendMessage);
	}

	private void failure(){
		Text.of("&c-/parkouredit [アスレ名]");
	}

}
