package amata1219.parkour.util;

public class Range {

	public static <N extends Number & Comparable<N>> N limit(N min, N max, N value){
		if(value.compareTo(min) == -1) return min;
		else if(value.compareTo(max) == 1) return max;
		else return value;
	}

}
