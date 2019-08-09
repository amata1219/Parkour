package amata1219.parkour.head;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import amata1219.amalib.util.SkullMaker;

public class Head {

	public final UUID uuid;
	public final String name;
	public final int value;
	public final ItemStack item;

	public Head(UUID uuid, String playerName, int value){
		this.uuid = uuid;
		name = playerName;
		this.value = value;
		item = SkullMaker.fromPlayerUniqueId(uuid);
	}

	public Head(UUID uuid, String headName, int value, String base64){
		this.uuid = uuid;
		name = headName;
		this.value = value;
		item = SkullMaker.fromBase64(base64);
	}

}
