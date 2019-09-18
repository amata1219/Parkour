package amata1219.beta.parkour.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import amata1219.beta.parkour.util.Range;
import graffiti.ArgList;
import graffiti.PlayerCommand;
import graffiti.Text;

public class DirectionCommand implements PlayerCommand {

	@Override
	public void onCommand(Player player, ArgList args) {
		args.nextFloat().flatBind(
			y -> args.nextFloat().ifJust(
			p -> execute(player, y, p)
		)).ifNothing(() -> Text.of("&c-/direction [yaw] [pitch]").actionbar(player));
	}

	private void execute(Player player, float yaw, float pitch){
		Location loc = player.getLocation();
		loc.setYaw(yaw = Range.limit(-180.0f, 179.9f, yaw));
		loc.setPitch(pitch = Range.limit(-90.0f, 90.0f, pitch));
		player.teleport(loc);
		Text.of("&b-ヨーを%s、ピッチを%sに設定しました。", "&b-You set yaw to %s and pitch to %s.", player).format(yaw, pitch).actionbar(player);
	}

}
