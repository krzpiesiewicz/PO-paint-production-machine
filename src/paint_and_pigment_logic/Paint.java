/**
 * 
 */
package paint_and_pigment_logic;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class Paint {

	private final Colour colour;
	private final Toxicity toxicity;
	private final Quality quality;

	public Paint(Colour colour, Toxicity toxicity, Quality quality) {
		this.colour = colour;
		this.toxicity = toxicity;
		this.quality = quality;
	}

	public static Paint parse(String colourExpression, String toxicityExpression,
			String qualityExpression) throws DataException {

		Colour colour = new Colour(colourExpression);

		Toxicity toxicity;
		try {
			toxicity = new Toxicity(toxicityExpression);
		} catch (DataException e) {
			throw new DataException("Toksyczność: " + e.getMessage());
		}

		Quality quality;
		try {
			quality = new Quality(qualityExpression);
		} catch (DataException e) {
			throw new DataException("Jakość: " + e.getMessage());
		}

		return new Paint(colour, toxicity, quality);
	}

	public Toxicity getToxicity() {
		return toxicity;
	}

	public Quality getQuality() {
		return quality;
	}

	public Colour getColour() {
		return colour;
	}

	@Override
	public String toString() {
		return colour.getName();
	}

	public boolean equals(Paint p) {
		boolean hasTheSameColur = hasTheSameColour(p);

		if (!hasTheSameColur) {
			return false;
		}

		return toxicity.equals(p.toxicity) && quality.equals(p.quality);
	}

	public boolean hasTheSameColour(Paint p) {
		if (p == null) {
			return false;
		}

		return colour.equals(colour);
	}
}
