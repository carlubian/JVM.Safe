package exceptions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import util.Action;
import util.Unit;

/**
 * Error Managers attempt to execute actions
 * and functions. Those executions return an instance
 * of Either with the result.
 */
public class ErrorManager {
	private static ErrorManager _default = new ErrorManager();
	
	public static ErrorManager getDefault() {
		return _default;
	}
	
	/**
	 * Attempt to execute an action.
	 * @param action Action
	 * @return Either
	 */
	public Either<Unit> attempt(Action action) {
		try {
			action.execute();
			return new Success<>(Unit.Instance());
		} catch(Throwable ex) {
			return new Failure<>("".equals(ex.getMessage()) || ex.getMessage() == null ?
					ex.toString() : ex.getMessage());
		}
	}
	
	/**
	 * Attempt to execute a consumer.
	 * @param consumer Consumer
	 * @param param Parameter
	 * @return Either
	 */
	public <TParam> Either<Unit> attempt(Consumer<TParam> consumer, TParam param) {
		try {
			consumer.accept(param);
			return new Success<>(Unit.Instance());
		} catch(Throwable ex) {
			return new Failure<>("".equals(ex.getMessage()) || ex.getMessage() == null ?
					ex.toString() : ex.getMessage());
		}
	}
	
	/**
	 * Attempt to execute a supplier
	 * @param supplier Supplier
	 * @return Either
	 */
	public <TResult> Either<TResult> atempt(Supplier<TResult> supplier) {
		try {
			return new Success<>(supplier.get());
		} catch(Throwable ex) {
			return new Failure<>("".equals(ex.getMessage()) || ex.getMessage() == null ?
					ex.toString() : ex.getMessage());
		}
	}
	
	/**
	 * Attempt to execute a function
	 * @param function Function
	 * @param param Parameter
	 * @return Either
	 */
	public <TParam, TResult> Either<TResult> attempt(Function<TParam, TResult> function, TParam param) {
		try {
			return new Success<>(function.apply(param));
		} catch(Throwable ex) {
			return new Failure<>("".equals(ex.getMessage()) || ex.getMessage() == null ?
					ex.toString() : ex.getMessage());
		}
	}
}
