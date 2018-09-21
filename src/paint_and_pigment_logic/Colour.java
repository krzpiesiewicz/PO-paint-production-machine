/**
 * 
 */
package paint_and_pigment_logic;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class Colour {

	private String name;
	private int hash;

	public Colour(String name) throws DataException {
		if (!isCorrectColourName(name)) {
			throw new DataException("Niepoprawna nazwa koloru.");
		}
		this.name = name;
		this.hash = this.name.hashCode();
	}

	public static boolean isCorrectColourName(String name) {
		return name.matches("^[\\p{L}]+[\\p{L}0-9-]*$");
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	public boolean equals(Object o) {
		if (o instanceof Colour) {
			Colour c = (Colour) o;
			return name.equals(c.getName());
		}
		return false;
	}
}