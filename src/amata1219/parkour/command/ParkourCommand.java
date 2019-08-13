package amata1219.parkour.command;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.Parkours;
import net.md_5.bungee.api.ChatColor;

public class ParkourCommand implements Command {

	private final Parkours parkours = Parkours.getInstance();

	/*
	 * parkour command
	 *
	 * [parkour] create
	 * [parkour] delete
	 * [parkour] enable
	 * [parkour] disable
	 *
	 * setparkourregion command
	 *
	 * [parkour?] setregion
	 * [parkour?] setstartline
	 * [parkour?] setfinishline
	 *
	 * parkoursetting command
	 *
	 * [parkour?] category
	 * [parkour?] spawn
	 * [parkour?] color [R,G,B]
	 * [parkour?] rewards [F,S]
	 * [parkour?] timeattack [true/false]
	 *
	 * [parkour?] - アスレの編集中であれば入力する必要が無くなる引数
	 */

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//送信者がプレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		//第1引数が無ければ戻る
		if(!args.hasNext()){
			displayCommandUsage(sender);
			return;
		}

		//第1引数をアスレ名として取得する
		String parkourName = ChatColor.translateAlternateColorCodes('&', args.next());

		//第1引数がlistであればアスレ名を全て表示する
		if(parkourName.equals("list")){
			for(Parkour parkour : parkours.getParkours()){
				String text = StringTemplate.capply("&7-: &r-$0 &7-@ &r-$1", parkour.name, parkour.enable ? "&b-有効" : "&7-無効");
				sender.message(text);

			}
			return;
		}

		//第2引数で分岐する
		switch(args.next()){
		case "create":{
			//対応したファイルが存在していれば戻る
			if(parkours.containsParkour(parkourName)){
				sender.warn(StringTemplate.capply("$0-&r-&c-は既に存在しています。", parkourName));
				return;
			}

			//ファイルを作成する
			parkours.makeYaml(parkourName);

			//無効化された状態で登録する
			parkours.registerParkour(parkourName);

			sender.info(StringTemplate.capply("$0-&r-&b-を作成しました。", parkourName));
			break;
		}case "delete":{
			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			//アスレが登録されていれば登録を解除する
			parkours.unregisterParkour(parkourName);

			//ファイルを削除する
			parkours.makeYaml(parkourName).file.delete();

			sender.info(StringTemplate.capply("$0-&r-&b-を削除しました。", parkourName));
			break;
		}case "enable":{
			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			Parkour parkour = getParkour(parkourName);

			if(parkour.enable){
				sender.warn(StringTemplate.capply("$0-&r-&c-は既に有効化されています。", parkourName));
				return;
			}

			//アスレを有効化する
			parkour.update(it -> it.enable = true);

			sender.info(StringTemplate.capply("$0-&r-&b-を有効化しました。", parkourName));
			break;
		}case "disable":{
			//指定されたアスレが存在しなければ戻る
			if(blockNotExistParkour(sender, parkourName)) return;

			Parkour parkour = getParkour(parkourName);

			if(!parkour.enable){
				sender.warn(StringTemplate.capply("$0-&r-&c-は既に無効化されています。", parkourName));
				return;
			}

			//アスレを無効化する
			parkour.update(it -> it.enable = false);

			sender.info(StringTemplate.capply("$0-&r-&b-を無効化しました。", parkourName));
			break;
		}default:
			displayCommandUsage(sender);
			break;
		}
	}

	private void displayCommandUsage(Sender sender){
		sender.warn("/parkour [parkour] create @ 指定された名前でアスレを作成します。");
		sender.warn("/parkour [parkour] delete @ アスレを削除します。");
		sender.warn("/parkour [parkour] enable @ 有効化し選択画面に表示します。");
		sender.warn("/parkour [parkour] disable @ 無効化し選択画面から非表示にします。");
		sender.warn("アスレ名の装飾コードはアンパサンドを使用して下さい。");
	}

	private Parkour getParkour(String parkourName){
		return parkours.getParkour(parkourName);
	}

	private boolean blockNotExistParkour(Sender sender, String parkourName){
		if(parkours.containsParkour(parkourName)) return false;

		sender.warn(StringTemplate.capply("$0-&r-&c-は存在しません。", parkourName));
		return true;
	}

}
