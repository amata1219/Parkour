package amata1219.parkour.chunk;

import java.util.List;

import amata1219.beta.parkour.region.Region;

public class ChunksToRegionsMap<V extends Region> extends ChunksToObjectsMap<V> {

	public List<V> getAll(V region){
		return getAll(region.lesserBoundaryCorner, region.greaterBoundaryCorner);
	}

	public void putAll(V region){
		putAll(region.lesserBoundaryCorner, region.greaterBoundaryCorner, region);
	}

	public void removeAll(V region){
		removeAll(region.lesserBoundaryCorner, region.greaterBoundaryCorner, region);
	}

}
