package amata1219.parkour.stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;

public class Stages {

	private static Stages instance;

	public static void load(){
		instance = new Stages();
	}

	public static Stages getInstance(){
		return instance;
	}

	private final Main plugin = Main.getPlugin();

	//ステージデータを保存するフォルダー
	public final File folder = new File(plugin.getDataFolder() + File.separator + "Stages");

	private final Map<String, Stage> stages = new HashMap<>();

	//カテゴリーとステージをバインドするマップ
	private final Map<StageCategory, List<Stage>> categoriesToStagesMap = new HashMap<>();

	//アスレ名とステージをバインドするマップ
	private final Map<String, Stage> parkourNamesToStagesMap = new HashMap<>();

	private Stages(){
		//フォルダーが存在しなければ作成する
		if(!folder.exists()) folder.mkdirs();

		//各カテゴリ用にリストを用意する
		for(StageCategory category : StageCategory.values()) categoriesToStagesMap.put(category, new ArrayList<>());

		for(File file : Optional.ofNullable(folder.listFiles()).orElse(new File[0])){
			//ファイルをコンフィグとして読み込む
			Yaml yaml = new Yaml(plugin, file);

			//ファイルを基にステージを作成する
			Stage stage = new Stage(yaml);

			//ステージを登録する
			registerStage(stage);
		}
	}

	public void saveAll(){
		//stages.values().forEach(Stage::save);
	}

	public boolean existsFile(String stageName){
		return new File(folder, stageName).exists();
	}

	public void registerStage(Stage stage){
		stages.put(stage.name, stage);

		//カテゴリーのステージリストに追加する
		categoriesToStagesMap.get(stage.category).add(stage);

		//ステージ内のアスレの名前とステージをバインドする
		for(String parkourName : stage.parkourNames) addParkour(stage, parkourName);
	}

	public void unregisterStage(Stage stage){
		//ステージ内のアスレの名前とステージをアンバインドする
		for(String parkourName : stage.parkourNames) removeParkour(parkourName);

		//カテゴリーのステージリストから削除する
		categoriesToStagesMap.get(stage.category).remove(stage);

		stages.remove(stage.name);
	}

	public void unregisterStage(String stageName){
		if(stages.containsKey(stageName)) unregisterStage(stages.get(stageName));
	}

	public List<Stage> getStages(){
		List<Stage> stages = new ArrayList<>(this.stages.size());

		//全ステージをリストに追加する
		this.stages.values().forEach(stages::add);

		return stages;
	}

	public Stage getStage(String stageName){
		return stages.get(stageName);
	}

	public boolean containsStage(Stage stage){
		return containsStage(stage.name);
	}

	public boolean containsStage(String stageName){
		return stages.containsKey(stageName);
	}

	public List<Stage> getStagesByCategory(StageCategory category){
		return categoriesToStagesMap.get(category);
	}

	public Stage getStageByParkour(Parkour parkour){
		return getStageByParkour(parkour);
	}

	public Stage getStageByParkourName(String parkourName){
		return parkourNamesToStagesMap.get(parkourName);
	}

	public void addParkour(Stage stage, String parkourName){
		stage.parkourNames.add(parkourName);
		parkourNamesToStagesMap.put(parkourName, stage);
	}

	public void addParkour(String stageName, String parkourName){
		addParkour(getStage(stageName), parkourName);
	}

	public void removeParkour(String parkourName){
		//ステージを取得する
		Stage stage = parkourNamesToStagesMap.get(parkourName);

		//ステージからアスレを削除する
		removeParkour(parkourName);

		stage.parkourNames.remove(parkourName);
	}

	public Yaml makeYaml(String stageName){
		return new Yaml(plugin, new File(folder, StringTemplate.apply("$0.yml", stageName)), "stage.yml");
	}

}
