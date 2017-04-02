package exceptions.throwing;

@FunctionalInterface
public interface ThrowingAction {
	void execute() throws Throwable;
}
