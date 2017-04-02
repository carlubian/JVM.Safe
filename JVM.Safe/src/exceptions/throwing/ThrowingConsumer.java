package exceptions.throwing;

@FunctionalInterface
public interface ThrowingConsumer<TParam> {
	void accept(TParam param) throws Throwable;
}
