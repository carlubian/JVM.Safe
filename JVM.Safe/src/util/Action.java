package util;

/**
 * Functional interface for methods that do not
 * require parameters and return void.
 */
@FunctionalInterface
public interface Action {
	void execute();
}
