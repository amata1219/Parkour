package amata1219.parkour.parkour;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

import amata1219.amalib.location.ImmutableLocation;
import amata1219.amalib.region.Region;
import amata1219.amalib.string.StringSplit;
import amata1219.amalib.string.message.MessageTemplate;
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
	public ImmutableLocation spawn;
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
		ImmutableLocation origin = ImmutableLocation.deserialize(yaml.getString("Origin"));

		//スポーン地点を設定する
		spawn = origin.add(ImmutableLocation.deserialize(yaml.getString("Spawn")));

		region = Region.deserialize(yaml.getString("Region")).sub(origin);
		borderColor = Color.deserialize(yaml.getString("Border color"));
		startLine = new ParkourRegion(this, Region.deserialize(yaml.getString("Start line")).sub(origin));
		finishLine =  new ParkourRegion(this, Region.deserialize(yaml.getString("Finish line")).sub(origin));
		checkAreas = new CheckAreas(parkours, yaml, this, origin);
		enableTimeAttack = yaml.getBoolean("Enable time attack");
		records = new Records(yaml);
		rewards = new Rewards(StringSplit.splitToIntArguments(yaml.getString("Rewards")));
	}

	public String getColorlessName(){
		return ChatColor.stripColor(name);
	}

	public ImmutableLocation getOrigin(){
		return region.lesserBoundaryCorner;
	}

	public World getWorld(){
		return region.world;
	}

	public void teleportTo(Player player){
		player.teleport(spawn.asBukkit());
	}

	public void entry(User user){
		if(user.isPlayingWithParkour()) user.parkourPlayingNow.exit(user);

		user.currentParkour = this;

		Player player = user.asBukkitPlayer();

		//パケット送信用のコネクションリストに追加する
		connections.add(player);

		//全境界線を表示する
		startLine.displayBorders();
		finishLine.displayBorders();
		checkAreas.displayAll();

		MessageTemplate.capply("&7-: You joined $0", name).display(player);
	}

	public void exit(User user){
		user.currentParkour = user.parkourPlayingNow = null;

		Player player = user.asBukkitPlayer();

		connections.remove(player);

		MessageTemplate.capply("&7-: You quit $0", name).display(player);

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
		parkours.unregisterParkour(this);
		apply(applier);
		parkours.registerParkour(this);
	}

	public void save(){
		Yaml yaml = Parkours.getInstance().makeYaml(name);

		yaml.set("Enable", enable);
		yaml.set("Category", category.toString());

		//アスレの領域の基準点を取得する
		ImmutableLocation origin = region.lesserBoundaryCorner;

		yaml.set("Origin", origin.serialize());
		yaml.set("Region", region.relative(origin).serialize());

		yaml.set("Spawn", origin.relative(spawn));

		yaml.set("Border color", borderColor.serialize());
		yaml.set("Start line", startLine.relative(origin).serialize());
		yaml.set("Finish line", finishLine.relative(origin).serialize());
		checkAreas.save(yaml, origin);

		yaml.set("Rewards", rewards.serialize());
		yaml.set("Enable time attack", enableTimeAttack);

		records.save(yaml);

		yaml.save();
	}

}
