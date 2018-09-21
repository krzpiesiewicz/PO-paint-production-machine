/**
 * 
 */
package paint_and_pigment_logic;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class UseOfPigment {

	private final Colour initialPaintColour, resultingPaintColour;
	private final RangeIntegerChanger toxicityChanger, qualityChanger;

	public UseOfPigment(Colour initialPaintColour, Colour resultingPaintColour,
			RangeIntegerChanger toxicityChanger, RangeIntegerChanger qualityChanger) {

		this.initialPaintColour = initialPaintColour;
		this.resultingPaintColour = resultingPaintColour;
		this.toxicityChanger = toxicityChanger;
		this.qualityChanger = qualityChanger;
	}

	public static UseOfPigment parse(String initialColourExpression,
			String resultingColourExpression, String toxicityChangerExpression,
			String qualityChangerExpression) throws DataException {

		Colour initialPaintColour, resultingPaintColour;

		try {
			initialPaintColour = new Colour(initialColourExpression);
		} catch (DataException e) {
			throw new DataException("Wyjściowy kolor: " + e.getMessage());
		}

		try {
			resultingPaintColour = new Colour(resultingColourExpression);
		} catch (DataException e) {
			throw new DataException("Wynikowy kolor: " + e.getMessage());
		}

		RangeIntegerChanger toxicityChanger, qualityChanger;

		try {
			toxicityChanger = new RangeIntegerChanger(toxicityChangerExpression);
		} catch (DataException e) {
			throw new DataException("Zmiana toksyczności: " + e.getMessage());
		}

		try {
			qualityChanger = new RangeIntegerChanger(qualityChangerExpression);
		} catch (DataException e) {
			throw new DataException("Zmiana jakości: " + e.getMessage());
		}

		return new UseOfPigment(initialPaintColour, resultingPaintColour, toxicityChanger,
				qualityChanger);
	}

	protected Paint getResultingPaint(Paint initialPaint) {

		if (initialPaint == null || !initialPaint.getColour().equals(initialPaintColour)) {
			return null;
		}

		Toxicity toxicity = new Toxicity(toxicityChanger.operateOn(initialPaint.getToxicity()));
		Quality quality = new Quality(qualityChanger.operateOn(initialPaint.getQuality()));

		return new Paint(resultingPaintColour, toxicity, quality);
	}

	public RangeIntegerChanger getQualityChanger() {
		return qualityChanger;
	}

	public RangeIntegerChanger getToxicityChanger() {
		return toxicityChanger;
	}

	public Colour getInitialPaintColour() {
		return initialPaintColour;
	}

	public Colour getResultingPaintColour() {
		return resultingPaintColour;
	}

	@Override
	public String toString() {
		return "[" + initialPaintColour.toString() + ", " + resultingPaintColour.toString() + ", "
				+ toxicityChanger.toString() + ", " + qualityChanger.toString() + "]";
	}
}