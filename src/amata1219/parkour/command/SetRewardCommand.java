package amata1219.parkour.command;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.ParkourSet;

public class SetRewardCommand implements Command {

	private final ParkourSet parkourSet = ParkourSet.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		String parkourName = args.next();

		if(!parkourSet.existsFile(parkourName)){
			sender.warn(StringTemplate.applyWithColor("$0-&r-&c-は存在しません。", parkourName));
			return;
		}

		String rewards = args.next();

		if(!rewards.contains(",")){
			sender.warn("/setreward [parkour_name] [first/second_and_subsequent]");
			return;
		}

		int[] coins;

		try{
			coins = StringSplit.splitToIntArguments(rewards);
		}catch(Exception e){
			sender.warn("/setreward [parkour_name] [first/second_and_subsequent]");
			return;
		}

		Yaml yaml = parkourSet.makeYaml(parkourName);

		yaml.set("Reward coins", StringTemplate.apply("$0,$1", coins[0], coins[1]));

		yaml.save();

		sender.info(StringTemplate.applyWithColor("$0-&r-&b-の報酬を書き換えました。", parkourName));
		return;
	}

}
