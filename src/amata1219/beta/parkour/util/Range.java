package amata1219.beta.parkour.util;

public class Range {

	public static <N extends Number & Comparable<N>> N limit(N min, N max, N value){
		return value.compareTo(min) == -1 ? min : (value.compareTo(max) == 1 ? max : value);
	}

}
