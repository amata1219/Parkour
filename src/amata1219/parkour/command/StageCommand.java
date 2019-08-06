package amata1219.parkour.command;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.StageSet;
import net.md_5.bungee.api.ChatColor;

public class StageCommand implements Command {

	private final StageSet stages = StageSet.getInstance();
	private final ParkourSet parkourSet = ParkourSet.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//第1引数で分岐する
		switch(args.next()){
		case "create":{
			//第2引数をステージ名として取得する
			String stageName = color(args.next());

			//既にステージが存在していれば戻る
			if(stages.containsStage(stageName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は既に存在しています。", stageName));
				return;
			}

			//コンフィグを作成する
			Yaml yaml = stages.makeYaml(stageName);

			//コンフィグを基にステージを生成する
			Stage stage = new Stage(yaml);

			//ステージを登録する
			stages.registerStage(stage);

			sender.info(StringTemplate.apply("$0-&r-&b-を作成しました。", stageName));
			return;
		}case "delete":{
			//第2引数をステージ名として取得する
			String stageName = color(args.next());

			//コンフィグが存在しなければ戻る
			if(!stages.existsFile(stageName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", stageName));
				return;
			}

			//ステージの登録を解除する
			stages.unregisterStage(stageName);

			//コンフィグを削除する
			stages.makeYaml(stageName).file.delete();

			sender.info(StringTemplate.apply("$0-&r-&b-を削除しました。", stageName));
			return;
		}case "setspawn":{
			if(blockNonPlayer(sender)) return;

			//第2引数をステージ名として取得する
			String stageName = color(args.next());

			//コンフィグが存在しなければ戻る
			if(!stages.existsFile(stageName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", stageName));
				return;
			}

			Stage stage = stages.getStage(stageName);

			stage.setSpawnLocation(new ImmutableEntityLocation(sender.asPlayerCommandSender().getLocation()));

			sender.info(StringTemplate.capply("$0-&r-&b-のスポーン地点を現在地点に書き換えました。", stageName));
			return;
		}case "list":{
			//第2引数をステージ名として取得する
			String stageName = color(args.next());

			//ステージが存在していなければ戻る
			if(!stages.containsStage(stageName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", stageName));
				return;
			}

			//ステージを取得する
			Stage stage = stages.getStage(stageName);

			//ステージ内のアスレを全て表示する
			for(String parkourName : stage.parkourNames) sender.info(StringTemplate.capply("&7-: &b-$0", parkourName));
			return;
		}case "addp":
		 case "addparkour":{
			//第2引数をステージ名として取得する
			String stageName = color(args.next());

			//ステージが存在していなければ戻る
			if(!stages.containsStage(stageName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", stageName));
				return;
			}

			//第3引数をアスレ名として取得する
			String parkourName = color(args.next());

			//アスレが存在しなければ戻る
			if(!parkourSet.containsParkour(parkourName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", parkourName));
				return;
			}

			//ステージにアスレを追加する
			stages.addParkour(stageName, parkourName);

			sender.info(StringTemplate.apply("$0-&r-&b-に$1-&r-&b-を追加しました。", stageName, parkourName));
			return;
		 }case "removep":
		  case "removeparkour":{
			//第2引数をステージ名として取得する
			String stageName = color(args.next());

			//ステージが存在していなければ戻る
			if(!stages.containsStage(stageName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", stageName));
				return;
			}

			//第3引数をアスレ名として取得する
			String parkourName = color(args.next());

			//アスレが存在しなければ戻る
			if(!parkourSet.containsParkour(parkourName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", parkourName));
				return;
			}

			//ステージからアスレを削除する
			stages.removeParkour(parkourName);

			sender.info(StringTemplate.apply("$0-&r-&b-から$1-&r-&b-を削除しました。", stageName, parkourName));
			return;
		}default:
			sender.warn("/stage [create/delete] [stage_name] | /stage list | /stage [addp/removep] [stage_name] [parkour_name]");
			return;
		}
	}

	private String color(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}
