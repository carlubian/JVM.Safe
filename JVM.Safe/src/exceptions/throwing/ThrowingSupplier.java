package exceptions.throwing;

@FunctionalInterface
public interface ThrowingSupplier<TResult> {
	TResult get() throws Throwable;
}
