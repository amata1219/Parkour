package amata1219.parkour.command;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;

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

public class CheckAreaCommand implements Command {

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
		CheckAreas checkAreas = parkour.checkAreas;

		switch (args.next()) {
		case "add":{
			//範囲選択がされていなければ戻る
			if(blockNotSelected(sender)) return;

			int maxMajorCheckAreaNumber = checkAreas.getMaxMajorCheckAreaNumber();

			//メジャーチェックエリア番号を取得する
			int majorCheckAreaNumber = args.hasNextInt() ? args.nextInt() - 1 : maxMajorCheckAreaNumber + 1;

			//不正な番号が指定された場合
			if(majorCheckAreaNumber < 0 || majorCheckAreaNumber - 1 > maxMajorCheckAreaNumber){
				sender.warn("指定されたメジャーCA番号は正しくありません。");
				return;
			}

			//選択範囲を取得する
			RegionSelection selection = selections.getSelection(uuid);

			//新しくチェックエリアを生成する
			ParkourRegion newCheckArea = generateParkourRegion(parkour, selection);

			//バインドする
			checkAreas.bindCheckArea(majorCheckAreaNumber, newCheckArea);

			sender.info("指定されたアスレにチェックエリアを追加しました。");
			break;
		}case "set":{
			//範囲選択がされていなければ戻る
			if(blockNotSelected(sender)) return;

			//メジャーチェックエリア番号が指定されていなければ戻る
			if(!args.hasNextInt()){
				sender.warn("メジャーCA番号を指定して下さい。");
				return;
			}

			//メジャーチェックエリア番号を取得する
			int majorCheckAreaNumber = args.nextInt() - 1;

			//不正なメジャーチェックエリア番号であれば戻る
			if(blockInvalidMajorCheckAreaNumber(sender, checkAreas, majorCheckAreaNumber)) return;

			//マイナーチェックエリア番号が指定されていなければ戻る
			if(!args.hasNextInt()){
				sender.warn("マイナーCA番号を指定して下さい。");
				return;
			}

			//マイナーチェックエリア番号を取得する
			int minorCheckAreaNumber = args.nextInt() - 1;

			//不正なマイナーチェックエリア番号であれば戻る
			if(blockInvalidMinorCheckAreaNumber(sender, checkAreas, majorCheckAreaNumber, minorCheckAreaNumber)) return;

			//選択範囲を取得する
			RegionSelection selection = selections.getSelection(uuid);

			//新しくチェックエリアを生成する
			ParkourRegion newCheckArea = generateParkourRegion(parkour, selection);

			checkAreas.setCheckArea(majorCheckAreaNumber, minorCheckAreaNumber, newCheckArea);

			sender.warn("指定した番号のチェックエリアを書き換えました。");
			break;
		}case "insert":{

		}case "remove":{
			//メジャーチェックエリア番号が指定されていなければ戻る
			if(!args.hasNextInt()){
				sender.warn("メジャーCA番号を指定して下さい。");
				return;
			}

			//メジャーチェックエリア番号を取得する
			int majorCheckAreaNumber = args.nextInt() - 1;

			//不正なメジャーチェックエリア番号であれば戻る
			if(blockInvalidMajorCheckAreaNumber(sender, checkAreas, majorCheckAreaNumber)) return;

			//マイナーチェックエリア番号が指定されていなければ戻る
			if(!args.hasNextInt()){
				sender.warn("マイナーCA番号を指定して下さい。");
				return;
			}

			//マイナーチェックエリア番号を取得する
			int minorCheckAreaNumber = args.nextInt() - 1;

			//不正なマイナーチェックエリア番号であれば戻る
			if(blockInvalidMinorCheckAreaNumber(sender, checkAreas, majorCheckAreaNumber, minorCheckAreaNumber)) return;

			//指定された番号にバインドされたチェックエリアを削除する
			checkAreas.unbindCheckArea(majorCheckAreaNumber, minorCheckAreaNumber);

			sender.warn("指定された番号のチェックエリアを削除しました。");
			break;
		}case "clear":{
			//メジャーチェックエリア番号が指定されていなければ戻る
			if(!args.hasNextInt()){
				sender.warn("メジャーCA番号を指定して下さい。");
				return;
			}

			//メジャーチェックエリア番号を取得する
			int majorCheckAreaNumber = args.nextInt() - 1;

			//不正なメジャーチェックエリア番号であれば戻る
			if(blockInvalidMajorCheckAreaNumber(sender, checkAreas, majorCheckAreaNumber)) return;

			//指定された番号にバインドされたチェックエリアを全て削除する
			checkAreas.unbindAllCheckAreas(majorCheckAreaNumber);

			sender.warn("指定された番号のチェックエリアを全て削除しました。");
			break;
		}case "list":{
			Map<Integer, List<ParkourRegion>> areasMap = checkAreas.getCheckAreas();

			//空であればその趣旨のメッセージを表示して戻る
			if(areasMap.isEmpty()){
				sender.info("このアスレにチェックエリアは存在しません。");
				return;
			}

			//各メジャーチェックエリア番号毎に処理をする
			for(Entry<Integer, List<ParkourRegion>> areasEntry : areasMap.entrySet()){
				int majorCheckAreaNumber = areasEntry.getKey();

				//番号を表示する
				sender.message(StringTemplate.capply("&7-: &b-$0", majorCheckAreaNumber + 1));

				List<ParkourRegion> areas = areasEntry.getValue();

				//各チェックエリアの座標情報を表示する
				for(int minorCheckAreaNumber = 0; minorCheckAreaNumber < areas.size(); minorCheckAreaNumber++){
					ParkourRegion area = areas.get(minorCheckAreaNumber);
					sender.message(StringTemplate.capply("  &7-: &f-$0 - &f-$1", minorCheckAreaNumber + 1, area.serialize().replace(",", "§7,§f")));
				}
			}
			break;
		}default:
			displayCommandUsage(sender);
			break;
		}
	}

	private void displayCommandUsage(Sender sender){
		sender.warn("/checkarea add @ CAを追加する");
		sender.warn("/checkarea add [major] @ 指定メジャーCA番号にCAを追加する");
		sender.warn("/checkarea set [major] [minor] @ 指定メジャーCA番号、マイナーCA番号に設定されているCAを書き換える");
		sender.warn("/checkarea [parkour] remove [major] [minor] @ 指定メジャーCA番号、マイナーCA番号に設定されているCAを削除する");
		sender.warn("/checkarea [parkour] clear [major] @ 指定メジャーCA番号に設定されているCAを全て削除する");
		sender.warn("/checkarea [parkour] list @ CA一覧を表示する");
		sender.warn("アスレの範囲選択中であれば[parkour]は省略出来る");
	}

	private boolean blockNotSelected(Sender sender){
		if(selections.hasSelection(sender.asPlayerCommandSender().getUniqueId())) return false;

		sender.warn("範囲を指定して下さい。");
		return true;
	}

	private boolean blockInvalidMajorCheckAreaNumber(Sender sender, CheckAreas checkAreas, int majorCheckAreaNumber){
		if(majorCheckAreaNumber >= 0 && majorCheckAreaNumber <= checkAreas.getMaxMajorCheckAreaNumber()) return false;

		sender.warn("指定されたメジャーCA番号は不正です。");
		return true;
	}

	private boolean blockInvalidMinorCheckAreaNumber(Sender sender, CheckAreas checkAreas, int majorCheckAreaNumber, int minorCheckAreaNumber){
		List<ParkourRegion> areas = checkAreas.getCheckAreas(majorCheckAreaNumber);

		if(minorCheckAreaNumber >= 0 && minorCheckAreaNumber < areas.size()) return false;

		sender.warn("指定されたマイナーCA番号は不正です。");
		return true;
	}

	private ParkourRegion generateParkourRegion(Parkour parkour, RegionSelection selection){
		return new ParkourRegion(parkour, selection);
	}

}
