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
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.parkour.enchantment.GleamEnchantment;

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

	public void gleam(){
		enchantments.put(GleamEnchantment.GLEAM_ENCHANTMENT, 0);
	}

	public void raw(Consumer<ItemStack> raw){
		this.raw = raw;
	}

	public ItemStack build(){
		ItemStack item = basedItemStack != null ? basedItemStack : new ItemStack(material);

		item.setAmount(amount);

		ItemMeta meta = item.getItemMeta();

		if(meta != null){
			if(meta instanceof Damageable) ((Damageable) meta).setDamage(damage);

			meta.setDisplayName(displayName);
			meta.setLore(lore);

			enchantments.entrySet().forEach(entry -> meta.addEnchant(entry.getKey(), entry.getValue(), true));

			meta.addItemFlags(flags.toArray(new ItemFlag[flags.size()]));

			item.setItemMeta(meta);
		}

		if(raw != null) raw.accept(item);

		return item;
	}

}
