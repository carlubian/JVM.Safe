package exceptions;

import java.util.function.Supplier;

/**
 * Represents a composition ready to be
 * executed on demand.
 *
 * @param <TResult> Type of the result
 */
public class Lazy<TResult> {

	private Supplier<Either<TResult>> _supplier;
	
	protected Lazy(Supplier<Either<TResult>> supplier) {
		_supplier = supplier;
	}
	
	/**
	 * Invoke the composition and return the result
	 * as an instance of Either
	 * @return Either
	 */
	public Either<TResult> eval() {
		return _supplier.get();
	}
}
