package exceptions.steps;

import exceptions.Either;

public interface CompositionStep {
	Either<Object> invoke(Either<Object> param);
}
