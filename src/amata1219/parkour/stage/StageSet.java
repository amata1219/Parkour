package amata1219.parkour.stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.Main;
import amata1219.parkour.parkour.Parkour;

public class StageSet {

	private static StageSet instance;

	public static StageSet getInstance(){
		return instance != null ? instance : (instance = new StageSet());
	}

	private final Main plugin = Main.getPlugin();

	//ステージデータを保存するフォルダー
	public final File folder = new File(plugin.getDataFolder() + File.separator + "Stages");

	private final Map<String, Stage> stages = new HashMap<>();

	//カテゴリーとステージをバインドするマップ
	private final Map<StageCategory, List<Stage>> categoriesToStagesMap = new HashMap<>();

	//アスレ名とステージをバインドするマップ
	private final Map<String, Stage> parkourNamesToStagesMap = new HashMap<>();

	private StageSet(){
		//フォルダーが存在しなければ作成する
		if(!folder.exists()) folder.mkdir();

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

	public void registerStage(Stage stage){
		stages.put(stage.name, stage);

		//カテゴリーのステージリストに追加する
		categoriesToStagesMap.get(stage.category).add(stage);

		//ステージ内のアスレの名前とステージをバインドする
		for(String parkourName : stage.parkourNames) parkourNamesToStagesMap.put(parkourName, stage);
	}

	public void unregisterStage(Stage stage){
		//ステージ内のアスレの名前とステージをアンバインドする
		for(Entry<String, Stage> entry : parkourNamesToStagesMap.entrySet()) if(stage.equals(entry.getValue()))
			parkourNamesToStagesMap.remove(entry.getKey());

		//カテゴリーのステージリストから削除する
		categoriesToStagesMap.get(stage.category).remove(stage);

		stages.remove(stage.name);
	}

	public void unregisterStage(String stageName){
		if(stages.containsKey(stageName)) unregisterStage(stages.get(stageName));
	}

	public List<Stage> getStages(StageCategory category){
		return categoriesToStagesMap.get(category);
	}

	public Stage getStage(Parkour parkour){
		return getStage(parkour);
	}

	public Stage getStage(String parkourName){
		return parkourNamesToStagesMap.get(parkourName);
	}

	public Yaml makeYaml(String stageName){
		return new Yaml(plugin, new File(folder, StringTemplate.apply("$0.yml", stageName)), "stage.yml");
	}

}
