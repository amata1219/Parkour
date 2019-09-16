package amata1219.parkour.command;

import static graffiti.Maybe.*;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.beta.parkour.course.Course;
import amata1219.beta.parkour.course.CourseSet;
import amata1219.parkour.selection.RegionSelectionSet;
import graffiti.Args;
import graffiti.Command;
import graffiti.SafeCast;
import graffiti.Text;

public class WandCommand implements Command {

	private final RegionSelectionSet selections = RegionSelectionSet.getInstance();

	@Override
	public void onCommand(CommandSender sender, Args args) {
		unit(SafeCast.down(sender, Player.class)).flatBind(
			p -> args.next().flatBind(
			n -> CourseSet.instance().get(n)).ifJust(
			c -> execute(p, c)
		)).ifNothing(() -> Text.of("&c-/parkouredit [アスレ名]").sendTo(sender));
	}

	public void execute(Player player, Course course){
		UUID uuid = player.getUniqueId();
		String courseName = course.name;

		selections.setNewSelection(uuid, courseName);

		ItemStack selectionTool = selections.makeNewSelectionTool(uuid);
		player.getInventory().addItem(selectionTool);

		Text.of("%s-&r-用の範囲選択ツールを与えました。").format(courseName).sendTo(player);
	}

}
