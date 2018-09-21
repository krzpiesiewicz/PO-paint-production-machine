/**
 * 
 */
package paint_and_pigment_logic;

import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class Pigment {

	private final String name;

	private HashMap<Colour, UseOfPigment> map;

	private ObservableList<Colour> initialColours;

	public Pigment(String name) throws DataException {
		if (!isCorrectPigmentName(name)) {
			throw new DataException("Niepoprawna nazwa pigmentu.");
		}
		this.name = name;
		map = new HashMap<>();
		initialColours = FXCollections.observableArrayList();
	}

	public static boolean isCorrectPigmentName(String name) {
		return name.matches("^[\\p{L}0-9]*$");
	}

	public Paint getResultingPaint(Paint initialPaint) {

		if (initialPaint == null) {
			return null;
		}

		UseOfPigment useOfPigment = map.get(initialPaint.getColour());

		if (useOfPigment == null) {
			return null;
		}

		return useOfPigment.getResultingPaint(initialPaint);
	}

	public UseOfPigment getUseOfPigment(Colour initialPaintColour) {
		return map.get(initialPaintColour);
	}

	public void editUseOfPigment(UseOfPigment useOfPigment) throws DataException {
		if (!map.containsKey(useOfPigment.getInitialPaintColour())) {
			throw new DataException(
					"Nie edytować reguły użycia pigmentu, gdy pigment nie posiada reguły dla takiego wyjściowego koloru.");
		}
		map.put(useOfPigment.getInitialPaintColour(), useOfPigment);
	}

	public void addUseOfPigment(UseOfPigment useOfPigment) throws DataException {
		if (map.containsKey(useOfPigment.getInitialPaintColour())) {
			throw new DataException(
					"Nie można dodać reguły użycia pigmentu, gdy już istnieje reguła dla tego wyjściowego koloru.");
		}
		map.put(useOfPigment.getInitialPaintColour(), useOfPigment);
		initialColours.add(useOfPigment.getInitialPaintColour());
	}

	public void deleteUseOfPigmentOfPaint(Colour initialPaintColour) throws DataException {
		if (!map.containsKey(initialPaintColour)) {
			throw new DataException(
					"Nie można usunąć reguły użycia pigmentu dla koloru, dla którego reguła nie jest zdefioniowana.");
		}
		map.remove(initialPaintColour);
		initialColours.remove(initialPaintColour);
	}

	public ObservableList<Colour> getInitialColours() {
		return FXCollections.unmodifiableObservableList(initialColours);
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Pigment) {
			return name.equals(((Pigment) o).getName());
		}
		return false;
	}

	@Override
	public String toString() {
		return name;
	}
}