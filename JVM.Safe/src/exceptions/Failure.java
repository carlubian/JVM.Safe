package exceptions;

/**
 * Represents a failed Either
 *
 * @param <T>
 */
public class Failure<T> extends Either<T> {

	public Failure(String error) {
		_error = error;
	}
	
	@Override
	public boolean succeeded() {
		return false;
	}

	@Override
	public boolean failed() {
		return true;
	}

	@Override
	public T getOrElse(T other) {
		return other;
	}

	@Override
	public String errorOrElse(String other) {
		return _error;
	}

}
