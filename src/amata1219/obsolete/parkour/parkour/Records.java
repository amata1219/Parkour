package amata1219.obsolete.parkour.parkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.Map.Entry;
import java.util.TreeMap;

import amata1219.beta.parkour.util.TimeFormat;
import amata1219.parkour.schedule.Async;
import amata1219.parkour.tuplet.Tuple;
import graffiti.Maybe;
import graffiti.Yaml;

public class Records {

	public static void main(String[] $){
		TreeMap<Integer, Integer> map = new TreeMap<>();
		map.put(100, 1);
		map.put(1, 2);
		map.put(345, 3);
		map.put(98, 4);
		map.put(12, 5);
		map.
		map.forEach((k, v) -> System.out.println(k + "," + v.toString()));
	}

	/*
	 * Records:
	 * - "uuid:time"
	 * - "uuid:time"
	 *
	 */

	private final Map<UUID, Long> records = new HashMap<>();
	private final SortedMap<Long, UUID>
	//上位10件の記録(非同期でリストを操作する為スレッドセーフなリストにしている)
	public final List<Tuple<UUID, String>> bestRecords = new CopyOnWriteArrayList<>();

	public Records(Yaml yaml){
		yaml.getStringList("Records").stream()
		.map(text -> text.split(","))
		.forEach(data -> records.put(UUID.fromString(data[0]), Long.parseLong(data[1])));

		sortAsync();
	}

	public void tryRcord(UUID uuid, long time){
		personalBest(uuid).ifJustOrElse(best -> {
			if(best > time) records.put(uuid, time);
		}, () -> Long.MAX_VALUE);
	}

	public boolean containsRecord(UUID uuid){
		return records.containsKey(uuid);
	}

	public Maybe<Long> personalBest(UUID uuid){
		return Maybe.unit(records.get(uuid));
	}

	public void withdrawRecord(UUID uuid){
		records.remove(uuid);
		sortAsync();
	}

	public void runForBestRecords(BiConsumer<Integer, Entry<>>)

	public void sortAsync(){
		Async.define(() -> {
			List<Entry<UUID, Long>> list = new ArrayList<>(records.entrySet());

			//記録を昇順にソートする
			list.sort(Entry.comparingByValue());

			bestRecords.clear();

			//最大で上位10件の記録をリストに追加する
			for(int index = 0; index < Math.min(10, records.size()); index++){
				//ソート済みリストから記録を取得する
				Entry<UUID, Long> record = list.get(index);

				UUID uuid = record.getKey();

				//記録をフォーマットして追加する
				bestRecords.add(new Tuple<>(uuid, TimeFormat.format(records.get(uuid))));
			}
		}).execute();
	}

	public void save(Yaml yaml){
		//レコードを記録する
		for(Entry<UUID, Long> recordEntry : records.entrySet()) yaml.set("Records." + recordEntry.getKey() , recordEntry.getValue());
	}

}
