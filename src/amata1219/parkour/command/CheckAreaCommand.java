package amata1219.parkour.command;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.selection.RegionSelections;

public class CheckAreaCommand implements Command {

	private final Parkours parkourSet = Parkours.getInstance();
	private final RegionSelections selections = RegionSelections.getInstance();

	/*
	 * checkarea [add/list], checkarea [set/remove] [number], checkarea color [RGB(000,000,000)]
	 */

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//プレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		Player player = sender.asPlayerCommandSender();
		UUID uuid = player.getUniqueId();

		//第1引数で分岐する
		switch(args.next()){
		case "add":{
			if(!selections.hasSelection(uuid)){
				sender.warn("チェックエリアに設定する範囲を指定して下さい。");
				return;
			}

			RegionSelection selection = selections.getSelection(uuid);
			String parkourName = selections.getSelectedParkourName(uuid);

			Yaml yaml = parkourSet.makeYaml(parkourName);

			List<String> checkAreas = yaml.getStringList("Check areas");
			checkAreas.add(selection.toString());

			yaml.set("Check areas", checkAreas);

			yaml.save();

			sender.info(StringTemplate.capply("$0-&r-&b-にチェックエリアを追加しました。", parkourName));
			return;
		}case "list":{
			String parkourName = selections.hasSelection(uuid) ? selections.getSelectedParkourName(uuid) : args.next();

			//アスレが存在しなければ戻る
			if(!parkourSet.existsFile(parkourName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", parkourName));
				return;
			}

			Yaml yaml = parkourSet.makeYaml(parkourName);

			List<String> checkAreas = yaml.getStringList("Check areas");

			//チェックエリアが無ければ戻る
			if(checkAreas.isEmpty()){
				sender.warn(StringTemplate.capply("$0-&r-&c-のチェックエリアはまだありません。", parkourName));
				return;
			}

			for(int checkAreaNumber = 0; checkAreaNumber < checkAreas.size(); checkAreaNumber++)
				sender.info(StringTemplate.capply("&7-: &b-$0 &7-@ &b-$1", checkAreaNumber, checkAreas.get(checkAreaNumber)));
			return;
		}case "set":{
			if(!args.hasNextInt()){
				sender.warn("書き換えるチェックエリアの番号を指定して下さい。");
				return;
			}

			int checkAreaNumber = args.nextInt();

			if(checkAreaNumber <= 0){
				sender.warn("チェックエリア番号は1以上で指定して下さい。");
				return;
			}

			if(!selections.hasSelection(uuid)){
				sender.warn("チェックエリアに設定する範囲を指定して下さい。");
				return;
			}

			RegionSelection selection = selections.getSelection(uuid);
			String parkourName = selections.getSelectedParkourName(uuid);

			Yaml yaml = parkourSet.makeYaml(parkourName);

			List<String> checkAreas = yaml.getStringList("Check areas");

			if(checkAreaNumber <= checkAreas.size()){
				sender.warn("指定されたチェックエリア番号は大きすぎます。");
				return;
			}

			checkAreas.set(checkAreaNumber - 1, selection.toString());

			yaml.set("Check areas", checkAreas);

			yaml.save();

			sender.info(StringTemplate.capply("$0-&r-&b-のチェックエリア$1を書き換えました。", parkourName, checkAreaNumber));
			return;
		}case "remove":{
			String parkourName = selections.hasSelection(uuid) ? selections.getSelectedParkourName(uuid) : args.next();

			if(!args.hasNextInt()){
				sender.warn("削除するチェックエリアの番号を指定して下さい。");
				return;
			}

			int checkAreaNumber = args.nextInt();

			if(checkAreaNumber <= 0){
				sender.warn("チェックエリア番号は1以上で指定して下さい。");
				return;
			}

			Yaml yaml = parkourSet.makeYaml(parkourName);

			List<String> checkAreas = yaml.getStringList("Check areas");

			if(checkAreaNumber <= checkAreas.size()){
				sender.warn("指定されたチェックエリア番号は大きすぎます。");
				return;
			}

			checkAreas.remove(checkAreaNumber - 1);

			yaml.set("Check areas", checkAreas);

			yaml.save();

			sender.info(StringTemplate.capply("$0-&r-&b-のチェックエリア$1を削除しました。", parkourName, checkAreaNumber));
			return;
		}case "color":{
			String parkourName = selections.hasSelection(uuid) ? selections.getSelectedParkourName(uuid) : args.next();

			String text = args.next();

			if(text.isEmpty() || !text.contains(",")){
				sender.warn("パーティクルカラーはR,G,Bの形で指定して下さい。");
				return;
			}

			int[] color = StringSplit.splitToIntArguments(text);

			if(color.length != 3){
				sender.warn("パーティクルカラーはR,G,Bの形で指定して下さい。");
				return;
			}

			Yaml yaml = parkourSet.makeYaml(parkourName);

			yaml.set("Particle color", StringTemplate.apply("$0,$1,$2", adjustRGBValue(color[0]), adjustRGBValue(color[1]), adjustRGBValue(color[2])));

			yaml.save();

			sender.info(StringTemplate.capply("$0-&r-&b-のパーティクルカラーを書き換えました。", parkourName));
			return;
		}default:
			sender.warn("範囲指定した状態で、/checkarea [add/list] | /checkarea [set/remove] [check_area_number] | /checkarea color [R,G,B]");
			sender.warn("または、/checkarea list [parkour_name] | /checkarea [set/remove] [parkour_name] [check_area_number] | /checkarea color [parkour_name] [R,G,B]");
			return;
		}
	}

	private int adjustRGBValue(int value){
		return Math.max(Math.min(value, 255), 0);
	}

}
