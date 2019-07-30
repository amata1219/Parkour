package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import amata1219.amalib.region.Region;
import amata1219.amalib.text.StringSplit;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.stage.Stage;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class Parkour {

	//アスレ名
	public final String name;

	//ワールド
	public final World world;

	//アスレの領域
	public final Region region;

	//スタートライン
	public final RegionBorder startLine;

	//フィニッシュライン
	public final RegionBorder finishLine;

	//チェックエリアのリスト
	public final List<RegionBorder> checkAreas = new ArrayList<>();

	//最高クリアタイム
	public final Map<UUID, Float> uuidsToHighestRecordsMap = new HashMap<>();

	//最新のハイスコアテキスト、フォーマット決定したらuuidとrecord織り込んで固定
	//private final String[] latestHighRecords = new String[10];

	//プレイヤーのコネクションリスト
	final List<PlayerConnection> connections = new ArrayList<>();

	public Parkour(Yaml yaml){
		name = yaml.name;

		world = Bukkit.getWorld(yaml.getString("World"));

		//領域を作成する
		region = Region.fromString(world, yaml.getString("Region"));

		//テキストをカンマ毎に分割しそれぞれを数値に変換する
		int[] colors = StringSplit.splitToIntArguments(yaml.getString("Color"));

		//カラーを作成する
		Color color = Color.fromRGB(colors[0], colors[1], colors[2]);

		//スタートラインを作成する
		startLine = RegionBorder.fromString(this, color, yaml.getString("Start line"));

		//フィニッシュラインを作成する
		finishLine = RegionBorder.fromString(this, color, yaml.getString("Finish line"));

		//各チェックエリアを作成してリストに詰め込む
		for(String text : yaml.getStringList("Check areas"))
			checkAreas.add(RegionBorder.fromString(this, color, text));
	}

	//このアスレに参加する
	public void join(Player player){
		//プレイヤーのコネクションを取得する
		PlayerConnection connection = asEntityPlayer(player).playerConnection;

		//既にコネクションリストに含まれていたら戻る
		if(connections.contains(connection))
			return;

		//コネクションリストにプレイヤーのコネクションを追加する
		connections.add(connection);

		//プレイヤーが2人以上いれば戻る
		if(connections.size() >= 2)
			return;

		//各領域の境界線を表示する

		startLine.display();
		finishLine.display();

		for(RegionBorder checkArea : checkAreas)
			checkArea.display();
	}

	//このアスレから退出する
	public void quit(Player player){
		//プレイヤーのコネクションを取得する
		PlayerConnection connection = asEntityPlayer(player).playerConnection;

		//コネクションリストからプレイヤーのコネクションを削除する
		connections.remove(connection);

		//プレイヤーがまだ残っていれば戻る
		if(connections.size() > 0)
			return;

		//各領域の境界線を非表示にする

		startLine.cancel();
		finishLine.cancel();

		for(RegionBorder checkArea : checkAreas)
			checkArea.cancel();
	}

	//BukkitプレイヤーをNMSプレイヤーに変換する
	private EntityPlayer asEntityPlayer(Player player){
		return ((CraftPlayer) player).getHandle();
	}

	//このマップがあるステージを返す
	public Stage getStage(){
		return Main.getStageSet().parkourNamesToStagesMap.get(name);
	}

	public void tryToRecordTime(UUID uuid, float time){
		if(uuidsToHighestRecordsMap.getOrDefault(uuid, Float.MAX_VALUE) > time)
			uuidsToHighestRecordsMap.put(uuid, time);
	}

	public void updateHighRecords(){
		AtomicInteger counter = new AtomicInteger();
		uuidsToHighestRecordsMap.entrySet()
		.stream()
		.sorted(Entry.comparingByValue())
		.forEachOrdered(entry -> {
			int index = counter.getAndIncrement();
			if(index < 10)
				latestHighRecords
		});
	}

	public String[] getLatestHighRecords(){
		return latestHighRecords;
	}

}
