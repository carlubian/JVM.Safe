package exceptions.steps;

import java.util.function.Consumer;

import exceptions.Either;
import exceptions.ErrorManager;
import util.Action;
import util.Resources;

public class OtherwiseCompositionStep<TParam> implements CompositionStep {

	private Consumer<String> _consumer;
	
	public OtherwiseCompositionStep(Consumer<String> consumer) {
		_consumer = consumer;
	}
	
	public OtherwiseCompositionStep(Action action) {
		_consumer = str -> action.execute();
	}
	
	@Override
	public Either<Object> invoke(Either<Object> param) {
		if(param.succeeded())
			return param;
		
		ErrorManager.getDefault().attempt(_consumer, param.errorOrElse(Resources.MISSING_ERROR_MESSAGE));
		
		return param;
	}

}
