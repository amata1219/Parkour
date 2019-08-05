package amata1219.parkour.command;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.StageSet;
import net.md_5.bungee.api.ChatColor;

public class StageCommand implements Command {

	private final StageSet stages = StageSet.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//第1引数で分岐する
		switch(args.next()){
		case "create":
			//第2引数をステージ名として取得する
			String stageName = color(args.next());

			//コンフィグを作成する
			Yaml yaml = stages.makeYaml(stageName);

			Stage stage = new Stage(yaml);
			return;
		case "delete":

			return;
		case "list":

			return;
		default:
			sender.warn("/stage [create/delete] [stage_name] | /stage list | /stage [addp/removep] [parkour_name]");
			return;
		}
	}

	private String color(String text){
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}
