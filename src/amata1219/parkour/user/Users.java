package amata1219.parkour.user;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.amalib.listener.PlayerJoinListener;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;

public class Users implements PlayerJoinListener {

	private static Users instance;

	public static void load(){
		instance = new Users();
	}

	public static Users getInstnace(){
		return instance;
	}

	private final Main plugin = Main.getPlugin();

	//ユーザーデータを保存するフォルダー
	public final File folder = new File(plugin.getDataFolder() + File.separator + "Users");

	private final Map<UUID, User> users = new HashMap<>();

	private Users(){
		//フォルダーが存在しなければ作成する
		if(!folder.exists()) folder.mkdirs();

		for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
			//ファイルをコンフィグとして読み込む
			Yaml yaml = new Yaml(plugin, file, "user.yml");

			//コンフィグを基にユーザーを生成する
			User user = new User(yaml);

			//登録する
			users.put(user.uuid, user);
		}
	}

	public List<User> getOnlineUsers(){
		return Bukkit.getOnlinePlayers().stream().map(this::getUser).collect(Collectors.toList());
	}

	public User getUser(Player player){
		return users.get(player.getUniqueId());
	}

	public User getUser(UUID uuid){
		return users.get(uuid);
	}

	public boolean containsUser(Player player){
		return containsUser(player.getUniqueId());
	}

	public boolean containsUser(UUID uuid){
		return users.containsKey(uuid);
	}

	public Yaml makeYaml(UUID uuid){
		return new Yaml(plugin, new File(folder, StringTemplate.apply("$0.yml", uuid)), "user.yml");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		UUID uuid = event.getPlayer().getUniqueId();

		//既にユーザーデータが存在するのであれば戻る
		if(users.containsKey(uuid)) return;

		//ユーザーデータコンフィグ作成する
		Yaml yaml = makeYaml(uuid);

		//コンフィグを基にユーザーを生成する
		User user = new User(yaml);

		//登録する
		users.put(uuid, user);
	}

}