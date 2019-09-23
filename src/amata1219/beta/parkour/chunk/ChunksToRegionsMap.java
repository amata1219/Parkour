package amata1219.beta.parkour.chunk;

import java.util.List;

import amata1219.beta.parkour.region.Region;

public class ChunksToRegionsMap<V extends Region> extends ChunksToObjectsMap<V> {

	public List<V> getAll(V region){
		return getAll(region.min, region.max);
	}

	public void putAll(V region){
		putAll(region.min, region.max, region);
	}

	public void removeAll(V region){
		removeAll(region.min, region.max, region);
	}

}
