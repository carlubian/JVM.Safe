package exceptions;

/**
 * Represents either a success or a failure.
 * Either cannot be directly instanced; Use
 * Success or Failure instead.
 *
 * @param <T> Type within
 */
public abstract class Either<T> {
	protected T _value = null;
	protected String _error = "";
	
	/**
	 * Determines whether this Either succeeded.
	 */
	public abstract boolean succeeded();
	
	/**
	 * Determines whether this Either failed.
	 */
	public abstract boolean failed();
	
	/**
	 * Gets the value within. If the Either failed
	 * or the value cannot be recovered, other will
	 * be returned instead.
	 * @param other Alternative value
	 * @return Value
	 */
	public abstract T getOrElse(T other);
	
	/**
	 * Gets the error message. If the Either succeeded
	 * or the error cannot be recovered, other will
	 * be returned instead.
	 * @param other Alternative message
	 * @return Error message
	 */
	public abstract String errorOrElse(String other);
}
