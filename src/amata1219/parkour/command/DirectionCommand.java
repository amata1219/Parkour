package amata1219.parkour.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import amata1219.parkour.string.StringTemplate;

public class DirectionCommand implements Command {

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender))
			return;

		//プレイヤーとして取得する
		Player player = sender.asPlayerCommandSender();

		//プレイヤーの今いる座標を取得する
		Location location = player.getLocation();

		//アクションバーへのメッセージ表示を有効にする
		sender.displayMessageToActionbar = true;

		if(args.hasNextFloat()){
			//第1引数をyawとして取得する
			float yaw = args.nextFloat();

			//第2引数が存在しない或いはfloat型の値ではない場合は警告しつつ戻る
			if(!args.hasNextFloat()){
				sender.warn(": Syntax error > /direction [yaw] [pitch]");
				return;
			}

			//第2引数をpitchとして取得する
			float pitch = args.nextFloat();

			adjustAndSetYaw(location, yaw);
			adjustAndSetPitch(location, pitch);
		}else{
			switch(args.next()){
			case "yaw":
				if(!args.hasNextFloat()){
					sender.warn(": Syntax error > /direction yaw [yaw]");
					return;
				}

				float yaw = args.nextFloat();

				adjustAndSetYaw(location, yaw);
				break;
			case "pitch":
				if(!args.hasNextFloat()){
					sender.warn(": Syntax error > /direction pitch [pitch]");
					return;
				}

				float pitch = args.nextFloat();

				adjustAndSetPitch(location, pitch);
				break;
			default:
				sender.warn(": Syntax error > /direction [yaw] [pitch] | /set yaw [yaw] | /set pitch [pitch]");
				return;
			}
		}

		//yawとpitchを適用する
		player.teleport(location, TeleportCause.COMMAND);

		//表示例: Set your direction @ 75.2 / 45.0
		sender.info(StringTemplate.capply("Set your direction &7-@ &b-$0 &7-/ &b-$1", location.getYaw(), location.getPitch()));
	}

	private void adjustAndSetYaw(Location location, float yaw){
		location.setYaw(Math.max(Math.min(yaw, 179.9f), -180.0f));
	}

	private void adjustAndSetPitch(Location location, float pitch){
		location.setPitch(Math.max(Math.min(pitch, 90.0f), -90.0f));
	}

}
