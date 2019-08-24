package amata1219.parkour.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import amata1219.amalib.location.ImmutableLocation;
import amata1219.amalib.string.message.Localizer;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.function.hotbar.ControlFunctionalItem;
import amata1219.parkour.function.hotbar.ItemType;
import amata1219.parkour.parkour.Parkour;
import amata1219.parkour.parkour.ParkourRegion;
import amata1219.parkour.parkour.Parkours;

public class User {

	//UUID
	public final UUID uuid;

	//Updateのランク
	private int updateRank;

	//Extendのランク
	private int extendRank;

	//コイン
	private int coins;

	//今いるアスレ
	private Parkour parkourWithNow;

	//今プレイ中のアスレ
	public Parkour parkourPlayingNow;

	//これやるぐらいならSetCheckpointListenerの為にも今いるエリアを保持した方が良い
	//今チェックエリア内にいるかどうか
	public boolean onCheckArea;

	//今いるチェックエリア
	private ParkourRegion checkAreaWithNow;

	//プレイし始めた時間(ミリ秒)
	public long timeToStartPlaying;

	//アスレのプレイ開始からログアウトまでの経過時間(ミリ秒)
	public long timeElapsed;

	//各アスレのチェックポイント
	public final Checkpoints checkpoints;

	//個人設定
	public final UserSetting setting;

	//クリア済みのアスレの名前リスト
	public final Set<String> clearedParkourNames;

	//クリエイティブワールドでのチェックポイント
	public ImmutableLocation creativeWorldCheckpoint;

	//購入したヘッドのセット
	public final UserHats hats;

	//スコアボードの管理インスタンス
	public StatusBoard statusBoard;

	//InventoryUIの管理インスタンス
	public InventoryUserInterfaces inventoryUserInterfaces;

	//プレイヤーの言語設定に対応したテキストを選び加工するインスタンス
	public Localizer localizer;

	public User(Yaml yaml){
		//ファイル名に基づきUUIDを生成し代入する
		this.uuid = UUID.fromString(yaml.name);

		//Updateランクを取得する
		updateRank = yaml.getInt("Update rank");

		//Extendランクを取得する
		extendRank = yaml.getInt("Extend rank");

		//コイン数を取得する
		coins = yaml.getInt("Coins");

		Parkours parkours = Parkours.getInstance();

		//最後にいたアスレを取得する
		parkourWithNow = parkours.getParkour("Last parkour");

		//最後に遊んでいたアスレを取得する
		parkourPlayingNow = parkours.getParkour("Last played parkour");

		//このフラグどうにかしたい
		//今チェックエリア内にいるかどうか取得する
		onCheckArea = yaml.getBoolean("On check area");

		//タイムアタックを始めてからの経過時間を取得する
		timeElapsed = yaml.getLong("Time elapsed");

		checkpoints = new Checkpoints(yaml);

		//個人設定はYamlに基づき生成する
		setting = new UserSetting(yaml);

		//クリア済みのアスレ名リストを取得してセットでラップする
		clearedParkourNames = new HashSet<>(yaml.getStringList("Cleared parkour names"));

		//データを基に座標を作成する
		creativeWorldCheckpoint = ImmutableLocation.deserialize(yaml.getString("Creative world checkpoint"));

		//購入済みのスカルのIDをUUIDに変換したリストを作成する
		hats = new UserHats(this, yaml);
	}

	//このユーザーに対応したプレイヤーを取得する
	public Player asBukkitPlayer(){
		return Bukkit.getPlayer(uuid);
	}

	public int updateRank(){
		return updateRank;
	}

	public void incrementUpdateRank(){
		updateRank++;
	}

	public int extendRank(){
		return extendRank;
	}

	public void incrementExtendRank(){
		extendRank++;
	}

	public int coins(){
		return coins;
	}

	//指定数だけ所持コイン数を増やす
	public void depositCoins(int coins){
		this.coins += coins;

		if(statusBoard != null) statusBoard.updateCoins();
	}

	//指定数だけ所持コイン数を減らす
	public void withdrawCoins(int coins){
		this.coins = Math.max(this.coins - coins, 0);

		if(statusBoard != null) statusBoard.updateCoins();
	}

	//今アスレ内にいるかどうか
	public boolean isInParkour(){
		return parkourWithNow != null;
	}

	//今いるアスレを返す
	public Optional<Parkour> parkourWithNow(){
		return Optional.ofNullable(parkourWithNow);
	}

	//今いるアスレをセットする
	public void setParkourWithNow(Parkour parkour){
		this.parkourWithNow = parkour;
	}

	//今アスレをプレイ中かどうか
	public boolean isPlayingParkour(){
		return parkourPlayingNow != null;
	}

	//今プレイ中のアスレを返す
	public Optional<Parkour> parkourPlayingNow(){
		return Optional.ofNullable(parkourPlayingNow);
	}

	//今プレイ中のアスレをセットする
	public void setParkourPlayingNow(Parkour parkour){
		this.parkourPlayingNow = parkour;
	}

	//アスレから退出する
	public void exitParkour(){
		parkourWithNow().ifPresent(parkour -> {
			//今いるアスレから退出する
			parkour.exit(this);

			parkourWithNow = parkourPlayingNow = null;

			//通知アイテムを更新する
			ControlFunctionalItem.updateSlot(asBukkitPlayer(), ItemType.CHERCKPOINT_TELEPORTER);
		});
	}

	//今チェックエリア内にいるかどうか
	public boolean isInCheckArea(){
		return checkAreaWithNow != null;
	}

	//今いるアスレを返す
	public Optional<ParkourRegion> checkAreaWithNow(){
		return Optional.ofNullable(checkAreaWithNow);
	}

	public Optional<StatusBoard> statusBoard(){
		return Optional.ofNullable(statusBoard);
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
		yaml.set("Last parkour", parkourWithNow != null ? parkourWithNow.name : null);

		//最後にプレイしていたアスレの名前を記録する
		yaml.set("Last played parkour", parkourPlayingNow != null ? parkourPlayingNow.name : null);

		//チェックエリア内にいたかどうか記録する
		yaml.set("On check area", onCheckArea);

		//タイムアタック中であれば経過時間を記録し、そうでなければ削除する
		yaml.set("Time elapsed", timeElapsed > 0 ? timeElapsed : null);

		//クリア済みのアスレの名前リストを記録する
		yaml.set("Cleared parkour names", clearedParkourNames.stream().collect(Collectors.toList()));

		//クリエイティブワールドのチェックポイントを記録する
		yaml.set("Creative world checkpoint", creativeWorldCheckpoint.serialize());

		hats.save(yaml);

		//全チェックポイントを記録する
		checkpoints.save(yaml);

		setting.save(yaml);

		//セーブする
		yaml.save();
	}

}
