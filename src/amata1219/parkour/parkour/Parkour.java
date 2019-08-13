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
import amata1219.parkour.user.User;

public class Parkour {

	private final Parkours parkours;

	public final String name;
	public boolean enable;
	public ParkourCategory category;
	public Color borderColor;
	public Region region;
	public ImmutableEntityLocation spawnPoint;
	public ParkourRegion startLine, finishLine;
	public CheckAreas checkAreas;
	public Rewards rewards;
	public boolean enableTimeAttack;
	public Records records;
	public PlayerConnections connections = new PlayerConnections();

	public Parkour(Parkours parkours, Yaml yaml){
		this.parkours = parkours;

		name = yaml.name;
		enable = yaml.getBoolean("Enable");
		category = ParkourCategory.valueOf(yaml.getString("Category"));

		//アスレの領域の基準点を生成する
		ImmutableBlockLocation origin = ImmutableBlockLocation.deserialize(yaml.getString("Origin"));

		//スポーン地点を設定する
		ImmutableEntityLocation relativeSpawnPoint = ImmutableEntityLocation.deserialize(yaml.getString("Spawn point"));
		ImmutableEntityLocation absoluteSpawnPoint = (ImmutableEntityLocation) origin.add(relativeSpawnPoint);
		spawnPoint = new ImmutableEntityLocation(origin.world, absoluteSpawnPoint.x, absoluteSpawnPoint.y, absoluteSpawnPoint.z, relativeSpawnPoint.yaw, relativeSpawnPoint.pitch);

		region = origin.add(Region.deserialize(yaml.getString("Region")));
		borderColor = Color.deserialize(yaml.getString("Border color"));
		startLine = new ParkourRegion(this, origin.add(Region.deserialize(yaml.getString("Start line"))));
		finishLine =  new ParkourRegion(this, origin.add(Region.deserialize(yaml.getString("Finish line"))));
		checkAreas = new CheckAreas(parkours, yaml, this, origin);
		enableTimeAttack = yaml.getBoolean("Enable time attack");
		records = new Records(yaml);
		rewards = new Rewards(StringSplit.splitToIntArguments(yaml.getString("Rewards")));
	}

	public String getColorlessName(){
		return ChatColor.stripColor(name);
	}

	public World getWorld(){
		return region.world;
	}

	public void entry(User user){
		if(user.isPlayingWithParkour()) user.parkourPlayingNow.exit(user);

		user.currentParkour = this;

		connections.add(user.asBukkitPlayer());

		//全境界線を表示する
		startLine.displayBorders();
		finishLine.displayBorders();
		checkAreas.displayAll();
	}

	public void exit(User user){
		user.currentParkour = null;

		connections.remove(user.asBukkitPlayer());

		//プレイヤーがいれば戻る
		if(!connections.isEmpty()) return;

		//全境界線を非表示にする
		startLine.undisplayBorders();
		finishLine.undisplayBorders();
		checkAreas.undisplayAll();
	}

	public void apply(Consumer<Parkour> applier){
		applier.accept(this);
	}

	public void update(Consumer<Parkour> applier){
		if(enable) parkours.unregisterParkour(this);
		apply(applier);
		if(enable) parkours.registerParkour(this);
	}

	public void save(){
		Yaml yaml = Parkours.getInstance().makeYaml(name);

		yaml.set("Enable", enable);
		yaml.set("Category", category.toString());

		//アスレの領域の基準点を取得する
		ImmutableBlockLocation origin = region.lesserBoundaryCorner;

		yaml.set("Origin", origin.serialize());
		yaml.set("Region", origin.relative(region).serialize());

		ImmutableEntityLocation relativeSpawnPoint = (ImmutableEntityLocation) origin.relative(spawnPoint);
		yaml.set("Spawn point", new ImmutableEntityLocation(origin.world, relativeSpawnPoint.x, relativeSpawnPoint.y, relativeSpawnPoint.z, spawnPoint.yaw, spawnPoint.pitch).serialize());

		yaml.set("Border color", borderColor.serialize());
		yaml.set("Start line", origin.relative(startLine).serialize());
		yaml.set("Finish line", origin.relative(finishLine).serialize());
		checkAreas.save(yaml, origin);
		yaml.set("Rewards", rewards.serialize());
		yaml.set("Enable time attack", enableTimeAttack);
		records.save(yaml);

		yaml.save();
	}

}
