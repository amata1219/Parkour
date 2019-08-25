package amata1219.parkour.listener;

import static amata1219.parkour.util.Reflection.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.minecraft.server.v1_13_R2.Packet;

public class DisablePlayerCollision implements PlayerJoinListener {

	private static final Field collisionRule, playerNames, teamAction, teamName;
	private static final Constructor<?> newPacketPlayOutScoreboardTeam;

	static{
		//プライベートなフィールドを書き換える為にリフレクションを用いる

		Class<?> PacketPlayOutScoreboardTeam = getNMSClass("PacketPlayOutScoreboardTeam");

		newPacketPlayOutScoreboardTeam = getConstructor(PacketPlayOutScoreboardTeam);

		collisionRule = getField(PacketPlayOutScoreboardTeam, "f");
		playerNames = getField(PacketPlayOutScoreboardTeam, "h");
		teamAction = getField(PacketPlayOutScoreboardTeam, "i");
		teamName = getField(PacketPlayOutScoreboardTeam, "a");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		disableCollision(event.getPlayer());
	}

	@EventHandler
	public void onChangedWorld(PlayerChangedWorldEvent event){
		disableCollision(event.getPlayer());
	}

	public void disableCollision(Player player){
		//新しくパケットを作成する
		Object packet = newInstance(newPacketPlayOutScoreboardTeam);

		//対象となるプレイヤーを書き込む
		setFieldValue(playerNames, packet, Arrays.asList(player.getName()));

		//衝突はしない設定にする
		setFieldValue(collisionRule, packet, "never");

		//チームに参加するアクションとする
		setFieldValue(teamAction, packet, 0);

		//適当にチーム名を決める
		setFieldValue(teamName, packet, UUID.randomUUID().toString().substring(0, 15));

		((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
	}

}
