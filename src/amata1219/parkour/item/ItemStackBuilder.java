package amata1219.parkour.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ItemStackBuilder {

	private final ItemStack basedItemStack;
	private final Material material;
	private int amount = 1;
	private int damage;
	private String displayName;
	private ArrayList<String> lore = new ArrayList<>();
	private Map<Enchantment, Integer> enchantments = new HashMap<>();
	private Set<ItemFlag> flags = new HashSet<>();
	private Consumer<ItemStack> raw;

	public static ItemStackBuilder builder(ItemStack basedItemStack){
		return new ItemStackBuilder(basedItemStack);
	}

	public static ItemStackBuilder builder(Material material){
		return new ItemStackBuilder(material);
	}

	public ItemStackBuilder(ItemStack basedItemStack){
		this.basedItemStack = basedItemStack;
		this.material = basedItemStack.getType();
	}

	public ItemStackBuilder(Material material){
		this.basedItemStack = null;
		this.material = material;
	}

	public void setAmount(int amount){
		this.amount = amount;
	}

	public void setDamage(int damage){
		this.damage = damage;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public void addLore(String text){
		lore.add(text);
	}

	public void setLore(Collection<String> lore){
		this.lore.clear();
		this.lore.addAll(lore);
	}

	public void addEnchantment(Enchantment enchantment){
		addEnchantment(enchantment, 1);
	}

	public void addEnchantment(Enchantment enchantment, int level){
		enchantments.put(enchantment, level);
	}

	public void addFlag(ItemFlag flag){
		flags.add(flag);
	}

}
