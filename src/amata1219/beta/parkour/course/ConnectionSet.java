package amata1219.beta.parkour.course;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_13_R2.PlayerConnection;

public class ConnectionSet {

	private final HashMap<UUID, PlayerConnection> connections = new HashMap<>();

	public boolean isEmpty(){
		return connections.isEmpty();
	}

	public boolean contains(Player player){
		return connections.containsKey(player);
	}

	public void add(Player player){
		connections.put(player.getUniqueId(), ((CraftPlayer) player).getHandle().playerConnection);
	}

	public void remove(Player player){
		connections.remove(player.getUniqueId());
	}

	public void forEach(Consumer<PlayerConnection> action){
		connections.values().forEach(action);
	}

}
