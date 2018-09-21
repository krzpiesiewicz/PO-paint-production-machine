/**
 * 
 */
package paint_and_pigment_logic;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class Quality extends RangeInteger {

	private static final IntRange RANGE = new IntRange(0, 100);

	protected Quality(int val) {
		super(val, RANGE);
	}

	protected Quality(RangeInteger ri) {
		this(ri.val);
	}

	public Quality(String expression) throws DataException {
		super(expression, RANGE);
	}
}
