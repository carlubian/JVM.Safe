package exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import exceptions.steps.CompositionStep;
import exceptions.steps.OtherwiseCompositionStep;
import exceptions.steps.ThenCompositionStep;
import exceptions.throwing.ThrowingAction;
import exceptions.throwing.ThrowingConsumer;
import exceptions.throwing.ThrowingFunction;
import exceptions.throwing.ThrowingSupplier;
import util.Action;
import util.Resources;
import util.Unit;

/**
 * Represents an in-progress composition. This can be
 * finalized eagerly or lazily.
 *
 * @param <TCurrent> Current type of the composition.
 */
public class Composition<TCurrent> {

	private List<CompositionStep> _steps = new ArrayList<>();
	
	protected Composition(CompositionStep step) {
		_steps = new ArrayList<>();
		_steps.add(step);
	}
	
	protected Composition(List<CompositionStep> steps) {
		_steps = steps;
	}
	
	/**
	 * Append an action to this composition.
	 * @param action Action
	 * @return In-progress composition
	 */
	public Composition<Unit> then(Action action) {
		_steps.add(new ThenCompositionStep<TCurrent, Unit>(param -> {
			action.execute();
			return Unit.Instance();
		}));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Append a throwing action to this composition.
	 * @param action Throwing Action
	 * @return In-progress composition
	 */
	public Composition<Unit> thenUncheck(ThrowingAction action) {
		_steps.add(new ThenCompositionStep<TCurrent, Unit>(param -> {
			try {
				action.execute();
			} catch(Throwable ex) {
				throw new RuntimeException(ex);
			}
			return Unit.Instance();
		}));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Append a consumer to this composition.
	 * @param consumer Consumer
	 * @return In-progress composition
	 */
	public Composition<Unit> then(Consumer<TCurrent> consumer) {
		_steps.add(new ThenCompositionStep<TCurrent, Unit>(param -> {
			consumer.accept(param);
			return Unit.Instance();
		}));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Append a throwing consumer to this composition.
	 * @param consumer Throwing Consumer
	 * @return In-progress composition
	 */
	public Composition<Unit> thenUncheck(ThrowingConsumer<TCurrent> consumer) {
		_steps.add(new ThenCompositionStep<TCurrent, Unit>(param -> {
			try {
				consumer.accept(param);
			} catch(Throwable ex) {
				throw new RuntimeException();
			}
			return Unit.Instance();
		}));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Append a supplier to this composition.
	 * @param supplier Supplier
	 * @return In-progress composition
	 */
	public <TResult> Composition<TResult> then(Supplier<TResult> supplier) {
		_steps.add(new ThenCompositionStep<TCurrent, TResult>(param -> supplier.get()));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Append a throwing supplier to this composition.
	 * @param supplier Throwing Supplier
	 * @return In-progress composition
	 */
	public <TResult> Composition<TResult> thenUncheck(ThrowingSupplier<TResult> supplier) {
		_steps.add(new ThenCompositionStep<TCurrent, TResult>(param -> {
			try {
				return supplier.get();
			} catch(Throwable ex) {
				throw new RuntimeException(ex);
			}
		}));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Append a function to this composition.
	 * @param function Function
	 * @return In-progress composition
	 */
	public <TResult> Composition<TResult> then(Function<TCurrent, TResult> function) {
		_steps.add(new ThenCompositionStep<TCurrent, TResult>(param -> function.apply(param)));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Append a throwing function to this composition.
	 * @param function Throwing Function
	 * @return In-progress composition
	 */
	public <TResult> Composition<TResult> thenUncheck(ThrowingFunction<TCurrent, TResult> function) {
		_steps.add(new ThenCompositionStep<TCurrent, TResult>(param -> {
			try {
				return function.apply(param);
			} catch(Throwable ex) {
				throw new RuntimeException(ex);
			}
			
		}));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Execute an action if the composition has
	 * failed at this point.The action will be
	 * ignored if the composition is successful.
	 * @param action Action
	 * @return In-progress composition
	 */
	public Composition<TCurrent> otherwise(Action action) {
		_steps.add(new OtherwiseCompositionStep<TCurrent>(action));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Execute a throwing action if the composition has
	 * failed at this point.The action will be
	 * ignored if the composition is successful.
	 * @param action Throwing Action
	 * @return In-progress composition
	 */
	public Composition<TCurrent> otherwiseUncheck(ThrowingAction action) {
		_steps.add(new OtherwiseCompositionStep<TCurrent>(() -> {
			try {
				action.execute();
			} catch(Throwable ex) {
				throw new RuntimeException();
			}
		}));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Execute a string consumer if the composition
	 * has failed at this point. The consumer will be
	 * ignored if the composition is successful.
	 * @param consumer Consumer
	 * @return In-progress composition
	 */
	public Composition<TCurrent> otherwise(Consumer<String> consumer) {
		_steps.add(new OtherwiseCompositionStep<TCurrent>(consumer));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Execute a string throwing consumer if the composition
	 * has failed at this point. The consumer will be
	 * ignored if the composition is successful.
	 * @param consumer Throwing Consumer
	 * @return In-progress composition
	 */
	public Composition<TCurrent> otherwiseUncheck(ThrowingConsumer<String> consumer) {
		_steps.add(new OtherwiseCompositionStep<TCurrent>((param) -> {
			try {
				consumer.accept(param);
			} catch(Throwable ex) {
				throw new RuntimeException(ex);
			}
		}));
		
		return new Composition<>(_steps);
	}
	
	/**
	 * Finalize the composition and execute it eagerly. The
	 * result will be returned as an instance of Either.
	 * @return Either
	 */
	@SuppressWarnings("unchecked")
	public Either<TCurrent> now() {
		Either<Object> tmp = new Success<>(null);
		
		for(CompositionStep step : _steps)
			tmp = step.invoke(tmp);
		
		if(tmp.failed())
			return new Failure<TCurrent>(tmp.errorOrElse(Resources.MISSING_ERROR_MESSAGE));
		
		return new Success<>((TCurrent)tmp.getOrElse(null));
	}
	
	/**
	 * Finalize the composition and return an instance of Lazy
	 * ready to be invoked at a later time.
	 * @return Lazy
	 */
	public Lazy<TCurrent> later() {
		@SuppressWarnings("unchecked")
		Supplier<Either<TCurrent>> supplier = () -> {
			Either<Object> tmp = new Success<>(null);
			
			for(CompositionStep step : _steps)
				tmp = step.invoke(tmp);
			
			if(tmp.failed())
				return new Failure<TCurrent>(tmp.errorOrElse(Resources.MISSING_ERROR_MESSAGE));
			
			return new Success<>((TCurrent)tmp.getOrElse(null));
		};
		
		return new Lazy<>(supplier);
	}
}
