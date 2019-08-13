package amata1219.parkour.parkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

	//上位10件の記録
	public final List<Tuple<UUID, String>> topTenRecords = new ArrayList<>(10);

	private final List<UUID> invalidRecorders = new ArrayList<>();

	public Records(Yaml yaml){
		if(!yaml.isConfigurationSection("Records")){
			records = new HashMap<>();
			return;
		}

		ConfigurationSection recorderSection = yaml.getConfigurationSection("Records");

		Set<String> recorderUUIDs = recorderSection.getKeys(false);
		records = new HashMap<>(recorderUUIDs.size());

		for(String recorderUUID : recorderUUIDs){
			//UUIDに変換する
			UUID uuid = UUID.fromString(recorderUUID);

			//タイムに変換する
			long time = Long.parseLong(yaml.getString(recorderUUID));

			records.put(uuid, time);
		}

		sort();
	}

	public boolean record(UUID uuid, long time){
		if(records.getOrDefault(uuid, Long.MAX_VALUE) <= time) return false;

		records.put(uuid, time);
		return true;
	}

	public void deleteInvalidRecord(UUID uuid){
		//validate uuid... records.remove(uuid); invalidRecorders.add(uuid); sort();
	}

	public void sort(){
		//非同期で実行する
		Async.define(() -> {
			List<Entry<UUID, Long>> list = new ArrayList<>(records.entrySet());

			//記録を昇順ソートする
			list.sort(Entry.comparingByValue());

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
		for(UUID invalidRecorder : invalidRecorders) yaml.set(StringTemplate.apply("Records.$0", invalidRecorder), null);
	}

}
