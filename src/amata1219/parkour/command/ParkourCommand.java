package amata1219.parkour.command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.text.StringSplit;
import amata1219.amalib.text.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;

public class ParkourCommand implements Command {

	private final Main plugin = Main.getPlugin();
	private final ParkourSet parkourSet = Main.getParkourSet();
	private final File folder = new File(plugin.getDataFolder() + File.separator + "ParkourList");

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//第1引数をアスレ名として取得する
		String parkourName = args.next();

		//第2引数で分岐する
		switch(args.next()){
		case "create":{
			File file = new File(folder, StringTemplate.format("$0.yml", parkourName));

			//アスレの設定ファイルが存在していればエラーとする
			if(file.exists()){
				sender.warn(StringTemplate.format(": Value error > [$0].ymlは既に存在しています。", parkourName));
				return;
			}

			//アスレ名.ymlを生成する
			new Yaml(plugin, file);

			sender.info(StringTemplate.format(": Success > $0.ymlを作成しました。", parkourName));
			return;
		}case "editfile":{
			//アスレが登録されていればエラーとする
			if(parkourSet.isParkourRegistered(parkourName)){
				sender.warn(StringTemplate.format(": Value error > [$0]の登録を解除して下さい(/parkour $0 unregister)。", parkourName));
				return;
			}

			File file = new File(folder, StringTemplate.format("$0.yml", parkourName));

			//アスレの設定ファイルが存在しなければエラーとする
			if(!file.exists()){
				sender.warn(StringTemplate.format(": Value error > [$0].ymlが存在しません。", parkourName));
				return;
			}

			/*
			 * 'World': "world"
			'Region': "0,0,0,0,0,0"
			'Color': "0,0,0"
			'Start line': "0,0,0,0,0,0"
			'Finish line': "0,0,0,0,0,0"
			'Check areas': []
			 */

			Yaml yaml = new Yaml(plugin, file);

			//第3引数で分岐する
			switch(args.next()){
			case "world":{
				//第4引数をワールド名として取得する
				String worldName = args.next();

				World world = Bukkit.getWorld(worldName);
				if(world == null){
					sender.warn(StringTemplate.format(": Value error > [$0]は存在しません。", worldName));
					return;
				}

				//ワールドを書き換える
				yaml.set("World", worldName);

				yaml.save();
				return;
			}case "pos1":{
				String[] xyz = new String[3];
				for(int index = 0; index < 3; index++){
					//引数が存在しない又は整数型で表現出来なければエラーとする
					if(!args.hasNextInt()){
						sender.warn(": Syntax error > /parkour $0 editfile pos1 [x] [y] [z]");
						return;
					}

					//各引数を取得する
					String coordinate = args.next();

					//対応したインデックスに値を代入する
					xyz[index] = coordinate;
				}

				//x1,y1,z1,x2,y2,z2の形の文字列を取得する
				String text = yaml.getString("Region");

				//各値に分割する
				String[] coordinates = text.split(",");

				//各値を書き換える
				coordinates[0] = xyz[0];
				coordinates[1] = xyz[1];
				coordinates[2] = xyz[2];

				//領域を書き換える
				yaml.set("Region", String.join(",", coordinates));

				yaml.save();
				return;
			}case "pos2":{
				String[] xyz = new String[3];
				for(int index = 0; index < 3; index++){
					//引数が存在しない又は整数型で表現出来なければエラーとする
					if(!args.hasNextInt()){
						sender.warn(": Syntax error > /parkour $0 editfile pos2 [x] [y] [z]");
						return;
					}

					//各引数を取得する
					String coordinate = args.next();

					//対応したインデックスに値を代入する
					xyz[index] = coordinate;
				}

				//領域を表現する座標を各値に分割する
				String[] coordinates = yaml.getString("Region").split(",");

				//各値を相対座標化して書き換える
				for(int index = 0; index < 3; index++)
					coordinates[index + 3] = String.valueOf(Integer.parseInt(xyz[index]) - Integer.parseInt(coordinates[index]));

				//領域を書き換える
				yaml.set("Region", String.join(",", coordinates));

				yaml.save();
				return;
			}case "startline":{
				String[] xyzxyz = new String[6];
				for(int index = 0; index < 6; index++){
					//引数が存在しない又は整数型で表現出来なければエラーとする
					if(!args.hasNextInt()){
						sender.warn(": Syntax error > /parkour $0 editfile startline [x1] [y1] [z1] [x2] [y2] [z2]");
						return;
					}

					//各引数を取得する
					String coordinate = args.next();

					//対応したインデックスに値を代入する
					xyzxyz[index] = coordinate;
				}

				//領域を表現する座標を各値に分割し整数型に変換する
				int[] coordinates = StringSplit.splitToIntArguments(yaml.getString("Region"));

				//スタートラインを表現する座標を各値に分割する
				String[] startLine = yaml.getString("Start line").split(",");

				//各値を相対座標化して書き換える
				for(int index = 0; index < 6; index++)
					startLine[index] = String.valueOf(Integer.parseInt(xyzxyz[index]) - coordinates[index < 3 ? index : index - 3]);

				yaml.set("Start line", String.join(",", startLine));

				yaml.save();
				return;
			}case "finishline":{
				String[] xyzxyz = new String[6];
				for(int index = 0; index < 6; index++){
					//引数が存在しない又は整数型で表現出来なければエラーとする
					if(!args.hasNextInt()){
						sender.warn(": Syntax error > /parkour $0 editfile startline [x1] [y1] [z1] [x2] [y2] [z2]");
						return;
					}

					//各引数を取得する
					String coordinate = args.next();

					//対応したインデックスに値を代入する
					xyzxyz[index] = coordinate;
				}

				//領域を表現する座標を各値に分割し整数型に変換する
				int[] coordinates = StringSplit.splitToIntArguments(yaml.getString("Region"));

				//スタートラインを表現する座標を各値に分割する
				String[] finishLine = yaml.getString("Finish line").split(",");

				//各値を相対座標化して書き換える
				for(int index = 0; index < 6; index++)
					finishLine[index] = String.valueOf(Integer.parseInt(xyzxyz[index]) - coordinates[index < 3 ? index : index - 3]);

				yaml.set("Finish line", String.join(",", finishLine));

				yaml.save();
				return;
			}default:
				sender.warn(StringTemplate.format(": Syntax error > /parkour $0 editfile (world|pos1|pos2|startline|finishline) [...values]", parkourName));
				return;
			}
		}case "register":{
			File file = new File(folder, StringTemplate.format("$0.yml", parkourName));

			//アスレの設定ファイルが存在しなければエラーとする
			if(!file.exists()){
				sender.warn(": Value error > [parkour_name] is invalid");
				return;
			}

			//アスレ名.ymlを生成する
			Yaml yaml = new Yaml(plugin, file);
			Parkour parkour = new Parkour(yaml);

			//アスレを登録する
			parkourSet.registerParkour(parkour);

			sender.info(StringTemplate.format("[$0]を登録しました。", parkourName));
			return;
		}case "unregister":{
			//アスレが登録されていなければエラーとする
			if(!parkourSet.isParkourRegistered(parkourName)){
				sender.warn(": Value error > [parkour_name] has not been registered");
				return;
			}

			//指定されたアスレの登録を解除する
			parkourSet.parkourMap.remove(parkourName);

			sender.info(StringTemplate.format("[$0]の登録を解除しました。", parkourName));
			return;
		}default:
			sender.warn(": Syntax error > /parkour [name] (create|edit)");
			return;
		}
	}

}
