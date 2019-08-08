package amata1219.parkour.parkour;

import org.bukkit.ChatColor;
import org.bukkit.World;

import amata1219.amalib.color.Color;
import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.Stages;
import amata1219.parkour.user.User;

public class Parkour {

	private final Stages stages = Stages.getInstance();
	private final Parkours parkours = Parkours.getInstance();

	public final String name;
	public boolean enable;
	public Color borderColor;
	public Region region;
	public ImmutableEntityLocation spawnPoint;
	public ParkourRegion startLine, finishLine;
	public CheckAreas checkAreas;
	public Records records;
	public Rewards rewards;
	public PlayerConnections connections;

	public Parkour(Yaml yaml){
		name = yaml.name;

		//アスレの基準点を生成する
		ImmutableBlockLocation origin = ImmutableBlockLocation.deserialize(yaml.getString("Origin"));

		spawnPoint = (ImmutableEntityLocation) ImmutableEntityLocation.deserialize(yaml.getString("Spawn point")).add(origin).middle();
		region = Region.deserialize(yaml.getString("Region"));
		borderColor = Color.deserialize(yaml.getString("Border color"));
		startLine = new ParkourRegion(this, Region.deserializeToCorners(yaml.getString("Start line")));
		finishLine =  new ParkourRegion(this, Region.deserializeToCorners(yaml.getString("Finish line")));
		checkAreas = new CheckAreas(this, yaml);
		records = new Records(yaml);
		rewards = new Rewards(StringSplit.splitToIntArguments(yaml.getString("Reward coins")));
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

	public World getWorld(){
		return region.world;
	}

	public void entry(User user){
		if(user.isPlayingWithParkour()) user.parkourPlayingNow.exit(user);

		connections.add(user.asBukkitPlayer());

		startLine.displayBorders();
		finishLine.displayBorders();
		checkAreas.displayAll();
	}

	public void exit(User user){
		connections.remove(user.asBukkitPlayer());

		//人がいれば戻る
		if(!connections.isEmpty()) return;

		startLine.undisplayBorders();
		finishLine.undisplayBorders();
		checkAreas.undisplayAll();
	}

	//このマップがあるステージを返す
	public Stage getStage(){
		return stages.getStageByParkourName(name);
	}

	//スポーン地点を設定する
	public void setSpawnLocation(ImmutableEntityLocation spawnLocation){
		//ブロック中央に座標を修正する
		this.spawnPoint = spawnLocation.middle();
	}

	public void apply(Parkour parkour){
		if(enable) 	parkours.unregisterParkour(this);

		enable = parkour.enable;
		region = parkour.region;
		borderColor = parkour.borderColor;
		spawnPoint = parkour.spawnPoint;
		startLine = parkour.startLine;
		finishLine = parkour.finishLine;
		checkAreas = parkour.checkAreas;
		rewards = parkour.rewards;

		if(enable) parkours.registerParkour(this);
	}

	public void save(){
		Yaml yaml = parkours.makeYaml(name);

		ImmutableBlockLocation origin = region.lesserBoundaryCorner;

		yaml.set("Region", region.relative(origin).serialize());
		yaml.set("Spawn location", spawnPoint.relative(origin).serialize());
		yaml.set("Border color", borderColor.serialize());
		yaml.set("Start line", startLine.relative(origin).serialize());
		yaml.set("Finish line", finishLine.relative(origin).serialize());
		yaml.set("Rewards", rewards.serialize());

		checkAreas.save(yaml);
		records.save(yaml);

		yaml.save();
	}

}
