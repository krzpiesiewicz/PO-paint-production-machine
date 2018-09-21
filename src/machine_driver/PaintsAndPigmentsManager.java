/**
 * 
 */
package machine_driver;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import paint_and_pigment_logic.Colour;
import paint_and_pigment_logic.DataException;
import paint_and_pigment_logic.Paint;
import paint_and_pigment_logic.Pigment;
import paint_and_pigment_logic.UseOfPigment;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
class PaintsAndPigmentsManager {

	protected ObservableList<Paint> paints;
	protected ObservableList<Pigment> pigments;

	private HashMap<Colour, Paint> paintsMap;
	private HashMap<String, Pigment> pigmentsMap;

	protected PaintsAndPigmentsManager() {
		paints = FXCollections.observableArrayList();
		pigments = FXCollections.observableArrayList();

		paintsMap = new HashMap<>();
		pigmentsMap = new HashMap<>();
	}

	protected void addPaint(Paint paint) throws MachineException {
		if (paintsMap.containsKey(paint.getColour())) {
			throw new MachineException("Nazwa koloru jest zajęta.");
		}
		paintsMap.put(paint.getColour(), paint);
		paints.add(paint);
	}

	protected void addPigment(Pigment pigment) throws MachineException {
		if (pigmentsMap.containsKey(pigment.getName())) {
			throw new MachineException("Nazwa pigmentu jest zajęta.");
		}
		pigmentsMap.put(pigment.getName(), pigment);
		pigments.add(pigment);
	}

	protected void addUseOfPigment(Pigment pigment, UseOfPigment useOfPigment)
			throws MachineException, DataException {
		if (!pigmentsMap.containsKey(pigment.getName())) {
			throw new MachineException(
					"Nie można dodać reguły użycia pigmentu dla pigmentu, którego nie ma w maszynie.");
		}
		pigment = pigmentsMap.get(pigment.getName());
		pigment.addUseOfPigment(useOfPigment);
	}

	protected void editPaint(Paint paint) throws MachineException {
		if (!paintsMap.containsKey(paint.getColour())) {
			throw new MachineException("Nie można edytować farby, której nie ma w maszynie.");
		}
		paintsMap.put(paint.getColour(), paint);
		replaceElementInList(paints, paint, (p -> {
			return paint.hasTheSameColour(p);
		}));
	}

	protected void editPigment(Pigment pigment) throws MachineException {
		if (!pigmentsMap.containsKey(pigment.getName())) {
			throw new MachineException("Nie można edytować pigmentu, którego nie ma w maszynie.");
		}
		pigmentsMap.put(pigment.getName(), pigment);
		replaceElementInList(pigments, pigment, (p -> {
			return pigment.equals(p);
		}));
	}

	protected void editUseOfPigment(Pigment pigment, UseOfPigment useOfPigment)
			throws MachineException, DataException {
		if (!pigmentsMap.containsKey(pigment.getName())) {
			throw new MachineException(
					"Nie można edytować reguły użycia pigmentu dla pigmentu, którego nie ma w maszynie.");
		}
		pigment = pigmentsMap.get(pigment.getName());
		pigment.editUseOfPigment(useOfPigment);
	}

	protected void deleteUseOfPigment(Pigment pigment, Colour initialPaintColour)
			throws MachineException, DataException {
		if (!pigmentsMap.containsKey(pigment.getName())) {
			throw new MachineException(
					"Nie można usunąć reguły użycia pigmentu dla pigmentu, którego nie ma w maszynie.");
		}
		pigment = pigmentsMap.get(pigment.getName());
		pigment.deleteUseOfPigmentOfPaint(initialPaintColour);
	}

	private <T> void replaceElementInList(List<T> list, T element,
			Function<T, Boolean> elementEquals) {
		ListIterator<T> it = list.listIterator();
		while (it.hasNext()) {
			if (elementEquals.apply(it.next())) {
				it.set(element);
				break;
			}
		}
	}

	protected boolean isPaintAvailable(Paint paint) {
		return paints.contains(paint);
	}

	protected boolean isPigmentAvailable(Pigment pigment) {
		return pigments.contains(pigment);
	}

	protected ObservableList<Paint> getPaints() {
		return FXCollections.unmodifiableObservableList(paints);
	}

	protected ObservableList<Pigment> getPigments() {
		return FXCollections.unmodifiableObservableList(pigments);
	}

	public ObservableList<Colour> getInitialColours(Pigment pigment) throws MachineException {
		if (!pigmentsMap.containsKey(pigment.getName())) {
			throw new MachineException(
					"Nie można pobrać listy kolorów wyjściowych dla działań pigmentu.");
		}
		pigment = pigmentsMap.get(pigment.getName());
		return pigment.getInitialColours();
	}
}
