/**
 * 
 */
package paint_and_pigment_logic;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class Toxicity extends RangeInteger {

	private static final IntRange RANGE = new IntRange(0, 100);

	/**
	 * @param val
	 */
	protected Toxicity(int val) {
		super(val, RANGE);
	}

	protected Toxicity(RangeInteger ri) {
		this(ri.val);
	}

	public Toxicity(String expression) throws DataException {
		super(expression, RANGE);
	}
}
