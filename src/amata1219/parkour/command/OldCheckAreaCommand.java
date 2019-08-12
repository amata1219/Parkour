package amata1219.parkour.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.parkour.CheckAreas;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.selection.RegionSelections;

public class OldCheckAreaCommand implements Command {

	private final Parkours parkours = Parkours.getInstance();
	private final RegionSelections selections = RegionSelections.getInstance();

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//プレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		Player player = sender.asPlayerCommandSender();
		UUID uuid = player.getUniqueId();

		//第1引数で分岐する
		switch(args.next()){
		case "add":{
			//セレクションが存在しなければ戻る
			if(blockNotHasSelection(sender, uuid)) return;

			//セレクションを取得する
			RegionSelection selection = selections.getSelection(uuid);

			//編集するアスレの名前を取得する
			String parkourName = selections.getSelectedParkourName(uuid);

			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			Parkour parkour = parkours.getParkour(parkourName);

			//セレクションを基にチェックエリアを作成する
			ParkourRegion checkArea = new ParkourRegion(parkour, selection);

			//チェックエリアを追加する
			int checkAreaNumber = parkour.checkAreas.addCheckArea(checkArea);

			sender.info(StringTemplate.capply("$0-&r-&b-にチェックエリア$1を追加しました。", parkourName, checkAreaNumber + 1));
			return;
		}case "list":{
			String parkourName = selections.hasSelection(uuid) ? selections.getSelectedParkourName(uuid) : args.next();

			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			//チェックエリアリストを取得する
			CheckAreas checkAreas = parkours.getParkour(parkourName).checkAreas;

			//チェックエリアが無ければ戻る
			if(checkAreas.areas.isEmpty()){
				sender.warn(StringTemplate.capply("$0-&r-&c-のチェックエリアはまだありません。", parkourName));
				return;
			}

			//全チェックエリアを表示する
			for(int checkAreaNumber = 0; checkAreaNumber < checkAreas.areas.size(); checkAreaNumber++)
				sender.info(StringTemplate.capply("&7-: &b-$0 &7-@ &b-$1", checkAreaNumber + 1, checkAreas.getCheckArea(checkAreaNumber).serialize()));

			return;
		}case "set":{
			if(!args.hasNextInt()){
				sender.warn("書き換えるチェックエリアの番号を指定して下さい。");
				return;
			}

			int checkAreaNumber = args.nextInt() - 1;

			//最小値メソッドで勝手に補正しては困る処理なので警告を表示する
			if(checkAreaNumber < 0){
				sender.warn("チェックエリア番号は1以上で指定して下さい。");
				return;
			}

			//セレクションが存在しなければ戻る
			if(blockNotHasSelection(sender, uuid)) return;

			//セレクションを取得する
			RegionSelection selection = selections.getSelection(uuid);

			//編集するアスレの名前を取得する
			String parkourName = selections.getSelectedParkourName(uuid);

			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			Parkour parkour = parkours.getParkour(parkourName);

			//チェックエリアリストを取得する
			CheckAreas checkAreas = parkour.checkAreas;

			int maxCheckAreaNumber = checkAreas.areas.size();

			//チェックエリア番号が大きすぎる場合
			if(maxCheckAreaNumber <= checkAreaNumber){
				sender.warn(StringTemplate.capply("チェックエリア番号が大きすぎます。$0以下に設定して下さい。", maxCheckAreaNumber));
				return;
			}

			//セレクションを基にチェックエリアを作成する
			ParkourRegion checkArea = new ParkourRegion(parkour, selection);

			//番号に対応したチェックエリアを書き換える
			checkAreas.setCheckArea(checkAreaNumber, checkArea);

			sender.info(StringTemplate.capply("$0-&r-&b-のチェックエリア$1を書き換えました。", parkourName, checkAreaNumber + 1));
			return;
		}case "remove":{
			String parkourName = selections.hasSelection(uuid) ? selections.getSelectedParkourName(uuid) : args.next();

			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			if(!args.hasNextInt()){
				sender.warn("削除するチェックエリアの番号を指定して下さい。");
				return;
			}

			int checkAreaNumber = args.nextInt() - 1;

			if(checkAreaNumber < 0){
				sender.warn("チェックエリア番号は1以上で指定して下さい。");
				return;
			}

			Parkour parkour = parkours.getParkour(parkourName);

			//チェックエリアリストを取得する
			CheckAreas checkAreas = parkour.checkAreas;

			int maxCheckAreaNumber = checkAreas.areas.size();

			//チェックエリア番号が大きすぎる場合
			if(maxCheckAreaNumber <= checkAreaNumber){
				sender.warn(StringTemplate.capply("チェックエリア番号が大きすぎます。$0以下に設定して下さい。", maxCheckAreaNumber));
				return;
			}

			checkAreas.removeCheckArea(checkAreaNumber);

			sender.info(StringTemplate.capply("$0-&r-&b-のチェックエリア$1を削除しました。", parkourName, checkAreaNumber + 1));
			return;
		}default:
			sender.warn("範囲指定した状態で、/checkarea [add/list] | /checkarea [set/remove] [check_area_number] | /checkarea color [R,G,B]");
			sender.warn("または、/checkarea list [parkour_name] | /checkarea [set/remove] [parkour_name] [check_area_number] | /checkarea color [parkour_name] [R,G,B]");
			return;
		}
	}

	private boolean blockNotHasSelection(Sender sender, UUID uuid){
		if(selections.hasSelection(uuid)) return false;

		sender.warn("チェックエリアとして設定する範囲を選択して下さい。");
		return true;
	}

	private boolean blockNotExistParkour(Sender sender, String parkourName){
		if(parkours.containsParkour(parkourName)) return false;

		sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", parkourName));
		return true;
	}

}
