package amata1219.parkour.command;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import amata1219.amalib.command.Arguments;
import amata1219.amalib.command.Command;
import amata1219.amalib.command.Sender;
import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.region.LocationOnBorderCollector;
import amata1219.amalib.region.Region;
import amata1219.amalib.schedule.Async;
import amata1219.amalib.selection.RegionSelection;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.util.Color;
import amata1219.parkour.selection.RegionSelections;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.ParticleParamRedstone;

public class TestCommand implements Command {

	private final RegionSelections selections = RegionSelections.getInstance();

	private BukkitTask task;

	@Override
	public void onCommand(Sender sender, Arguments args) {
		if(blockNonPlayer(sender)) return;

		//test 128,128,128

		//送信者をプレイヤーとして取得する
		Player player = sender.asPlayerCommandSender();

		//プレイヤーのUUIDを取得する
		UUID uuid = player.getUniqueId();

		//セレクションが存在しなければ戻る
		if(!selections.hasSelection(uuid)){
			sender.warn("selection can not be null.");
			return;
		}

		//セレクションを取得する
		RegionSelection selection = selections.getSelection(uuid);

		ImmutableBlockLocation lesserBoundaryCorner = selection.getLesserBoundaryCorner();
		ImmutableBlockLocation greaterBoundaryCorner = selection.getGreaterBoundaryCorner();

		Region region = new Region(lesserBoundaryCorner, greaterBoundaryCorner);

		List<ImmutableEntityLocation> locations = LocationOnBorderCollector.collect(region, 4);

		String text = args.next();

		if(text.equals("cancel")){
			if(task != null) task.cancel();
			sender.info("cancelled");
			return;
		}

		//各値に分割する
		int[] values = StringSplit.splitToIntArguments(text);

		Color color = new Color(values[0], values[1], values[2]);

		//各座標に対応したパーティクルパケットを作成する
		List<PacketPlayOutWorldParticles> packets = locations.stream()
							.map(location -> {
								float red = color.adjustRed(30) / 255f;
								float green = color.adjustGreen(30) / 255f;
								float blue = color.adjustBlue(30) / 255f;

								return new PacketPlayOutWorldParticles(new ParticleParamRedstone(red, green, blue, 1), true,
										(float) location.getEntityX(), (float) location.getEntityY() + 0.1f, (float) location.getEntityZ(),
										red, green, blue, 1, 0);
								})
							.collect(Collectors.toList());

		AtomicInteger counter = new AtomicInteger();
		AtomicInteger position1 = new AtomicInteger(0);
		AtomicInteger position2 = new AtomicInteger(packets.size() / 2);

		final int size = packets.size();

		//非同期で実行する
		task = Async.define(() -> {
			if(counter.incrementAndGet() >= size){
				task.cancel();
				return;
			}

			//ポジションがリストのサイズを超えない様にクリアする(position1 >= size の時、position2 == position1 - (size / 2) である事が保証される為、片方ずつの判定にしている)
			if(position1.get() >= size) position1.set(0);
			else if(position2.get() >= size) position2.set(0);

			//各ポジションに対応したパケットを取得する
			PacketPlayOutWorldParticles packet1 = packets.get(position1.getAndIncrement());
			PacketPlayOutWorldParticles packet2 = packets.get(position2.getAndIncrement());

			for(Player pl : Bukkit.getOnlinePlayers()){
				((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet1);
				((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet2);
			}
		}).executeTimer(0, 3);
	}

}
