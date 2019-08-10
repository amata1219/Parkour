package amata1219.parkour.parkour;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.util.Consumer;

import amata1219.amalib.location.ImmutableBlockLocation;
import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.util.Color;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.stage.Stage;
import amata1219.parkour.stage.Stages;
import amata1219.parkour.user.User;

public class Parkour {

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

		spawnPoint = (ImmutableEntityLocation) ImmutableEntityLocation.deserialize(yaml.getString("Spawn point")).add(origin);
		region = Region.deserialize(yaml.getString("Region"));
		borderColor = Color.deserialize(yaml.getString("Border color"));
		startLine = new ParkourRegion(this, Region.deserializeToCorners(yaml.getString("Start line")));
		finishLine =  new ParkourRegion(this, Region.deserializeToCorners(yaml.getString("Finish line")));
		checkAreas = new CheckAreas(this, yaml);
		records = new Records(yaml);
		rewards = new Rewards(StringSplit.splitToIntArguments(yaml.getString("Rewards")));
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
		return Stages.getInstance().getStageByParkourName(name);
	}

	public void apply(Consumer<Parkour> apply){
		apply.accept(this);
	}

	public void applyParkourRegion(Consumer<Parkour> apply){
		Parkours parkours = Parkours.getInstance();

		if(enable) parkours.unregisterParkour(this);
		apply.accept(this);
		if(enable) parkours.registerParkour(this);
	}

	public void save(){
		Yaml yaml = Parkours.getInstance().makeYaml(name);

		ImmutableBlockLocation origin = region.lesserBoundaryCorner;

		yaml.set("Origin", origin.serialize());
		yaml.set("Region", region.relative(origin).serialize());
		yaml.set("Spawn point", spawnPoint.relative(origin).serialize());
		yaml.set("Border color", borderColor.serialize());
		yaml.set("Start line", startLine.relative(origin).serialize());
		yaml.set("Finish line", finishLine.relative(origin).serialize());
		yaml.set("Rewards", rewards.serialize());

		checkAreas.save(yaml);
		records.save(yaml);

		yaml.save();
	}

}
