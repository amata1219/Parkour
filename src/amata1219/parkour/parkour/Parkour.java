package amata1219.parkour.parkour;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import amata1219.amalib.color.Color;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.Stages;

public class Parkour {

	private final Stages stages = Stages.getInstance();
	private final Parkours parkours = Parkours.getInstance();

	public final String name;
	public boolean enable;
	public World world;
	public Reward reward;
	public ImmutableEntityLocation spawnPoint;
	public Region region;
	public OldParkourRegion startLine, finishLine;
	public Color borderColor;
	public CheckAreas checkAreas;
	public Records records;
	public PlayerConnections connections;

	public Parkour(Yaml yaml){
		name = yaml.name;

		world = Bukkit.getWorld(yaml.getString("World"));

		int[] rewardCoins = StringSplit.splitToIntArguments(yaml.getString("Reward coins"));

		//報酬

		setSpawnLocation(ImmutableEntityLocation.deserialize(yaml.getString("Spawn location")));

		//領域を作成する
		region = Region.deserialize(world, yaml.getString("Region"));

		//カラーを作成する
		borderColor = Color.deserialize(yaml.getString("Particle color"));

		//スタートラインを作成する
		startLine = new OldParkourRegion(this, Region.deserialize(world, yaml.getString("Start line")));

		//フィニッシュラインを作成する
		finishLine = new OldParkourRegion(this, Region.deserialize(world, yaml.getString("Finish line")));

		checkAreas = new CheckAreas(yaml, this);

		records = new Records(yaml);
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

	//このアスレに参加する
	/*public void entry(User user){
		Player player = user.asBukkitPlayer();

		//プレイヤーのコネクションを取得する
		PlayerConnection connection = asEntityPlayer(player).playerConnection;

		//既にコネクションリストに含まれていたら戻る
		if(connections.contains(connection))
			return;

		Parkour currentParkour = user.currentParkour;

		//今遊んでいるアスレがあればそこから退出する
		if(currentParkour != null) currentParkour.exit(user);

		//今いるアスレをこのアスレに書き換える
		user.currentParkour = this;

		//コネクションリストにプレイヤーのコネクションを追加する
		connections.add(connection);

		//プレイヤーが2人以上いれば戻る
		if(connections.size() >= 2)
			return;

		//各領域の境界線を表示する

		startLine.display();
		finishLine.display();
		checkAreas.displayAll();
	}

	//このアスレから退出する
	public void exit(User user){
		Player player = user.asBukkitPlayer();

		//プレイヤーのコネクションを取得する
		PlayerConnection connection = asEntityPlayer(player).playerConnection;

		//今いるアスレと今遊んでいるアスレを削除する
		user.currentParkour = user.parkourPlayingNow = null;

		//コネクションリストからプレイヤーのコネクションを削除する
		connections.remove(connection);

		//プレイヤーがまだ残っていれば戻る
		if(connections.size() > 0)
			return;

		//各領域の境界線を非表示にする

		startLine.undisplay();
		finishLine.undisplay();
		checkAreas.undisplayAll();
	}*/

	//このマップがあるステージを返す
	public Stage getStage(){
		return stages.getStageByParkourName(name);
	}

	public ImmutableEntityLocation getSpawnLocation(){
		return spawnPoint;
	}

	//スポーン地点を設定する
	public void setSpawnLocation(ImmutableEntityLocation spawnLocation){
		//ブロック中央に座標を修正する
		this.spawnPoint = spawnLocation.middle();
	}

	//アスレの領域を返す
	public Region getRegion(){
		return region;
	}

	//アスレの領域をセットする
	public void setRegion(Region newRegion){
		Validate.notNull(newRegion, "Region can not be null");

		region = newRegion;
	}

	public OldParkourRegion getStartLine(){
		return startLine;
	}

	public void setStartLine(OldParkourRegion newStartLine){
		Validate.notNull(newStartLine, "Start line can not be null");

		if(startLine != null)
			parkours.unregisterStartLine(startLine);

		parkours.registerStartLine(startLine);
	}

	public OldParkourRegion getFinishLine(){
		return finishLine;
	}

	public void setFinishLine(OldParkourRegion newFinishLine){
		Validate.notNull(newFinishLine, "Finish line can not be null");

		if(finishLine != null)
			parkours.unregisterStartLine(finishLine);

		parkours.registerStartLine(finishLine);
	}

	public void apply(Parkour parkour){

	}

	public void save(){
		Yaml yaml = parkours.makeYaml(name);

		//ワールド名を記録する
		yaml.set("World", world.getName());

		//yaml.set("Reward coins", StringTemplate.apply("$0,$1", firstRewardCoins, secondAndSubsequentRewardCoins));

		//スポーン地点を記録する
		yaml.set("Spawn location", spawnPoint.serialize());

		//領域情報を記録する
		yaml.set("Region", region.serialize());

		//パーティクルの色をRGBの形式で記録する
		yaml.set("Particle color", StringTemplate.apply("$0,$1,$2", borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue()));

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
