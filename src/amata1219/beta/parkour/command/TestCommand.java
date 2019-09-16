package amata1219.beta.parkour.command;

import static graffiti.Maybe.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import graffiti.ArgList;
import graffiti.PlayerCommand;
import graffiti.Text;

public class TestCommand implements PlayerCommand {

	@Override
	public void onCommand(Player player, ArgList args) {
		args.next().flatBind(
			n -> unit(Bukkit.getPlayerExact(n)).flatBind(
			p -> args.next().flatBind(
			s -> unit(Bukkit.getWorld(s)).flatBind(
			w -> args.nextInt().flatBind(
			x -> args.nextInt().flatBind(
			y -> args.nextInt().ifJust(
			z -> execute(p, w, x, y, z)
		))))))).ifNothing(() -> Text.of("&c-/test [player] [world] [x] [y] [z]"));
	}

	private void execute(Player player, World world, int x, int y, int z){
		player.teleport(new Location(world, x, y, z));
	}

}

