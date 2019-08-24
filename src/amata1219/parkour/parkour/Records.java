package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Map.Entry;
import java.util.Set;

import amata1219.amalib.schedule.Async;
import amata1219.amalib.string.StringTemplate;
import amata1219.amalib.tuplet.Tuple;
import amata1219.amalib.yaml.Yaml;
import amata1219.parkour.util.TimeFormat;

public class Records {

	//全記録
	private final Map<UUID, Long> records;

	//上位10件の記録(非同期でリストを操作する為スレッドセーフなリストにしている)
	public final List<Tuple<UUID, String>> topTenRecords = new CopyOnWriteArrayList<>();

	//無効な記録の保有者リスト
	private final List<UUID> holdersOfInvalidRecords = new ArrayList<>();

	public Records(Yaml yaml){
		if(!yaml.isConfigurationSection("Records")){
			records = new HashMap<>();
			return;
		}

		ConfigurationSection recorderSection = yaml.getConfigurationSection("Records");

		Set<String> recorders = recorderSection.getKeys(false);
		records = new HashMap<>(recorders.size());

		for(String recorder : recorders){
			//UUIDに変換する
			UUID uuid = UUID.fromString(recorder);

			//タイムに変換する
			long time = recorderSection.getLong(recorder);

			records.put(uuid, time);
		}

		sort();
	}

	public boolean record(UUID uuid, long time){
		if(records.getOrDefault(uuid, Long.MAX_VALUE) <= time) return false;

		records.put(uuid, time);
		return true;
	}

	public void removeRecord(UUID uuid){
		records.remove(uuid);
		removedRecorders.add(uuid);
		sort();
	}

	public void sort(){
		//非同期で実行する
		Async.define(() -> {
			List<Entry<UUID, Long>> list = new ArrayList<>(records.entrySet());

			//記録を昇順にソートする
			list.sort(Entry.comparingByValue());

			topTenRecords.clear();

			//最大で上位10件の記録をリストに追加する
			for(int index = 0; index < Math.min(10, records.size()); index++){
				//ソート済みリストから記録を取得する
				Entry<UUID, Long> record = list.get(index);

				UUID uuid = record.getKey();

				//記録をフォーマットして追加する
				topTenRecords.add(new Tuple<>(uuid, TimeFormat.format(records.get(uuid))));
			}
		}).execute();
	}

	public void save(Yaml yaml){
		//レコードを記録する
		for(Entry<UUID, Long> recordEntry : records.entrySet()) yaml.set(StringTemplate.apply("Records.$0", recordEntry.getKey()) , recordEntry.getValue());

		//不正なレコードを削除する
		for(UUID removedRecorder : removedRecorders) yaml.set(StringTemplate.apply("Records.$0", removedRecorder), null);
	}

}
