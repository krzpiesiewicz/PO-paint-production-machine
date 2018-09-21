/**
 * 
 */
package paint_and_pigment_logic;

import java.util.function.BiFunction;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class RangeIntegerChanger {

	private final Double ratioOrDiff;
	private final String expression;

	BiFunction<Integer, Double, Integer> operation;

	public RangeIntegerChanger(String expression) throws DataException {

		if (expression.isEmpty()) {
			throw new DataException("Puste wyrażenie do parsowania.");
		}

		this.expression = expression;

		char sign = expression.charAt(0);

		switch (sign) {
		case '+':
			operation = (i, d) -> {
				return new Double(i.doubleValue() + d).intValue();
			};
			break;
		case '-':
			operation = (i, d) -> {
				return new Double(i.doubleValue() - d).intValue();
			};
			break;
		case 'x':
			operation = (i, d) -> {
				return new Double(i.doubleValue() * d).intValue();
			};
			break;
		default:
			throw new DataException("Nieznany znak operacji '" + Character.toString(sign) + "'.");
		}

		if (expression.length() < 2) {
			throw new DataException("Brakuje liczby rzeczywistej.");
		}

		try {
			ratioOrDiff = Double.parseDouble(expression.substring(1));
		} catch (NumberFormatException e) {
			throw new DataException(
					"Niepoprawna wartość rzeczywista. Nie daje się przekonwertować na double.");
		}
	}

	protected RangeInteger operateOn(RangeInteger intWithR) {
		return new RangeInteger(operation.apply(intWithR.val, ratioOrDiff), intWithR.range);
	}

	@Override
	public String toString() {
		return expression;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RangeIntegerChanger) {
			RangeIntegerChanger rich = (RangeIntegerChanger) o;
			return expression.equals(rich.expression);
		}
		return false;
	}
}
