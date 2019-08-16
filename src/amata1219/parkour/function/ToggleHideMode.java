package amata1219.parkour.function;

import java.util.HashSet;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import amata1219.amalib.schedule.Sync;
import amata1219.amalib.string.message.MessageColor;
import amata1219.parkour.Main;
import amata1219.parkour.user.User;
import amata1219.parkour.user.Users;
import amata1219.parkour.user.UserSetting;

public class ToggleHideMode {

	private static ToggleHideMode instance;

	public static ToggleHideMode getInstance(){
		return instance != null ? instance : (instance = new ToggleHideMode());
	}

	private final Users users = Users.getInstnace();

	//非表示モードの使用者
	private final HashSet<User> hideModeUsers = new HashSet<>();

	//クールダウン中のユーザー
	private final HashSet<User> cooldownUsers = new HashSet<>();

	private ToggleHideMode(){

	}

	//プレイヤーがログインした時
	public void onPlayerJoin(Player player){
		//ログインしたプレイヤーを全非表示モードの使用者から非表示にする
		forEachHideModeUser((user) -> hide(user, player));

		//ユーザーを取得する
		User user = users.getUser(player);

		//ユーザー設定を取得する
		UserSetting setting = user.setting;

		//非表示モードを使用する設定であればそれを適用する
		if(setting.hideMode)
			applyHideMode(user);
	}

	//プレイヤーがログアウトした時
	public void onPlayerQuit(Player player){
		//CraftPlayer#removeDisconnectingPlayer(Player)が同等の機能を持っている
		//ログアウトしたプレイヤーを全非表示モードの使用者に表示する
		//forEachHideModeUser((user) -> show(user, player));

		//ユーザーを取得する
		User user = users.getUser(player);

		//非表示モードの使用者リストから削除する
		hideModeUsers.remove(user);
	}

	public void change(User user){
		//プレイヤーを取得する
		Player player = user.asBukkitPlayer();

		//クールダウン中なら戻る
		if(cooldownUsers.contains(user)){
			MessageColor.color("&c-Operation blocked-&7 @ &c-Input is too fast").displayOnActionBar(player);
			return;
		}

		//ユーザー設定を取得する
		UserSetting setting = user.setting;

		//設定を切り替える
		boolean isHideMode = setting.hideMode = !setting.hideMode;

		//非表示モードであれば全プレイヤーを非表示にする
		if(isHideMode) applyHideMode(user);

		//そうでなければ全プレイヤーを表示する
		else applyShowMode(user);

		//クールダウンさせる
		cooldownUsers.add(user);

		//0.5秒後にクールダウンを完了させる
		Sync.define(() -> cooldownUsers.remove(user)).executeLater(10);
	}

	public boolean isHideMode(User user){
		return hideModeUsers.contains(user);
	}

	//targetをplayerから非表示にする
	private void show(Player player, Player target){
		player.showPlayer(Main.getPlugin(), target);
	}

	//targetをplayerに表示する
	private void hide(Player player, Player target){
		player.hidePlayer(Main.getPlugin(), target);
	}

	private void applyShowMode(User user){
		//全プレイヤーを表示する
		forEachOnlinePlayer((target) -> show(user.asBukkitPlayer(), target));

		hideModeUsers.remove(user);
	}

	private void applyHideMode(User user){
		//全プレイヤーを非表示にする
		forEachOnlinePlayer((target) -> hide(user.asBukkitPlayer(), target));

		hideModeUsers.add(user);
	}

	//全プレイヤーに対して処理をする
	private void forEachOnlinePlayer(Consumer<Player> processing){
		Bukkit.getOnlinePlayers().forEach(processing::accept);
	}

	//全非表示モード使用者に対して処理をする
	private void forEachHideModeUser(Consumer<Player> processing){
		hideModeUsers.stream().map(User::asBukkitPlayer).forEach(processing::accept);
	}

}
