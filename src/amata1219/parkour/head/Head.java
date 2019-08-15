package amata1219.parkour.head;

import org.bukkit.inventory.ItemStack;

public class Head {

	public final int id;
	public final int value;
	public final String name;
	public final ItemStack item;

	public Head(int id, int value, String name, ItemStack item){
		this.id = id;
		this.value = value;
		this.name = name;
		this.item = item;
	}

	@Override
	public int hashCode(){
		return id;
	}

}
