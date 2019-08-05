package amata1219.parkour.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourSet;
import amata1219.parkour.selection.RegionSelectionSet;

public abstract class AbstractSetParkourRegionCommand implements Command {

	private final RegionSelectionSet selections = RegionSelectionSet.getInstance();
	private final ParkourSet parkourSet = ParkourSet.getInstance();

	private final ParkourRegionType type;

	protected AbstractSetParkourRegionCommand(ParkourRegionType type){
		this.type = type;
	}

	@Override
	public void onCommand(Sender sender, Arguments args) {
		//プレイヤーでなければ戻る
		if(blockNonPlayer(sender)) return;

		//送信者をプレイヤーとして取得する
		Player player = sender.asPlayerCommandSender();

		//プレイヤーのUUIDを取得する
		UUID uuid = player.getUniqueId();

		//セレクションが存在しなければ戻る
		if(!selections.hasSelection(uuid)){
			sender.warn(type.regionName + "に設定する範囲を選択して下さい。");
			return;
		}

		//セレクションを取得する
		RegionSelection selection = selections.getSelection(uuid);

		//選択中のアスレを取得する
		Parkour parkour = selections.getSelectedParkourName(uuid);

		String parkourName = parkour.name;

		//アスレのコンフィグを取得する
		Yaml yaml = parkourSet.makeYaml(parkourName);

		yaml.set(type.keyToSaveRegion, selection.toString());

		yaml.save();

		//表示例: Update1のスタートラインを設定しました(world,0,0,0,20,1,2)。
		sender.info(StringTemplate.applyWithColor("$0-&r-&b-の$1を設定しました($2)。", parkourName, type.regionName, selection));
	}

	enum ParkourRegionType {

		PARKOUR_REGION("領域", "Region"),
		START_LINE("スタートライン", "Start line"),
		FINISH_LINE("フィニッシュライン", "Finish line");

		//日本語の領域名
		public final String regionName;

		//コンフィグのキー
		public final String keyToSaveRegion;

		private ParkourRegionType(String regionName, String keyToSaveRegion){
			this.regionName = regionName;
			this.keyToSaveRegion = keyToSaveRegion;
		}

	}

}
