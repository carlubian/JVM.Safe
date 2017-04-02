package util;
/**
 * Unit is used as a type when handling void-consuming
 * or supplying functions.
 * Unit only has a single instance, and it's only equal
 * to itself.
 */
public class Unit {
	private static Unit _unit = new Unit();
	
	private Unit() {}
	
	/**
	 * Get the active instance of Unit.
	 */
	public static Unit Instance() {
		return _unit;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Unit;
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public String toString() {
		return "[Unit]";
	}
}
