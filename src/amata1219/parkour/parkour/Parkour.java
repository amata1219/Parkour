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

	public Parkour(Yaml yaml){
		name = yaml.name;

		category = ParkourCategory.valueOf(yaml.getString("Category"));

		//アスレの基準点を生成する
		ImmutableBlockLocation origin = ImmutableBlockLocation.deserialize(yaml.getString("Origin"));

		spawnPoint = (ImmutableEntityLocation) origin.add(ImmutableEntityLocation.deserialize(yaml.getString("Spawn point")));
		region = origin.add(Region.deserialize(yaml.getString("Region")));
		borderColor = Color.deserialize(yaml.getString("Border color"));
		startLine = new ParkourRegion(this, origin.add(Region.deserialize(yaml.getString("Start line"))));
		finishLine =  new ParkourRegion(this, origin.add(Region.deserialize(yaml.getString("Finish line"))));
		checkAreas = new CheckAreas(yaml, this, origin);
		enableTimeAttack = yaml.getBoolean("Enable time attack");
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

		user.currentParkour = this;

		connections.add(user.asBukkitPlayer());

		startLine.displayBorders();
		finishLine.displayBorders();
		checkAreas.displayAll();
	}

	public void exit(User user){
		user.currentParkour = null;

		connections.remove(user.asBukkitPlayer());

		//人がいれば戻る
		if(!connections.isEmpty()) return;

		startLine.undisplayBorders();
		finishLine.undisplayBorders();
		checkAreas.undisplayAll();
	}

	public void apply(Consumer<Parkour> apply){
		apply.accept(this);
	}

	public void applyAndUpdate(Consumer<Parkour> apply){
		Parkours parkours = Parkours.getInstance();

		if(enable) parkours.unregisterParkour(this);
		apply.accept(this);
		if(enable) parkours.registerParkour(this);
	}

	public void save(){
		Yaml yaml = Parkours.getInstance().makeYaml(name);

		yaml.set("Category", category.toString());

		ImmutableBlockLocation origin = region.lesserBoundaryCorner;

		yaml.set("Origin", origin.serialize());
		yaml.set("Region", origin.relative(region).serialize());
		yaml.set("Spawn point", origin.relative(spawnPoint).serialize());
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
