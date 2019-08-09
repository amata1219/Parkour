package amata1219.parkour.command;

import java.util.regex.Pattern;

import org.bukkit.Location;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.util.Color;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.parkour.Rewards;
import amata1219.parkour.stage.Stages;
import net.md_5.bungee.api.ChatColor;

public class ParkourCommand implements Command {

	private static final Pattern RGB_FORMAT = Pattern.compile("^((2[0-4]\\d|25[0-5]|1\\d{1,2}|[1-9]\\d|\\d)( ?, ?)){2}(2[0-4]\\d|25[0-5]|1\\d{1,2}|[1-9]\\d|\\d)");
	private static final Pattern REWARDS_FORMAT = Pattern.compile("[0-9]{1,8},[0-9]{1,8}");

	private final Parkours parkours = Parkours.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//送信者がプレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		//第1引数が無ければ戻る
		if(!args.hasNext()){
			sender.warn("/parkour [parkour_name] [create/delete/enable/disable/spawn] | /parkour [parkour_name] color [R,G,B] | /parkour [parkour_name] rewards [coin,coin] | /parkour list");
			return;
		}

		//第1引数をアスレ名として取得する
		String parkourName = ChatColor.translateAlternateColorCodes('&', args.next());

		//第1引数がlistであればアスレ名を全て表示する
		if(parkourName.equals("list")){
			parkours.getParkours().stream()
			.map(parkour -> StringTemplate.capply("&7-: $0($1)", parkour.name, (parkour.enable ? "&b-有効" : "&7-無効")))
			.forEach(sender::message);
			return;
		}

		//第2引数で分岐する
		switch(args.next()){
		case "create":{
			//対応したファイルが存在していれば戻る
			if(parkours.existsFile(parkourName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は既に存在しています。", parkourName));
				return;
			}

			//ファイルを作成する
			parkours.makeYaml(parkourName);

			//無効化された状態で登録する
			parkours.registerParkour(parkourName);

			sender.info(StringTemplate.capply("$0-&r-&b-を作成しました。", parkourName));
			return;
		}case "delete":{
			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			//アスレが登録されていれば登録を解除する
			parkours.unregisterParkour(parkourName);

			//ステージからアスレを削除する
			Stages.getInstance().removeParkour(parkourName);

			//ファイルを削除する
			parkours.makeYaml(parkourName).file.delete();

			sender.info(StringTemplate.capply("$0-&r-&b-を削除しました。", parkourName));
			return;
		}case "enable":{
			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			Parkour parkour = parkours.getParkour(parkourName);

			if(parkour.enable){
				sender.warn(StringTemplate.capply("$0-&r-&c-は既に有効化されています。", parkourName));
				return;
			}

			//アスレを有効化する
			parkour.apply(it -> it.enable = true);

			sender.info(StringTemplate.capply("$0-&r-&b-を有効化しました。", parkourName));
			return;
		}case "disable":{
			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			Parkour parkour = parkours.getParkour(parkourName);

			if(!parkour.enable){
				sender.warn(StringTemplate.capply("$0-&r-&c-は既に無効化されています。", parkourName));
				return;
			}

			//アスレを無効化する
			parkour.apply(it -> it.enable = false);

			sender.info(StringTemplate.capply("$0-&r-&b-を無効化しました。", parkourName));
			return;
		}case "spawn":{
			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			Parkour parkour = parkours.getParkour(parkourName);

			//プレイヤーの座標を取得する
			Location location = sender.asPlayerCommandSender().getLocation();

			//イミュータブルな座標にしブロックの中央に調整した上でセットする
			parkour.apply(it -> it.spawnPoint = new ImmutableEntityLocation(location).middle());

			sender.info(StringTemplate.capply("$0-&r-&b-のスポーン地点を現在地点に書き換えました。", parkourName));
			return;
		}case "color":{
			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			String text = args.next();

			//RGB形式でなければ戻る
			if(!RGB_FORMAT.matcher(text).matches()){
				sender.warn("パーティクル色はRGB形式で指定して下さい。");
				return;
			}

			Parkour parkour = parkours.getParkour(parkourName);

			//各値に分割する
			int[] values = StringSplit.splitToIntArguments(text);

			Color color = new Color(values[0], values[1], values[2]);

			//適用する
			parkour.apply(it -> {
				it.borderColor = color;

				//各アスレ領域の境界線用パケットを再生成する
				if(it.startLine != null) it.startLine.generateParticlePackets();

				if(it.finishLine != null) it.finishLine.generateParticlePackets();

				it.checkAreas.areas.forEach(ParkourRegion::generateParticlePackets);
			});

			sender.info(StringTemplate.capply("$0-&r-&b-のパーティクル色を書き換えました。", parkourName));
			return;
		}case "rewards":{
			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			String text = args.next();

			if(!REWARDS_FORMAT.matcher(text).matches()){
				sender.warn("/setreward [parkour_name] [first/second_and_subsequent]");
				return;
			}

			Parkour parkour = parkours.getParkour(parkourName);

			//各値に分割する
			int[] coins = StringSplit.splitToIntArguments(text);

			//報酬として扱える様にする
			Rewards rewards = new Rewards(coins);

			//適用する
			parkour.apply(it -> it.rewards = rewards);

			Yaml yaml = parkours.makeYaml(parkourName);

			yaml.set("Reward coins", StringTemplate.apply("$0,$1", coins[0], coins[1]));

			yaml.save();

			sender.info(StringTemplate.capply("$0-&r-&b-の報酬を書き換えました。", parkourName));
			return;
		}default:
			sender.warn("/parkour [parkour_name] [create/delete/enable/disable/spawn] | /parkour [parkour_name] color [R,G,B] | /parkour [parkour_name] rewards [coin,coin] | /parkour list");
			return;
		}

	}

	private boolean blockNotExistParkour(Sender sender, String parkourName){
		if(parkours.containsParkour(parkourName)) return false;

		sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", parkourName));
		return true;
	}

}
