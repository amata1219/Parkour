package amata1219.parkour;

public interface Effect<T> {

	void applyTo(T value);

	default Effect<T> combine(Effect<T> effect){
		return value -> {
			this.applyTo(value);
			effect.applyTo(value);
		};
	}

}
