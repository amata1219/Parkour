package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_13_R2.CraftParticle;
import org.bukkit.scheduler.BukkitTask;

import amata1219.amalib.border.LocationOnBorderCollector;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.schedule.Async;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.ParticleParam;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class RegionBorderDisplayer {

	private static final ParticleParam PARTICLE_DATA = CraftParticle.toNMS(org.bukkit.Particle.REDSTONE, null);
	private static final boolean FLAG = true;
	private static final Random RANDOM = new Random();
	private static final float EXTRA = 1;
	private static final int COUNT = 0;

	//領域があるアスレ
	public final Parkour parkour;

	//領域
	public final Region region;

	//各地点のパーティクルのリスト
	private final List<PacketPlayOutWorldParticles> packets;

	//パーティクルの表示位置
	private int position1, position2;

	//パーティクルパケットを送信する非同期のループタスク
	private BukkitTask task;

	public static RegionBorderDisplayer fromString(Parkour parkour, String text){
		Region region = Region.fromString(parkour.world, text);
		return new RegionBorderDisplayer(parkour, region);
	}

	public RegionBorderDisplayer(Parkour parkour,  Region region){
		this.parkour = parkour;
		this.region = region;

		List<ImmutableEntityLocation> locationsOnBorderLines = LocationOnBorderCollector.collect(region, 4);

		int size = locationsOnBorderLines.size();

		//index1は原点、index2はその対角線上の頂点を始点とする
		position1 = 0;
		position2 = size / 2;

		packets = new ArrayList<>(size);

		Color color = parkour.particleColor;

		for(ImmutableEntityLocation location : locationsOnBorderLines){
			//パケットを作成する
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(PARTICLE_DATA, FLAG,
					(float) location.getEntityX(), (float) location.getEntityY(), (float) location.getEntityZ(),
					adjustColor(color.getRed()), adjustColor(color.getGreen()), adjustColor(color.getBlue()), EXTRA, COUNT);

			packets.add(packet);
		}
	}

	//境界線を表示する
	public void display(){
		//既に実行されているタスクがあればそれをキャンセルする
		cancel();

		final int size = packets.size();

		//非同期で実行する
		task = Async.define(() -> {
			//ポジションがリストのサイズを超えない様にクリアする(position1 >= size の時、position2 == position1 - (size / 2) である事が保証される為、片方ずつの判定にしている)
			if(position1 >= size)
				position1 = 0;
			else if(position2 >= size)
				position2 = 0;

			//各ポジションに対応したパケットを取得する
			PacketPlayOutWorldParticles packet1 = packets.get(position1++);
			PacketPlayOutWorldParticles packet2 = packets.get(position2++);

			//パケットを送信する
			for(PlayerConnection connection : parkour.connections){
				connection.sendPacket(packet1);
				connection.sendPacket(packet2);
			}
		}).executeTimer(0, 6);
	}

	//境界線を非表示にする
	public void cancel(){
		if(task != null)
			task.cancel();
	}

	//色を±15の範囲で調整する
	private float adjustColor(int color){
		return Math.max(Math.min(color + RANDOM.nextInt(30) - 14, 255), 0);
	}

}
