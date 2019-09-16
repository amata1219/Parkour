package amata1219.beta.parkour.command;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import amata1219.beta.parkour.course.Course;
import amata1219.beta.parkour.course.CourseSet;
import amata1219.parkour.selection.RegionSelectionSet;
import graffiti.ArgList;
import graffiti.PlayerCommand;
import graffiti.Text;

public class WandCommand implements PlayerCommand {

	private final RegionSelectionSet selections = RegionSelectionSet.getInstance();

	@Override
	public void onCommand(Player player, ArgList args) {
		args.next().flatBind(
			n -> CourseSet.instance().get(n)).ifJust(
			c -> execute(player, c)
		).ifNothing(() -> Text.of("&c-/parkouredit [アスレ名]").sendTo(player));
	}

	private void execute(Player player, Course course){
		UUID uuid = player.getUniqueId();
		String courseName = course.name;

		selections.setNewSelection(uuid, courseName);

		ItemStack selectionTool = selections.makeNewSelectionTool(uuid);
		player.getInventory().addItem(selectionTool);

		Text.of("%s-&r-用の範囲選択ツールを与えました。").format(courseName).sendTo(player);
	}

}
