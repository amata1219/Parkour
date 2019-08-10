package amata1219.parkour.parkour;

public enum ParkourCategory {

	NORMAL,
	UPDATE,
	EXTEND,
	SEGMENT,
	BIOME;

	public final String name;

	private ParkourCategory(){
		this.name = toString().charAt(0) + toString().substring(1).toLowerCase();
	}

}
