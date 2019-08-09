package amata1219.parkour.parkour;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.scheduler.BukkitTask;

import amata1219.amalib.border.LocationOnBorderCollector;
import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.schedule.Async;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.tuplet.Tuple;
import amata1219.amalib.util.Color;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.ParticleParamRedstone;

public class ParkourRegion extends Region {

	//この領域のあるアスレ(飽く迄プレイヤーのコネクションを取得する為の存在)
	public final Parkour parkour;

	//各地点のパーティクル
	private List<PacketPlayOutWorldParticles> packets;

	//パーティクルの表示位置
	private int position1, position2;

	//パーティクルパケットを送信する非同期のループタスク
	private BukkitTask task;

	public ParkourRegion(Parkour parkour, RegionSelection selection){
		this(parkour, selection.getLesserBoundaryCorner(), selection.getGreaterBoundaryCorner());
	}

	public ParkourRegion(Parkour parkour, Tuple<ImmutableBlockLocation, ImmutableBlockLocation> corners){
		this(parkour, corners.first, corners.second);
	}

	public ParkourRegion(Parkour parkour, ImmutableBlockLocation lesserBoundaryCorner, ImmutableBlockLocation greaterBoundaryCorner){
		super(lesserBoundaryCorner, greaterBoundaryCorner);

		this.parkour = parkour;

		generateParticlePackets();
	}

	public void generateParticlePackets(){
		boolean running = task != null;

		if(running) undisplayBorders();

		List<ImmutableEntityLocation> locations = LocationOnBorderCollector.collect(this, 4);

		Color color = parkour.borderColor;

		//各座標に対応したパーティクルパケットを作成する
		packets = locations.stream()
							.map(location -> {
								float red = color.adjustRed(30) / 255f;
								float green = color.adjustGreen(30) / 255f;
								float blue = color.adjustBlue(30) / 255f;

								return new PacketPlayOutWorldParticles(new ParticleParamRedstone(red, green, blue, 1), true,
										(float) location.getEntityX(), (float) location.getEntityY(), (float) location.getEntityZ(),
										red, green, blue, 1, 0);
								})
							.collect(Collectors.toList());

		position1 = 0;

		//index2はその対角線上の頂点を始点とする
		position2 = packets.size() / 2;

		if(running) displayBorders();
	}

	//境界線を表示する
	public void displayBorders(){
		//既に実行されていれば戻る
		if(task != null) return;

		//コネクションリストが空であれば戻る
		if(parkour.connections.isEmpty())
			return;

		final int size = packets.size();

		//非同期で実行する
		task = Async.define(() -> {
			//ポジションがリストのサイズを超えない様にクリアする(position1 >= size の時、position2 == position1 - (size / 2) である事が保証される為、片方ずつの判定にしている)
			if(position1 >= size) position1 = 0;
			else if(position2 >= size) position2 = 0;

			//各ポジションに対応したパケットを取得する
			PacketPlayOutWorldParticles packet1 = packets.get(position1++);
			PacketPlayOutWorldParticles packet2 = packets.get(position2++);

			parkour.connections.getConnections().forEach(connection -> {
				connection.sendPacket(packet1);
				connection.sendPacket(packet2);
			});
		}).executeTimer(0, 3);
	}

	//境界線を非表示にする
	public void undisplayBorders(){
		if(task != null) task.cancel();
	}

}
