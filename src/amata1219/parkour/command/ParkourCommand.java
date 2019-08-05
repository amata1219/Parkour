package amata1219.parkour.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.message.MessageTemplate;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;

public class ParkourCommand implements Command {

	private final ParkourSet parkourSet = ParkourSet.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//送信者がプレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		Player player = sender.asPlayerCommandSender();

		//第1引数をアスレ名として取得する
		String parkourName = ChatColor.translateAlternateColorCodes('&', args.next());

		//リストという名前であればアスレリストを表示する
		if(parkourName.equals("list")){
			for(Parkour parkour : parkourSet.parkourMap.values()) MessageTemplate.apply("&7-: &b-$0", parkour.name).display(player);

			return;
		}

		switch(args.next()){
		case "create":{
			if(parkourSet.containsParkour(parkourName) || parkourSet.unfinishedParkourNames.contains(parkourName)){
				sender.warn("指定されたアスレは既に存在しています。");
				return;
			}

			//ファイルを作成する
			parkourSet.getYaml(parkourName);

			//製作途中のアスレマップに追加する
			parkourSet.unfinishedParkourNames.add(parkourName);

			sender.info(StringTemplate.apply("指定された名前でアスレを作成しました。各情報を設定次第、/parkour register $0 を実行して登録して下さい。", parkourName));
			return;
		}case "delete":{
			if(parkourSet.containsParkour(parkourName)){
				//アスレの登録を解除する
				parkourSet.unregisterParkour(parkourName);

				//ファイルを削除する
				parkourSet.getYaml(parkourName).file.delete();

				sender.info("指定されたアスレを削除しました。");
				return;
			}else if(parkourSet.unfinishedParkourNames.contains(parkourName)){
				//製作途中のアスレマップから削除する
				parkourSet.unfinishedParkourNames.remove(parkourName);

				//ファイルを削除する
				parkourSet.getYaml(parkourName).file.delete();

				sender.info("指定されたアスレを削除しました。");
				return;
			}else{
				sender.warn("指定されたアスレは存在しません。");
				return;
			}
		}case "register":{
			if(parkourSet.containsParkour(parkourName)){
				sender.warn("指定されたアスレは既に登録されています。");
				return;
			}

			Yaml yaml = parkourSet.getYaml(parkourName);

			Parkour parkour = new Parkour(yaml);

			parkourSet.registerParkour(parkour);

			sender.info("指定されたアスレを登録しました。");
			return;
		}case "unregister":{
			if(!parkourSet.containsParkour(parkourName)){
				sender.warn("指定されたアスレは登録されていません。");
				return;
			}

			//アスレの登録を解除する
			parkourSet.unregisterParkour(parkourName);

			sender.info("指定されたアスレの登録を解除しました。");
			return;
		}case "unfinished":{
			for(String unfinishedParkourName : parkourSet.unfinishedParkourNames)
				sender.info(": " + unfinishedParkourName);
			return;
		}default:
			return;
		}
	}

}
