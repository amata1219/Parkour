package amata1219.parkour.command;

import java.util.Map;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.parkour.Main;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.StageSet;

public class StageCommand implements Command {

	private final StageSet stageSet = Main.getStageSet();
	private final Map<String, Stage> stages = stageSet.stages;

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
		String name = args.next();

		if(name.equalsIgnoreCase("list")){

		}else if(stages.containsKey(name)){

		}else{
			sender.warn(": Syntax error > /stage [name] (create|add|remove|list) | /stage list");
			return;
		}
	}

}
