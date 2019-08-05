package amata1219.parkour.stage;

public enum StageCategory {

	NORMAL,
	UPDATE,
	EXTEND,
	SEGMENT,
	BIOME;

	public String getName(){
		String name = toString();
		return name.charAt(0) + name.substring(1).toLowerCase();
	}

}
