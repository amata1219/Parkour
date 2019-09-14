package amata1219.beta.parkour.serialize;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Deserializer {

	private final String[] data;
	private final Function<String, ?>[] mappers;
	private final Class<?>[] types;

	public static Deserializer stream(String text){
		return new Deserializer(text);
	}

	@SuppressWarnings("unchecked")
	public Deserializer(String text){
		data = text.split(",");
		mappers = new Function[data.length];
		types = new Class<?>[data.length];
	}

	public Deserializer map(Function<String, ?> mapper, int startInclusive, int endInclusive){
		return map(mapper, null, startInclusive, endInclusive);
	}

	public Deserializer map(Function<String, ?> mapper, int... indexes){
		return map(mapper, null, indexes);
	}

	public Deserializer map(Function<String, ?> mapper, Class<?> type, int startInclusive, int endInclusive){
		return map(mapper, type, IntStream.rangeClosed(startInclusive, endInclusive).toArray());
	}

	public Deserializer map(Function<String, ?> mapper, Class<?> type, int... indexes){
		Arrays.stream(indexes).forEach(index -> {
			mappers[index] = mapper;
			types[index] = type;
		});
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T deserializeTo(Class<T> type){
		Object[] arguments = IntStream.range(0, data.length)
				.mapToObj(i -> mappers[i].apply(data[i]))
				.toArray();

		for(int i = 0; i < types.length; i++)
			if(types[i] == null) types[i] = arguments[i].getClass();

		try{
			Constructor<?> constructor = type.getDeclaredConstructor(types);
			constructor.setAccessible(true);
			return (T) constructor.newInstance(arguments);
		}catch(NullPointerException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e){
			e.printStackTrace();
		}

		return null;
	}

}
