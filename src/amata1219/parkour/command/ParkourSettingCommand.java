package amata1219.parkour.command;

import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.util.Color;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourCategory;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.parkour.Rewards;
import amata1219.parkour.selection.RegionSelections;

public class ParkourSettingCommand implements Command {

	private static final Pattern RGB_FORMAT = Pattern.compile("^((2[0-4]\\d|25[0-5]|1\\d{1,2}|[1-9]\\d|\\d)( ?, ?)){2}(2[0-4]\\d|25[0-5]|1\\d{1,2}|[1-9]\\d|\\d)");
	private static final Pattern REWARDS_FORMAT = Pattern.compile("[0-9]{1,8},[0-9]{1,8}");

	private final Parkours parkours = Parkours.getInstance();
	private final RegionSelections selections = RegionSelections.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//送信者がプレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		//第1引数が無ければ戻る
		if(!args.hasNext()){
			displayCommandUsage(sender);
			return;
		}

		//送信者のUUIDを取得する
		UUID uuid = sender.asPlayerCommandSender().getUniqueId();

		//対象となるアスレの名前を取得する
		String parkourName = selections.hasSelection(uuid) ? selections.getSelectedParkourName(uuid) : ChatColor.translateAlternateColorCodes('&', args.next());

		//アスレが存在しなければ戻る
		if(!parkours.containsParkour(parkourName)){
			sender.warn("指定されたアスレは存在しません。");
			return;
		}

		Parkour parkour = parkours.getParkour(parkourName);

		switch(args.next()){
		case "info":{
			sender.message(StringTemplate.capply("&7-: &b-Name &7-@ &f-$0", parkourName));
			sender.message(StringTemplate.capply("&7-: &b-Name &7-@ &f-$0", parkour.category.name));
			sender.message(StringTemplate.capply("&7-: &b-Name &7-@ &f-$0", parkour.spawnPoint.serialize().replace(",", "§7,§f")));
			sender.message(StringTemplate.capply("&7-: &b-Name &7-@ &f-$0", parkour.borderColor.serialize()));
			sender.message(StringTemplate.capply("&7-: &b-Name &7-@ &f-$0", parkour.enableTimeAttack));
			break;
		}case "category":{
			try{
				final ParkourCategory category = ParkourCategory.valueOf(args.next().toUpperCase());

				//同じカテゴリーであれば戻る
				if(category == parkour.category){
					sender.warn("このアスレは既に同じカテゴリーに設定されています。");
					return;
				}

				//正しいカテゴリーであれば書き換える
				parkour.apply(it -> it.category = category);
			}catch(Exception e){
				sender.warn("指定されたカテゴリーは不正です。[Normal, Update, Extend, Segment, Biome]から選んで下さい。");
				return;
			}

			sender.info("カテゴリーを設定しました。");
			break;
		}case "spawn": {
			//プレイヤーの座標を取得する
			Location location = sender.asPlayerCommandSender().getLocation();

			//イミュータブルな座標にしブロックの中央に調整した上でセットする
			parkour.apply(it -> it.spawnPoint = new ImmutableEntityLocation(location));

			sender.info(StringTemplate.capply("$0-&r-&b-のスポーン地点を現在地点に書き換えました。", parkourName));
			break;
		}case "color":{
			String text = args.next();

			//RGB形式でなければ戻る
			if(!RGB_FORMAT.matcher(text).matches()){
				sender.warn("パーティクル色はRGB形式で指定して下さい。");
				return;
			}

			//各値に分割する
			int[] values = StringSplit.splitToIntArguments(text);

			//各アスレのパーティクル色を更新する
			parkour.apply(it -> {
				it.borderColor =  new Color(values[0], values[1], values[2]);
				it.startLine.recolorParticles();
				it.finishLine.recolorParticles();
				it.checkAreas.recolorAll();
			});

			sender.info(StringTemplate.capply("$0-&r-&b-のパーティクル色を書き換えました。", parkourName));
			break;
		}case "rewards":{
			String text = args.next();

			if(!REWARDS_FORMAT.matcher(text).matches()){
				sender.warn("/parkour [parkour_name] [first,second_and_subsequent]");
				return;
			}

			//各値に分割する
			int[] coins = StringSplit.splitToIntArguments(text);

			//報酬を更新する
			parkour.apply(it -> it.rewards = new Rewards(coins));

			sender.info(StringTemplate.capply("$0-&r-&b-の報酬を書き換えました。", parkourName));
			return;
		}case "timeattack":{
			if(!args.hasNextBoolean()){
				sender.warn("/parkour [parkour_name] [timeattack] [true/false]");
				return;
			}

			//タイムアタックを有効にするかどうか
			boolean enableTimeAttack = args.nextBoolean();

			String stateName = enableTimeAttack ? "有効" : "無効";

			//既に同じ設定であれば戻る
			if(enableTimeAttack == parkour.enableTimeAttack){
				sender.warn(StringTemplate.apply("既にタイムアタックは$0化されています。", stateName));
				return;
			}

			//更新する
			parkour.apply(it -> it.enableTimeAttack = enableTimeAttack);

			sender.info(StringTemplate.capply("$0-&r-&b-でのタイムアタックを$1にしました。", parkourName, stateName));
			break;
		}default:
			displayCommandUsage(sender);
			break;
		}
	}

	private void displayCommandUsage(Sender sender){
		sender.warn("/parkoursetting [parkour] info @ 情報を表示する");
		sender.warn("/parkoursetting [parkour] category @ カテゴリーを設定する");
		sender.warn("/parkoursetting [parkour] spawn @ 現在地点をスポーン地点に設定する");
		sender.warn("/parkoursetting [parkour] color [R,G,B] @ パーティクル色を設定する");
		sender.warn("/parkoursetting [parkour] rewards [first,after] @ パーティクル色を設定する");
		sender.warn("/parkoursetting [parkour] timeattack [true/false] @ タイムアタックを有効にするかどうか設定する");
		sender.warn("アスレの範囲選択中であれば[parkour]は省略出来る");
	}

}
