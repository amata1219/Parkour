package amata1219.parkour.stage;

import java.util.ArrayList;
import java.util.List;

import amata1219.parkour.parkour.Parkour;

public class Stage {

	//ステージ名
	public final String name;

	//パルクールのリスト
	public final List<Parkour> parkourList = new ArrayList<>();

	public Stage(String name){
		this.name = name;
	}

}
