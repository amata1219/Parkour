package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import amata1219.amalib.chunk.ChunksToObjectsMap;
import amata1219.amalib.region.Region;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.tuplet.Tuple;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.stage.Stage;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class Parkour {

	//アスレ名
	public final String name;

	//装飾コードを削除したアスレ名
	public final String colorlessName;

	//ワールド
	public World world;

	//各領域の境界線を表示するパーティクルの色
	public final Color particleColor;

	//アスレの領域
	private Region region;

	//スタートライン
	private RegionBorderDisplayer startLine;

	//フィニッシュライン
	private RegionBorderDisplayer finishLine;

	//チェックエリアのリスト
	public final List<RegionBorderDisplayer> checkAreas = new ArrayList<>();

	//自己記録
	public final Map<UUID, Float> selfRecords = new HashMap<>();

	//上位10件の記録
	public final Map<Integer, Tuple<UUID, Float>> top10Records = new HashMap<>(10);

	//プレイヤーのコネクションリスト
	final List<PlayerConnection> connections = new ArrayList<>();

	public Parkour(Yaml yaml){
		name = yaml.name;

		colorlessName = ChatColor.stripColor(name);

		world = Bukkit.getWorld(yaml.getString("World"));

		//領域を作成する
		region = Region.fromString(world, yaml.getString("Region"));

		//テキストをカンマ毎に分割しそれぞれを数値に変換する
		int[] colors = StringSplit.splitToIntArguments(yaml.getString("Particle color"));

		//カラーを作成する
		particleColor = Color.fromRGB(colors[0], colors[1], colors[2]);

		//スタートラインを作成する
		startLine = RegionBorderDisplayer.fromString(this, yaml.getString("Start line"));

		//フィニッシュラインを作成する
		finishLine = RegionBorderDisplayer.fromString(this, yaml.getString("Finish line"));

		//各チェックエリアを作成してリストに詰め込む
		for(String text : yaml.getStringList("Check areas"))
			checkAreas.add(RegionBorderDisplayer.fromString(this, text));
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

		for(RegionBorderDisplayer checkArea : checkAreas)
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

		for(RegionBorderDisplayer checkArea : checkAreas)
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

	public Region getRegion(){
		return region;
	}

	public void setRegion(Region region){
		Validate.notNull(region, "Region can not be null");
	}

	public RegionBorderDisplayer getStartLine(){
		return startLine;
	}

	public void setStartLine(RegionBorderDisplayer newStartLine){
		Validate.notNull(newStartLine, "Start line can not be null");
		setRegion(Main.getParkourSet().chunksToStartLinesMap, startLine, newStartLine);
	}

	public RegionBorderDisplayer getFinishLine(){
		return finishLine;
	}

	public void setFinishLine(RegionBorderDisplayer newFinishLine){
		Validate.notNull(newFinishLine, "Finish line can not be null");
		setRegion(Main.getParkourSet().chunksToFinishLinesMap, finishLine, newFinishLine);
	}

	private void setRegion(ChunksToObjectsMap<RegionBorderDisplayer> chunksToRegionsMap, RegionBorderDisplayer oldDisplayer, RegionBorderDisplayer newDisplayer){
		if(oldDisplayer != null){
			oldDisplayer.cancel();
			Region oldRegion = oldDisplayer.region;
			chunksToRegionsMap.removeAll(oldRegion.lesserBoundaryCorner, oldRegion.greaterBoundaryCorner, oldDisplayer);
		}

		Region newRegion = newDisplayer.region;
		chunksToRegionsMap.putAll(newRegion.lesserBoundaryCorner, newRegion.greaterBoundaryCorner, newDisplayer);
		newDisplayer.display();
	}

	public int getCheckAreaNumber(RegionBorderDisplayer checkArea){
		return checkAreas.contains(checkArea) ? checkAreas.indexOf(checkArea): -1;
	}

	public void setCheckArea(int number, RegionBorderDisplayer checkArea){
		ChunksToObjectsMap<RegionBorderDisplayer> chunksToRegionsMap = Main.getParkourSet().chunksToCheckAreasMap;
		if(number < checkAreas.size()){
			RegionBorderDisplayer oldCheckArea = checkAreas.get(number);
			setRegion(chunksToRegionsMap, oldCheckArea, checkArea);
		}else{
			checkAreas.add(checkArea);
			Region region = checkArea.region;
			chunksToRegionsMap.putAll(region.lesserBoundaryCorner, region.greaterBoundaryCorner, checkArea);
			checkArea.display();
		}
	}

	public void tryToRecordTime(UUID uuid, float time){
		if(selfRecords.getOrDefault(uuid, Float.MAX_VALUE) > time)
			selfRecords.put(uuid, time);
	}

	public void updateTop10Records(){
		List<Entry<UUID, Float>> records = new ArrayList<>(selfRecords.entrySet());
		records.sort(Entry.comparingByValue());

		//上位10件の記録をマップに保存する
		for(int index = 0; index < 10; index++){
			Entry<UUID, Float> record = records.get(index);
			top10Records.put(index, new Tuple<>(record.getKey(), record.getValue()));
		}
	}

}
