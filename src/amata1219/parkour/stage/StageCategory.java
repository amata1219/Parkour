package amata1219.parkour.stage;

public enum StageCategory {

	BIOME,
	SEGMENT,
	EXTEND,
	UPDATE,
	NORMAL;

	public String getName(){
		String name = toString();
		return name.charAt(0) + name.substring(1).toLowerCase();
	}

}
