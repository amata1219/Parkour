package amata1219.parkour.command;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.StageSet;

public class StageCommand implements Command {

	private final StageSet stages = StageSet.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {

		String arg0 = args.next();

		if(arg0.equals("list")){
			sender.warn("not implemented.");
			return;
		}

		switch(args.next()){
		case "create":{
			Yaml yaml = stages.makeYaml(arg0);

			Stage stage = new Stage(yaml);

			stages.registerStage(stage);

			sender.info("ステージを作成しました");
			return;
		}case "delete":
			sender.warn("not implemented.");
			break;
		case "addparkour":{
			Stage stage = stages.getStage(arg0);
			stage.parkourNames.add(arg0);
			sender.info("アスレを追加しました。");
			break;
		}case "removeparkour":
			sender.warn("not implemented.");
			break;
		case "parkourlist":
			sender.warn("not implemented.");
			break;
		default:
			return;
		}
	}

}
