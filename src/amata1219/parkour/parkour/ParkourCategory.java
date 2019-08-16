package amata1219.parkour.parkour;

import org.bukkit.Material;

public enum ParkourCategory {

	NORMAL(Material.DIRT),
	UPDATE(Material.DIAMOND),
	EXTEND(Material.STONE),
	SEGMENT(Material.OBSIDIAN),
	BIOME(Material.OAK_LOG);

	public final String name;
	public final Material icon;

	private ParkourCategory(Material icon){
		this.name = toString().charAt(0) + toString().substring(1).toLowerCase();
		this.icon = icon;
	}

}
