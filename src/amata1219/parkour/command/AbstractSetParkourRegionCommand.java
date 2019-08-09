package amata1219.parkour.command;

import java.util.UUID;

import org.bukkit.entity.Player;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.string.StringTemplate;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.selection.RegionSelections;

public abstract class AbstractSetParkourRegionCommand implements Command {

	private final RegionSelections selections = RegionSelections.getInstance();
	private final Parkours parkours = Parkours.getInstance();

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

		ImmutableBlockLocation lesserBoundaryCorner = selection.getLesserBoundaryCorner();
		ImmutableBlockLocation greaterBoundaryCorner = selection.getGreaterBoundaryCorner();

		//編集するアスレの名前を取得する
		String parkourName = selections.getSelectedParkourName(uuid);

		//アスレを取得する
		Parkour parkour = parkours.getParkour(parkourName);

		//タイプに合わせて適用する
		parkour.applyParkourRegion(it -> {
			switch(type){
			case PARKOUR_REGION:
				it.region = new Region(lesserBoundaryCorner, greaterBoundaryCorner);
				break;
			case START_LINE:
				it.startLine = new ParkourRegion(it, lesserBoundaryCorner, greaterBoundaryCorner);
				break;
			case FINISH_LINE:
				it.finishLine = new ParkourRegion(it, lesserBoundaryCorner, greaterBoundaryCorner);
				break;
			default:
				throw new IllegalStateException("Invalid parkour region type");
			}
		});

		//表示例: Update1のスタートラインを設定しました(world,0,0,0,20,1,2)。
		sender.info(StringTemplate.capply("$0-&r-&b-の$1を設定しました($2)。", parkourName, type.regionName, selection));
	}

	enum ParkourRegionType {

		PARKOUR_REGION("領域", "Region"),
		START_LINE("スタートライン", "Start line"),
		FINISH_LINE("フィニッシュライン", "Finish line");

		//領域の名前
		public final String regionName;

		//コンフィグのキー
		public final String keyToSaveRegion;

		private ParkourRegionType(String regionName, String keyToSaveRegion){
			this.regionName = regionName;
			this.keyToSaveRegion = keyToSaveRegion;
		}

	}

}
