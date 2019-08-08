package amata1219.parkour.command;

import org.bukkit.Location;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.Stages;
import net.md_5.bungee.api.ChatColor;

public class StageCommand implements Command {

	private final Stages stages = Stages.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args){
		if(!blockNonPlayer(sender)) return;

		//第1引数が無ければ戻る
		if(!args.hasNext()){
			sender.warn("/stage");
			return;
		}

		String stageName = color(args.next());

		//ステージリストを表示する場合
		if(stageName.equals("list")){
			stages.getStages().stream().map(stage -> stage.name).map(name -> StringTemplate.capply("&7-: &b-$0", name)).forEach(sender::info);
			return;
		}

		switch(args.next()){
		case "create":{
			//同名のステージが既に存在していれば戻る
			if(stages.containsStage(stageName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は既に存在しています。", stageName));
				return;
			}

			//新たにコンフィグを作成する
			Yaml yaml = stages.makeYaml(stageName);

			//コンフィグに基づきステージを生成する
			Stage stage = new Stage(yaml);
			stages.registerStage(stage);

			sender.info(StringTemplate.capply("$0-&r-&b-を作成しました。", stageName));
			return;
		}case "delete":{
			//指定されたステージが存在しなければ戻る
			if(blockNotExistStage(sender, stageName)) return;

			stages.unregisterStage(stageName);

			//このステージのコンフィグを取得し削除する
			stages.makeYaml(stageName).file.delete();

			sender.info(StringTemplate.apply("$0-&r-&b-を削除しました。", stageName));
			return;
		}case "setspawn":{
			//指定されたステージが存在しなければ戻る
			if(!stages.containsStage(stageName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", stageName));
				return;
			}

			Stage stage = stages.getStage(stageName);

			//コマンドを送信したプレイヤーの座標を取得する
			Location spawnPoint = sender.asPlayerCommandSender().getLocation();

			//スポーン地点を設定する
			stage.setSpawnLocation(spawnPoint);

			sender.info(StringTemplate.capply("$0-&r-&b-のスポーン地点を現在地点に書き換えました。", stageName));
			return;
		}case "addparkour":{
			//ステージが存在していなければ戻る
			if(blockNotExistStage(sender, stageName)) return;

			//第3引数をアスレ名として取得する
			String parkourName = color(args.next());

			//アスレが存在していなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			//ステージにアスレを追加する
			stages.addParkour(stageName, parkourName);

			sender.info(StringTemplate.apply("$0-&r-&b-に$1-&r-&b-を追加しました。", stageName, parkourName));
			return;
		}case "removeparkour":{
			//ステージが存在していなければ戻る
			if(blockNotExistStage(sender, stageName)) return;

			//第3引数をアスレ名として取得する
			String parkourName = color(args.next());

			//アスレが存在していなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			//ステージからアスレを削除する
			stages.removeParkour(parkourName);

			sender.info(StringTemplate.apply("$0-&r-&b-から$1-&r-&b-を削除しました。", stageName, parkourName));
			return;
		}case "parkourlist":{
			//ステージが存在していなければ戻る
			if(blockNotExistStage(sender, stageName)) return;

			//ステージを取得する
			Stage stage = stages.getStage(stageName);

			stage.parkourNames.stream().map(parkourName -> StringTemplate.capply("&7-: &b-$0", parkourName)).forEach(sender::info);
			return;
		}default:
			sender.warn("/stage list | /stage [stage_name] [create/delete/addparkour/removeparkour] [parkour_name] | /stage [stage_name] [parkourlist]");
			return;
		}
	}

	private String color(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	private boolean blockNotExistStage(Sender sender, String stageName){
		if(stages.containsStage(stageName)) return false;

		sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", stageName));
		return true;
	}

	private boolean blockNotExistParkour(Sender sender, String parkourName){
		if(stages.containsStage(parkourName)) return false;

		sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", parkourName));
		return true;
	}

}
