package amata1219.parkour.user;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import amata1219.amalib.location.ImmutableEntityLocation;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.Parkours;
import amata1219.parkour.stage.Stage;

public class User {

	//UUID
	public final UUID uuid;

	//Updateのランク
	public int updateRank;

	//Extendのランク
	public int extendRank;

	//コイン
	private int coins;

	//今いるステージ
	public Stage currentStage;

	//現在いるアスレ
	public Parkour currentParkour;

	//現在プレイ中のアスレ
	public Parkour parkourPlayingNow;

	//プレイし始めた時間(ミリ秒)
	public long timeToStartPlaying;

	//各アスレのチェックポイント
	public final Checkpoints checkpoints;

	//個人設定
	public final UserSetting setting;

	//クリア済みのアスレの名前リスト
	public final Set<String> clearedParkourNames;

	//クリエイティブワールドでのチェックポイント
	public ImmutableEntityLocation creativeWorldCheckpoint;

	//購入したヘッドのセット
	public final PurchasedHeads heads;

	//スコアボードの管理インスタンス
	public InformationBoard board;

	//InventoryUIの管理インスタンス
	public InventoryUIs inventoryUIs;

	public User(Yaml yaml){
		//ファイル名に基づきUUIDを生成し代入する
		this.uuid = UUID.fromString(yaml.name);

		//Updateランクを取得する
		updateRank = yaml.getInt("Update rank");

		//Extendランクを取得する
		extendRank = yaml.getInt("Extend rank");

		//コイン数を取得する
		coins = yaml.getInt("Coins");

		Parkours parkourSet = Parkours.getInstance();

		//最後にいたアスレを取得する
		currentParkour = parkourSet.getParkour("Current parkour");

		//最後に遊んでいたアスレを取得する
		parkourPlayingNow = parkourSet.getParkour("Currently playing parkour");

		//最後にアスレをプレイし始めた時間を取得する
		timeToStartPlaying = yaml.getLong("Time to start playing");

		checkpoints = new Checkpoints(yaml);

		//個人設定はYamlに基づき生成する
		setting = new UserSetting(yaml);

		//クリア済みのアスレ名リストを取得してセットでラップする
		clearedParkourNames = new HashSet<>(yaml.getStringList("Cleared parkur names"));

		//データを基に座標を作成する
		creativeWorldCheckpoint = ImmutableEntityLocation.deserialize(yaml.getString("Creative world checkpoint"));

		//購入済みのスカルのIDをUUIDに変換したリストを作成する
		heads = new PurchasedHeads(this, yaml);
	}

	public Player asBukkitPlayer(){
		return Bukkit.getPlayer(uuid);
	}

	public int getUpdateRank(){
		return updateRank;
	}

	public void incrementUpdateRank(){
		updateRank++;
	}

	public int getExtendRank(){
		return extendRank;
	}

	public void incrementExtendRank(){
		extendRank++;
	}

	public int getCoins(){
		return coins;
	}

	public void depositCoins(int coins){
		this.coins += coins;

		if(board != null) board.updateCoins();
	}

	public void withdrawCoins(int coins){
		this.coins = Math.max(this.coins - coins, 0);

		if(board != null) board.updateCoins();
	}

	public Stage getCurrentStage(){
		return currentStage;
	}

	public boolean isPlayingWithParkour(){
		return parkourPlayingNow != null;
	}

	public InformationBoard getInformationBoard(){
		return board;
	}

	public InventoryUIs getInventoryUIs(){
		return inventoryUIs;
	}

	public void save(){
		Yaml yaml = Users.getInstnace().makeYaml(uuid);

		//Updateランクを記録する
		yaml.set("Update rank", updateRank);

		//Extendランクを取得する
		yaml.set("Extend rank", extendRank);

		//コイン数を記録する
		yaml.set("Coins", coins);

		//最後にいたアスレの名前を記録する
		yaml.set("Current parkour", currentParkour != null ? currentParkour.name : null);

		//最後にプレイしていたアスレの名前を記録する
		yaml.set("Last played parkour", parkourPlayingNow != null ? parkourPlayingNow.name : null);

		//最後にアスレをプレイし始めた時間を記録する
		yaml.set("Time to start playing", timeToStartPlaying);

		//クリア済みのアスレの名前リストを記録する
		yaml.set("Cleared parkour names", clearedParkourNames);

		//クリエイティブワールドのチェックポイントを記録する
		yaml.set("Creative world checkpoint", creativeWorldCheckpoint.serialize());

		heads.save(yaml);

		//全チェックポイントを記録する
		checkpoints.save(yaml);

		//セーブする
		yaml.save();
	}

}
