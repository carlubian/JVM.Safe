package exceptions;

/**
 * Represents a successful Either
 *
 * @param <T>
 */
public class Success<T> extends Either<T> {

	public Success(T value) {
		_value = value;
	}
	
	@Override
	public boolean succeeded() {
		return true;
	}

	@Override
	public boolean failed() {
		return false;
	}

	@Override
	public T getOrElse(T other) {
		return _value;
	}

	@Override
	public String errorOrElse(String other) {
		return other;
	}

}
