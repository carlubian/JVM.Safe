package exceptions.throwing;

@FunctionalInterface
public interface ThrowingFunction<TParam, TResult> {
	TResult apply(TParam param) throws Throwable;
}
