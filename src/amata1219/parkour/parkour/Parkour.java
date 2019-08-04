package amata1219.parkour.parkour;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import amata1219.amalib.region.Region;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.StageSet;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class Parkour {

	private final StageSet stages = StageSet.getInstance();
	private final ParkourSet parkourSet = ParkourSet.getInstance();

	//アスレ名
	public final String name;

	//ワールド
	public World world;

	//各領域の境界線を表示するパーティクルの色
	public final Color particleColor;

	//アスレの領域
	private Region region;

	private RegionWithBorders startLine;

	private RegionWithBorders finishLine;

	public final CheckAreaSet checkAreas;

	//自己記録
	public final RecordSet records;

	//プレイヤーのコネクションリスト
	final List<PlayerConnection> connections = new ArrayList<>();

	public Parkour(Yaml yaml){
		//コンフィグのファイル名をアスレ名として扱う
		name = yaml.name;

		world = Bukkit.getWorld(yaml.getString("World"));

		//領域を作成する
		region = Region.deserialize(world, yaml.getString("Region"));

		//テキストをカンマ毎に分割しそれぞれを数値に変換する
		int[] colors = StringSplit.splitToIntArguments(yaml.getString("Particle color"));

		//カラーを作成する
		particleColor = Color.fromRGB(colors[0], colors[1], colors[2]);

		//スタートラインを作成する
		startLine = new RegionWithBorders(this, Region.deserialize(world, yaml.getString("Start line")));

		//フィニッシュラインを作成する
		finishLine = new RegionWithBorders(this, Region.deserialize(world, yaml.getString("Finish line")));

		checkAreas = new CheckAreaSet(yaml, this);

		records = new RecordSet(yaml);
	}

	public String getColorlessName(){
		return ChatColor.stripColor(name);
	}

	public boolean isUpdate(){
		return getColorlessName().startsWith("Update");
	}

	public boolean isExtend(){
		return getColorlessName().startsWith("Extend");
	}

	public boolean isDoombless(){
		return getColorlessName().equals("Doobless");
	}

	public boolean hasReward(){
		try{
			Reward.valueOf(getColorlessName());
		}catch(Exception e){
			return false;
		}
		return true;
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
		checkAreas.display();
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

		startLine.undisplay();
		finishLine.undisplay();
		checkAreas.undisplay();
	}

	//このマップがあるステージを返す
	public Stage getStage(){
		return stages.parkourNamesToStagesMap.get(name);
	}

	//アスレの領域を返す
	public Region getRegion(){
		return region;
	}

	//アスレの領域をセットする
	public void setRegion(Region region){
		Validate.notNull(region, "Region can not be null");
	}

	public RegionWithBorders getStartLine(){
		return startLine;
	}

	public void setStartLine(RegionWithBorders newStartLine){
		Validate.notNull(newStartLine, "Start line can not be null");

		if(startLine != null)
			parkourSet.unregisterStartLine(startLine);

		parkourSet.registerStartLine(startLine);
	}

	public RegionWithBorders getFinishLine(){
		return finishLine;
	}

	public void setFinishLine(RegionWithBorders newFinishLine){
		Validate.notNull(newFinishLine, "Finish line can not be null");

		if(finishLine != null)
			parkourSet.unregisterStartLine(finishLine);

		parkourSet.registerStartLine(finishLine);
	}

	//BukkitプレイヤーをNMSプレイヤーに変換する
	private EntityPlayer asEntityPlayer(Player player){
		return ((CraftPlayer) player).getHandle();
	}

	public void save(){
		Yaml yaml = new Yaml(Main.getPlugin(), new File(parkourSet.folder, StringTemplate.apply("$0.yml", name)));

		//ワールド名を記録する
		yaml.set("World", world.getName());

		//領域情報を記録する
		yaml.set("Region", region.serialize());

		//パーティクルの色をRGBの形式で記録する
		yaml.set("Particle color", StringTemplate.apply("$0,$1,$2", particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue()));

		//スタートラインの領域情報を記録する
		yaml.set("Start line", startLine.region.serialize());

		//フィニッシュラインの領域情報を記録する
		yaml.set("Finish line", finishLine.region.serialize());

		//全チェックエリアの領域情報を記録する
		checkAreas.save(yaml);

		//全レコードを記録する
		records.save(yaml);

		//セーブする
		yaml.save();
	}

}
