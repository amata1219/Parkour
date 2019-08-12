package amata1219.parkour.parkour;

import amata1219.amalib.yaml.Yaml;

public class RankedParkour extends Parkour {

	//指定されたアスレ名がランクアップアスレであるかどうか
	public static boolean isRankedParkour(Parkour parkour){
		return parkour instanceof RankedParkour;
	}

	public static boolean isRankedParkour(String parkourName){
		return parkourName.startsWith("Update") || parkourName.startsWith("Extend");
	}

	public final RankedParkourType type;
	public final int rank;

	public RankedParkour(Parkours parkours, Yaml yaml) {
		super(parkours, yaml);

		//装飾コードを除いたアスレ名を取得する
		String parkourName = getColorlessName();

		//ランクアップアスレでなければ戻る
		if(!isRankedParkour(parkourName)) throw new IllegalArgumentException("Parkour must be ranked");

		//接頭辞を取得する
		String prefix = parkourName.substring(0, 7);

		//アスレ名からタイプを取得する
		type = RankedParkourType.valueOf(prefix.toUpperCase());

		//アスレ名からランクを取得する
		rank = Integer.parseInt(parkourName.replace(prefix, ""));
	}

	public static enum RankedParkourType {

		UPDATE,
		EXTEND;

	}

}
