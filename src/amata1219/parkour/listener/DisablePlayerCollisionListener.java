package amata1219.parkour.listener;

import static amata1219.amalib.reflection.Reflection.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.amalib.listener.PlayerJoinListener;

public class DisablePlayerCollisionListener implements PlayerJoinListener {

	private static final Method getHandle, sendPacket;
	private static final Field playerConnection, collisionRule, playerNames, teamAction, teamName;
	private static final Constructor<?> newPacketPlayOutScoreboardTeam;

	static{
		Class<?> CraftPlayer = getOBCClass("entity.CraftPlayer");

		getHandle = getMethod(CraftPlayer, "getHandle");

		Class<?> EntityPlayer = getNMSClass("EntityPlayer");

		playerConnection = getField(EntityPlayer, "playerConnection");

		Class<?> PlayerConnection = getNMSClass("PlayerConnection");
		Class<?> Packet = getNMSClass("Packet");

		sendPacket = getMethod(PlayerConnection, "sendPacket", Packet);

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
	public void onChangeWorld(PlayerChangedWorldEvent event){
		disableCollision(event.getPlayer());
	}

	public void disableCollision(Player player){
		Object entityPlayer = invokeMethod(getHandle, player);
		Object playerConnection = getFieldValue(DisablePlayerCollisionListener.playerConnection, entityPlayer);

		Object packet = newInstance(newPacketPlayOutScoreboardTeam);

		setFieldValue(playerNames, packet, Arrays.asList(player.getName()));
		setFieldValue(collisionRule, packet, "never");
		setFieldValue(teamAction, packet, 0);
		setFieldValue(teamName, packet, UUID.randomUUID().toString().substring(0, 15));

		invokeMethod(sendPacket, playerConnection, packet);
	}

}
