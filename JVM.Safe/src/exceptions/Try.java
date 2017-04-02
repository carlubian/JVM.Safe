package exceptions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import exceptions.steps.ThenCompositionStep;
import exceptions.throwing.ThrowingAction;
import exceptions.throwing.ThrowingConsumer;
import exceptions.throwing.ThrowingFunction;
import exceptions.throwing.ThrowingSupplier;
import util.Action;
import util.Unit;

/**
 * Try exposes methods to start a function composition.
 */
public class Try {
	private Try() {}
	
	/**
	 * Start a composition with an action.
	 * @param action Action
	 * @return In-progress composition
	 */
	public static Composition<Unit> that(Action action) {
		return new Composition<>(new ThenCompositionStep<Unit, Unit>(param -> {
			action.execute();
			return Unit.Instance();
		}));
	}
	
	/**
	 * Start a composition with a throwing action.
	 * @param action Throwing Action
	 * @return In-progress composition
	 */
	public static Composition<Unit> thatUnchecking(ThrowingAction action) {
		return new Composition<>(new ThenCompositionStep<Unit, Unit>(param -> {
			try {
				action.execute();
			} catch(Throwable ex) {
				throw new RuntimeException(ex);
			}
			return Unit.Instance();
		}));
	}
	
	/**
	 * Start a composition with a consumer
	 * @param consumer Consumer
	 * @param param Parameter
	 * @return In-progress composition
	 */
	public static <TParam> Composition<Unit> that(Consumer<TParam> consumer, TParam param) {
		return new Composition<>(new ThenCompositionStep<TParam, Unit>(_param -> {
			consumer.accept(param);
			return Unit.Instance();
		}));
	}
	
	/**
	 * Start a composition with a throwing consumer
	 * @param consumer Throwing Consumer
	 * @param param Parameter
	 * @return In-progress composition
	 */
	public static <TParam> Composition<Unit> thatUnchecking(ThrowingConsumer<TParam> consumer, TParam param) {
		return new Composition<>(new ThenCompositionStep<TParam, Unit>(_param -> {
			try {
				consumer.accept(param);
			} catch(Throwable ex) {
				throw new RuntimeException(ex);
			}
			return Unit.Instance();
		}));
	}
	
	/**
	 * Start a composition with a supplier.
	 * @param supplier Supplier
	 * @return In-progress composition
	 */
	public static <TResult> Composition<TResult> that(Supplier<TResult> supplier) {
		return new Composition<>(new ThenCompositionStep<Unit, TResult>(param -> supplier.get()));
	}
	
	/**
	 * Start a composition with a throwing supplier.
	 * @param supplier Throwing Supplier
	 * @return In-progress composition
	 */
	public static <TResult> Composition<TResult> thatUnchecking(ThrowingSupplier<TResult> supplier) {
		return new Composition<>(new ThenCompositionStep<Unit, TResult>(param -> {
			try {
				return supplier.get();
			} catch(Throwable ex) {
				throw new RuntimeException(ex);
			}
		}));
	}
	
	/**
	 * Start a composition with a function.
	 * @param function Function
	 * @param param Parameter
	 * @return In-progress composition
	 */
	public static <TParam, TResult> Composition<TResult> that(Function<TParam, TResult> function, TParam param) {
		return new Composition<>(new ThenCompositionStep<Unit, TResult>(_param -> function.apply(param)));
	}
	
	/**
	 * Start a composition with a throwing function.
	 * @param function Throwing Function
	 * @param param Parameter
	 * @return In-progress composition
	 */
	public static <TParam, TResult> Composition<TResult> thatUnchecking(ThrowingFunction<TParam, TResult> function, TParam param) {
		return new Composition<>(new ThenCompositionStep<Unit, TResult>(_param -> { 
			try {
				return function.apply(param);
			} catch(Throwable ex) {
				throw new  RuntimeException(ex);
			}
		}));
	}
}
