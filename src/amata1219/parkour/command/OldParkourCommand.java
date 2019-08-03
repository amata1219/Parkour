package amata1219.parkour.command;

import java.io.File;
import java.util.Map;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.message.MessageTemplate;
import amata1219.amalib.string.StringJoin;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.stage.Stage;

public class OldParkourCommand implements Command {

	private final Main plugin = Main.getPlugin();
	private final File folder = new File(plugin.getDataFolder() + File.separator + "ParkourList");
	private final Map<String, Parkour> parkourMap = Main.getParkourSet().parkourMap;
	private final Map<String, Stage> stages = Main.getStageSet().stages;

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//第1引数をアスレ名として取得する
		String parkourName = args.next();

		//アスレ名とは関係無い機能の処理をする
		switch(parkourName){
		case "help":
		case "commands":
		case "usage":
		case "howtouse":
			sender.warn(": Not implemented error > 実装されていません。");
			return;
		default:
			break;
		}

		//第2引数で分岐する
		switch(args.next()){
		case "create":{
			File file = new File(folder, MessageTemplate.apply("$0.yml", parkourName));

			//アスレの設定ファイルが存在していればエラーとする
			if(file.exists()){
				sender.warn(MessageTemplate.apply(": Value error > [$0].ymlは既に存在しています。", parkourName));
				return;
			}

			//アスレ名.ymlを生成する
			new Yaml(plugin, file);

			sender.info(MessageTemplate.apply(": Success > $0.ymlを作成しました。", parkourName));
			sender.info(MessageTemplate.apply(": Next > [$0]の設定を編集し登録して下さい。", parkourName));
			return;
		}case "delete":{
			//アスレが登録されていればエラーとする
			if(!parkourMap.containsKey(parkourName)){
				sender.warn(MessageTemplate.apply(": Value error > [$0]の登録を解除して下さい。", parkourName));
				return;
			}

			new File(folder, MessageTemplate.apply("$0.yml", parkourName)).delete();

			sender.info(MessageTemplate.apply(": Success > $0.ymlを削除しました。", parkourName));
			return;
		}case "editfile":{
			//アスレが登録されていればエラーとする
			if(parkourMap.containsKey(parkourName)){
				sender.warn(MessageTemplate.apply(": Value error > [$0]の登録を解除して下さい(/parkour $0 unregister)。", parkourName));
				return;
			}

			File file = new File(folder, MessageTemplate.apply("$0.yml", parkourName));

			//アスレの設定ファイルが存在しなければエラーとする
			if(!file.exists()){
				sender.warn(MessageTemplate.apply(": Value error > [$0].ymlが存在しません。", parkourName));
				return;
			}

			Yaml yaml = new Yaml(plugin, file);

			//第3引数で分岐する
			switch(args.next()){
			/*case "world":{
				//第4引数をワールド名として取得する
				String worldName = args.next();

				World world = Bukkit.getWorld(worldName);

				//ワールドが存在しなければエラーとする
				if(world == null){
					sender.warn(StringTemplate.format(": Value error > [$0]は存在しません。", worldName));
					return;
				}

				//ワールドを書き換える
				yaml.set("World", worldName);

				yaml.save();

				sender.info(StringTemplate.format(": Success > [$0]のWorldを[$1]に設定しました。", parkourName, worldName));
				return;
			}case "pos1":{
				int[] xyz = new int[3];
				for(int index = 0; index < 3; index++){
					//引数が存在しない又は整数型で表現出来なければエラーとする
					if(!args.hasNextInt()){
						sender.warn(": Syntax error > /parkour $0 editfile pos1 [x] [y] [z]");
						return;
					}

					//対応したインデックスに値を代入する
					xyz[index] = args.nextInt();
				}

				//領域を表現する座標を各値に分割する
				int[] coordinates = StringSplit.splitToIntArguments(yaml.getString("Region"));

				for(int index = 0; index < 3; index++){
					//pos2の値を絶対座標化する
					int coordinate2 = coordinates[index + 3] += coordinates[index];

					//pos1の値を書き換える
					int coordinate1 = coordinates[index] = xyz[index];

					//pos1の値がpos2の値より大きければ交換する
					if(coordinate1 > coordinate2){
						coordinates[index] = coordinate2;
						coordinates[index + 3] = coordinate1;
					}

					//pos2の値を相対座標化する
					coordinates[index + 3] -= coordinates[index];
				}

				//各値をカンマ区切りで結合する
				String result = StringJoin.join(coordinates, ",");

				//領域を書き換える
				yaml.set("Region", result);

				yaml.save();

				sender.info(StringTemplate.format(": Success > [$0]の領域を[$1]に設定しました。", parkourName, result));
				return;
			}case "pos2":{
				int[] xyz = new int[3];
				for(int index = 0; index < 3; index++){
					//引数が存在しない又は整数型で表現出来なければエラーとする
					if(!args.hasNextInt()){
						sender.warn(": Syntax error > /parkour $0 editfile pos2 [x] [y] [z]");
						return;
					}

					//対応したインデックスに値を代入する
					xyz[index] = args.nextInt();
				}

				//領域を表現する座標を各値に分割する
				int[] coordinates = StringSplit.splitToIntArguments(yaml.getString("Region"));

				//各値を相対座標化して書き換える
				for(int index = 0; index < 3; index++){
					int coordinate1 = coordinates[index];
					int coordinate2 = xyz[index];

					//pos1の値がpos2の値より大きければ交換する
					if(coordinate1 > coordinate2){
						coordinates[index] = coordinate2;
						coordinates[index + 3] = coordinate1;
					}

					//pos2の値を相対座標化して書き換える
					coordinates[index + 3] -= coordinates[index];
				}

				//各値をカンマ区切りで結合する
				String result = StringJoin.join(coordinates, ",");

				//領域を書き換える
				yaml.set("Region", result);

				yaml.save();

				sender.info(StringTemplate.format(": Success > [$0]の領域を[$1]に設定しました。", parkourName, result));
				return;
			}*/case "region":{

			}case "startline":{
				int[] xyzxyz = new int[6];
				for(int index = 0; index < 6; index++){
					//引数が存在しない又は整数型で表現出来なければエラーとする
					if(!args.hasNextInt()){
						sender.warn(": Syntax error > /parkour $0 editfile startline [x1] [y1] [z1] [x2] [y2] [z2]");
						return;
					}

					//対応したインデックスに値を代入する
					xyzxyz[index] = args.nextInt();
				}

				//領域を表現する座標を各値に分割し整数型に変換する
				int[] coordinates = StringSplit.splitToIntArguments(yaml.getString("Region"));

				//スタートラインを表現する座標を各値に分割する
				int[] startLine = StringSplit.splitToIntArguments(yaml.getString("Start line"));

				//各値を相対座標化して書き換える
				for(int index = 0; index < 6; index++)
					startLine[index] = xyzxyz[index] - coordinates[index < 3 ? index : index - 3];

				String result = StringJoin.join(startLine, ",");

				yaml.set("Start line", result);

				yaml.save();

				sender.info(MessageTemplate.apply(": Success > [$0]のスタートラインを[$1]に設定しました。", parkourName, result));
				return;
			}case "finishline":{
				int[] xyzxyz = new int[6];
				for(int index = 0; index < 6; index++){
					//引数が存在しない又は整数型で表現出来なければエラーとする
					if(!args.hasNextInt()){
						sender.warn(": Syntax error > /parkour $0 editfile startline [x1] [y1] [z1] [x2] [y2] [z2]");
						return;
					}

					//対応したインデックスに値を代入する
					xyzxyz[index] = args.nextInt();
				}

				//領域を表現する座標を各値に分割し整数型に変換する
				int[] coordinates = StringSplit.splitToIntArguments(yaml.getString("Region"));

				//スタートラインを表現する座標を各値に分割する
				int[] finishLine = StringSplit.splitToIntArguments(yaml.getString("Finish line"));

				//各値を相対座標化して書き換える
				for(int index = 0; index < 6; index++)
					finishLine[index] = xyzxyz[index] - coordinates[index < 3 ? index : index - 3];

				String result = StringJoin.join(finishLine, ",");

				yaml.set("Finish line", result);

				yaml.save();

				sender.info(MessageTemplate.apply(": Success > [$0]のフィニッシュラインを[$1]に設定しました。", parkourName, result));
			}default:
				sender.warn(MessageTemplate.apply(": Syntax error > /parkour $0 editfile (region|startline|finishline)", parkourName));
				return;
			}
		}case "register":{
			File file = new File(folder, MessageTemplate.apply("$0.yml", parkourName));

			//アスレの設定ファイルが存在しなければエラーとする
			if(!file.exists()){
				sender.warn(MessageTemplate.apply(": Value error > $0.ymlが存在しません。", parkourName));
				return;
			}

			//アスレ名.ymlを生成する
			Yaml yaml = new Yaml(plugin, file);
			Parkour parkour = new Parkour(yaml);

			//アスレを登録する
			Main.getParkourSet().registerParkour(parkour);

			//このアスレのステージに追加する
			for(Stage stage : stages.values()){
				if(!stage.parkourNames.contains(parkourName))
					continue;

				stage.parkourList.add(parkour);
				break;
			}

			sender.info(MessageTemplate.apply("[$0]を登録しました。", parkourName));
			return;
		}case "unregister":{
			//アスレが登録されていなければエラーとする
			if(!parkourMap.containsKey(parkourName)){
				sender.warn(MessageTemplate.apply(": Value error > [$0]は登録されていません。", parkourName));
				return;
			}

			//指定されたアスレの登録を解除する
			Parkour parkour = parkourMap.remove(parkourName);

			//このアスレのステージから削除する
			for(Stage stage : stages.values())
				if(stage.parkourList.remove(parkour))
					break;

			sender.info(MessageTemplate.apply("[$0]の登録を解除しました。", parkourName));
			return;
		}default:
			sender.warn(": Syntax error > /parkour [name] (create|editfile|register|unregister)");
			return;
		}
	}

}
