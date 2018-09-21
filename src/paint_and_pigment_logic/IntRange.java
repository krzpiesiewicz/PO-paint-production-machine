/**
 * 
 */
package paint_and_pigment_logic;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class IntRange {

	public final int from, to;

	public IntRange(int a, int b) {
		this.from = Math.min(a, b);
		this.to = Math.max(a, b);
	}

	public boolean isInRange(int val) {
		return val >= from && val <= to;
	}

	public int makeInRange(int val) {
		return Math.max(from, Math.min(to, val));
	}

	@Override
	public String toString() {
		return "[" + from + "; " + to + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IntRange) {
			IntRange ir = (IntRange) o;
			return from == ir.from && to == ir.to;
		}
		return false;
	}
}
