package amata1219.parkour.command;

import java.io.File;
import java.util.Optional;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.parkour.ParkourSet;
import net.md_5.bungee.api.ChatColor;

public class ParkourCommand implements Command {

	private final ParkourSet parkourSet = ParkourSet.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//送信者がプレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		//第1引数で分岐する
		switch(args.next()){
		case "create":{
			//第2引数をアスレ名として取得する
			String parkourName = color(args.next());

			//対応したファイルが存在していれば戻る
			if(parkourSet.existsFile(parkourName)){
				sender.warn(StringTemplate.applyWithColor("$0-&r-&c-は既に存在しています。", parkourName));
				return;
			}

			//ファイルを作成する
			parkourSet.makeYaml(parkourName);

			sender.info(StringTemplate.applyWithColor("$0-&r-&b-を作成しました。", parkourName));
			return;
		}case "delete":{
			//第2引数をアスレ名として取得する
			String parkourName = color(args.next());

			//対応したファイルが存在していれば戻る
			if(!parkourSet.existsFile(parkourName)){
				sender.warn(StringTemplate.applyWithColor("$0-&r-&c-は既に存在しています。", parkourName));
				return;
			}

			//アスレが登録されていれば登録を解除する
			parkourSet.unregisterParkour(parkourName);

			//ファイルを削除する
			parkourSet.makeYaml(parkourName).file.delete();

			sender.info(StringTemplate.applyWithColor("$0-&r-&b-を削除しました。", parkourName));
			return;
		}case "register":{
			//第2引数をアスレ名として取得する
			String parkourName = color(args.next());

			//既に登録されていれば戻る
			if(parkourSet.containsParkour(parkourName)){
				sender.warn(StringTemplate.applyWithColor("$0-&r-&c-は既に登録されています。", parkourName));
				return;
			}

			//アスレを登録する
			parkourSet.registerParkour(parkourName);

			sender.info(StringTemplate.applyWithColor("$0-&r-&b-を登録しました。", parkourName));
			return;
		}case "unregister":{
			//第2引数をアスレ名として取得する
			String parkourName = color(args.next());

			//登録されていなければ戻る
			if(!parkourSet.containsParkour(parkourName)){
				sender.warn(StringTemplate.applyWithColor("$0-&r-&c-は登録されていません。", parkourName));
				return;
			}

			//アスレの登録を解除する
			parkourSet.unregisterParkour(parkourName);

			sender.info(StringTemplate.applyWithColor("$0-&r-&b-の登録を解除しました。", parkourName));
			return;
		}case "list":{
			//登録されている全アスレ名を表示する
			for(File file : Optional.ofNullable(parkourSet.folder.listFiles()).orElse(new File[0])){
				//拡張子を除いたファイル名をアスレ名として取得する
				String parkourName = file.getName().replace(".yml", "");

				sender.info(StringTemplate.apply("&7-: &b-$0", parkourName));
			}
			return;
		}default:
			sender.warn("/parkour [create/delete/register/unregister] [parkour_name] | /parkour list");
			return;
		}

	}

	private String color(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}
