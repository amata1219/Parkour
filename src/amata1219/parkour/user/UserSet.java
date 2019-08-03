package amata1219.parkour.user;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;

public class UserSet implements Listener {

	private static UserSet instance;

	public static UserSet getInstnace(){
		return instance != null ? instance : (instance = new UserSet());
	}

	private final Main plugin = Main.getPlugin();

	//ユーザーデータを保存するフォルダー
	public final File folder = new File(plugin.getDataFolder() + File.separator + "Users");

	private final Map<UUID, User> users = new HashMap<>();

	private UserSet(){
		//フォルダーが存在しなければ作成する
		if(!folder.exists()) folder.mkdir();

		for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
			//ファイルをコンフィグとして読み込む
			Yaml yaml = new Yaml(plugin, file);

			//コンフィグを基にユーザーを生成する
			User user = new User(yaml);

			//登録する
			users.put(user.uuid, user);
		}
	}

	public User getUser(Player player){
		return users.get(player.getUniqueId());
	}

	public User getUser(UUID uuid){
		return users.get(uuid);
	}

	@EventHandler
	public void onPlayerFirstJoin(PlayerJoinEvent event){
		UUID uuid = event.getPlayer().getUniqueId();

		//既にユーザーデータが存在するのであれば戻る
		if(users.containsKey(uuid)) return;

		//ユーザーデータコンフィグ作成する
		Yaml yaml = new Yaml(plugin, new File(folder, StringTemplate.apply("$0.yml", uuid)));

		//コンフィグを基にユーザーを生成する
		User user = new User(yaml);

		//登録する
		users.put(uuid, user);
	}

}
