package amata1219.parkour.parkour;

import java.util.List;

import amata1219.amalib.space.Space;
import amata1219.amalib.yaml.Yaml;

public class ParkourSetting {

	//パルクールのエリア
	public final Space parkourSpace;

	//スタートライン
	public final Space startLine;

	//ゴールライン
	public final Space goalLine;

	//チェックエリアリスト
	public final List<Space> checkAreas;

	public ParkourSetting(Yaml yaml){

	}

}
