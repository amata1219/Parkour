package amata1219.parkour.command;

import org.bukkit.entity.Player;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;

public class SetDirectionCommand implements Command {

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender))
			return;

		Player player = sender.asPlayerCommandSender();
		if(args.hasNextFloat()){
			float yaw = args.nextFloat();

			if(args.hasNext()){
				sender.warn("/setdirection [yaw] [pitch]");
			}
		}else{
			switch(args.next()){
			case "yaw":

			case "pitch":
			}
		}
	}

	@Override
	public String commandSyntax(){
		return "/setdr [yaw] [pitch], /setdr y:[yaw], /setdr p:[yaw]";
	}

	private float adjustYaw(float yaw){
		return Math.max(Math.min(yaw, 179.9f), -180.0f);
	}

	private float adjustPitch(float pitch){
		return Math.max(Math.min(pitch, 90.0f), -90.0f);
	}

}
