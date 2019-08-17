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
	public boolean timeAttackEnable;
	public Records records;
	public PlayerConnections connections = new PlayerConnections();

	public Parkour(Parkours parkours, Yaml yaml){
		this.parkours = parkours;

		//yaml.nameは拡張子を取り除いたファイル名を返すのでアスレ名としてそのまま設定する
		name = yaml.name;

		enable = yaml.getBoolean("Enable");
		category = ParkourCategory.valueOf(yaml.getString("Category"));

		//全座標の基準点
		ImmutableLocation origin = ImmutableLocation.deserialize(yaml.getString("Origin"));

		//座標を主とするこれらのデータは基準点の相対座標として保存されているため絶対座標に変換して設定する

		//領域を設定する
		Region relativeRegion = Region.deserialize(yaml.getString("Region"));
		region = relativeRegion.sub(origin);

		//スポーン地点を設定する
		ImmutableLocation relativeSpawn =ImmutableLocation.deserialize(yaml.getString("Spawn"));
		spawn = origin.add(relativeSpawn);

		//スタートラインを設定する
		Region relativeStartLine = Region.deserialize(yaml.getString("Start line"));
		startLine = new ParkourRegion(this, relativeStartLine.sub(origin));

		//フィニッシュラインを設定する
		Region relativeFinishLine = Region.deserialize(yaml.getString("Finish line"));
		finishLine =  new ParkourRegion(this, relativeFinishLine.sub(origin));

		//チェックエリアを設定する
		checkAreas = new CheckAreas(parkours, yaml, this, origin);

		borderColor = Color.deserialize(yaml.getString("Border color"));
		timeAttackEnable = yaml.getBoolean("Time attack");
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

		MessageTemplate.clapply("$0-&f-にテレポートしました | You teleported to $0", player, name).displayOnActionBar(player);
	}

	public void exit(User user){
		user.currentParkour = user.parkourPlayingNow = null;

		Player player = user.asBukkitPlayer();

		connections.remove(player);

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

		yaml.set("Spawn", origin.relative(spawn).serialize());

		yaml.set("Border color", borderColor.serialize());
		yaml.set("Start line", startLine.relative(origin).serialize());
		yaml.set("Finish line", finishLine.relative(origin).serialize());
		checkAreas.save(yaml, origin);

		yaml.set("Rewards", rewards.serialize());
		yaml.set("Time attack", timeAttackEnable);

		records.save(yaml);

		yaml.save();
	}

}
