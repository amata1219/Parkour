package graffiti;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Maybe<T> {

	static final Nothing<?> EMPTY = new Nothing<>();

	@SuppressWarnings("unchecked")
	public static <T> Nothing<T> empty(){
		return (Nothing<T>) EMPTY;
	}	
	
	public static <T> Maybe<T> unit(T value){
		return value != null ? new Just<>(value) : empty();
	}

	default <U> Maybe<U> flatBind(Function<T, Maybe<U>> binder){
		return this instanceof Nothing ? empty() : binder.apply(((Just<T>) this).value);
	}
	
	default <U> Maybe<U> bind(Function<T, U> binder){
		return this instanceof Nothing ? empty() : flatBind(t -> unit(binder.apply(t)));
	}

	default Maybe<T> ifJust(Consumer<T> action){
		if(this instanceof Just) action.accept(((Just<T>) this).value);
		return this;
	}

	default Maybe<T> ifNothing(Runnable action){
		if(this instanceof Nothing) action.run();
		return this;
	}

	public static class Just<T> implements Maybe<T> {

		private final T value;

		public Just(T value){
			this.value = Objects.requireNonNull(value);
		}

	}

	public static class Nothing<T> implements Maybe<T> { }

}
