/**
 * 
 */
package paint_and_pigment_logic;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class RangeInteger {

	public final Integer val;
	public final IntRange range;

	protected RangeInteger(int val, IntRange range) {
		this.val = range.makeInRange(val);
		this.range = range;
	}

	protected RangeInteger(String expression, IntRange range) throws DataException {

		this.range = range;

		if (expression.isEmpty()) {
			throw new DataException("Puste wyrażenie do parsowania.");
		}

		try {
			val = Integer.parseInt(expression);

			if (!range.isInRange(val)) {
				throw new DataException("Wartość całkowita nie mieści się w " + range + ".");
			}
		} catch (NumberFormatException e) {
			throw new DataException(
					"Niepoprawna wartość całkowita. Nie można przekonwertować na int.");
		}
	}

	@Override
	public String toString() {
		return val.toString();
	}

	public boolean equals(Object o) {
		if (o instanceof RangeInteger) {
			RangeInteger ri = (RangeInteger) o;
			return val.equals(ri.val) && range.equals(ri.range);
		}
		return false;
	}
}
