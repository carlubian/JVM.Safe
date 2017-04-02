package exceptions.steps;

import java.util.function.Function;

import exceptions.Either;
import exceptions.ErrorManager;
import exceptions.Failure;
import exceptions.Success;
import util.Resources;

public class ThenCompositionStep<TParam, TResult> implements CompositionStep {

	private Function<TParam, TResult> _func;
	
	public ThenCompositionStep(Function<TParam, TResult> func) {
		_func = func;
	}
	
	@Override
	public Either<Object> invoke(Either<Object> param) {
		if(param.failed())
			return new Failure<>(param.errorOrElse(Resources.MISSING_ERROR_MESSAGE));
		
		@SuppressWarnings("unchecked")
		Either<TResult> tmp = ErrorManager.getDefault().attempt(_func, (TParam)param.getOrElse(null));
		
		if(tmp.failed())
			return new Failure<>(tmp.errorOrElse(Resources.MISSING_ERROR_MESSAGE));
		return new Success<>((Object)tmp.getOrElse(null));
	}

}
