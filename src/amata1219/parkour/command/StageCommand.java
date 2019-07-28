package amata1219.parkour.command;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.stage.Stage;

public class StageCommand implements Command {

	private final Main plugin = Main.getPlugin();
	private final File folder = new File(plugin.getDataFolder() + File.separator + "Stages");
	private final Map<String, Stage> stages = Main.getStageSet().stages;
	private final Map<String, Stage> parkourNamesToStagesMap = Main.getStageSet().parkourNamesToStagesMap;
	private final Map<String, Parkour> parkourMap = Main.getParkourSet().parkourMap;

	@Override
	public void onCommand(Sender sender, Arguments args) {
		/*
		 * stage [name]
		 *
		 * create
		 * addparkour [parkour]
		 * removeparkour [parkour]
		 * listparkour
		 *
		 * add
		 * remove
		 * list
		 *
		 */
		//第1引数を取得する
		String stageName = args.next();

		//ステージ名とは関係無い機能の処理をする
		switch(stageName){
		case "help":
		case "commands":
		case "usage":
		case "howtouse":
			sender.warn(": Not implemented error > 実装されていません。");
			return;
		case "list":

			return;
		default:
			break;
		}

		//第2引数で分岐する
		switch(args.next()){
		case "create":{
			File file = new File(folder, StringTemplate.format("$0.yml", stageName));

			//既にファイルが存在していればエラーとする
			if(file.exists()){
				sender.warn(StringTemplate.format(": Value error > [$0].ymlは既に存在しています。", stageName));
				return;
			}

			//ステージ名.ymlを作成する
			new Yaml(plugin, file);

			sender.info(StringTemplate.format(": Success > $0.ymlを作成しました。", stageName));
			return;
		}case "addparkour":{
			//ステージが存在しなければエラーとする
			if(!stages.containsKey(stageName)){
				sender.warn(StringTemplate.format(": Value error > [$0]は存在しません。", stageName));
				return;
			}

			//第3引数をアスレ名として取得する
			String parkourName = args.next();

			//アスレが存在しなければエラーとする
			if(!parkourMap.containsKey(parkourName)){
				sender.warn(StringTemplate.format(": Value error > [$0]は存在しません。", parkourName));
				return;
			}

			//ステージを取得する
			Stage stage = stages.get(stageName);

			//ステージ内のアスレのリストを取得する
			List<Parkour> parkourList = stage.parkourList;

			//アスレを取得する
			Parkour parkour = parkourMap.get(parkourName);

			//既にアスレが追加されていたらエラーとする
			if(parkourList.contains(parkour)){
				sender.warn(StringTemplate.format(": Value error > [$0]に[$1]は既に追加されています。", stageName, parkourName));
				return;
			}

			parkourList.add(parkour);

			sender.info(StringTemplate.format(": Success > [$0]に[$1]を追加しました。", stageName, parkourName));
			return;
		}case "removeparkour":{
			//ステージが存在しなければエラーとする
			if(!stages.containsKey(stageName)){
				sender.warn(StringTemplate.format(": Value error > [$0]は存在しません。", stageName));
				return;
			}

			//第3引数をアスレ名として取得する
			String parkourName = args.next();

			//アスレが存在しなければエラーとする
			if(!parkourMap.containsKey(parkourName)){
				sender.warn(StringTemplate.format(": Value error > [$0]は存在しません。", parkourName));
				return;
			}

			//ステージを取得する
			Stage stage = stages.get(stageName);

			//ステージ内のアスレのリストを取得する
			List<Parkour> parkourList = stage.parkourList;

			//アスレを取得する
			Parkour parkour = parkourMap.get(parkourName);

			//アスレが追加されていなければエラーとする
			if(!parkourList.contains(parkour)){
				sender.warn(StringTemplate.format(": Value error > [$0]に[$1]は追加されていません。", stageName, parkourName));
				return;
			}

			parkourList.remove(parkour);

			sender.info(StringTemplate.format(": Success > [$0]から[$1]を削除しました。", stageName, parkourName));
			return;
		}case "parkourlist":{
			//ステージが存在しなければエラーとする
			if(!stages.containsKey(stageName)){
				sender.warn(StringTemplate.format(": Value error > [$0]は存在しません。", stageName));
				return;
			}

			//ステージを取得する
			Stage stage = stages.get(stageName);

			//ステージ内のアスレのリストを取得する
			List<Parkour> parkourList = stage.parkourList;

			if(parkourList.isEmpty()){
				sender.warn(StringTemplate.format(": Not exists error > [$0]にアスレは追加されていません。", stageName));
				return;
			}

			sender.info(StringTemplate.format(": Information > [$0]に追加されているアスレの一覧です。", stageName));

			//各アスレ名を表示する
			stage.parkourList.forEach(parkour -> sender.message(StringTemplate.format("$0: $1", ChatColor.GRAY, parkour.name)));
			return;
		}default:
			sender.warn(": Syntax error > /stage [name] (create|addparkour|removeparkour|parkourlist)");
			return;
		}
	}

}
