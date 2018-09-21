/**
 * 
 */
package machine_driver;

import paint_and_pigment_logic.Paint;
import paint_and_pigment_logic.Pigment;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
class Blender {

	private PaintsAndPigmentsManager paintsAndPigmentsManager;

	private Paint currentPaint;

	protected Blender(PaintsAndPigmentsManager paintsAndPigmentsManager) {
		this.paintsAndPigmentsManager = paintsAndPigmentsManager;
	}

	protected void loadPaintToBlend(Paint paint) throws LackOfSourceException {

		if (!paintsAndPigmentsManager.isPaintAvailable(paint)) {
			throw new LackOfSourceException(
					"Nie można załadować do mieszania podanej farby. Nie ma takiej w maszynie.");
		}

		currentPaint = paint;
		System.out.println("Zaczynam mieszanie");
		printCurrentPaint();
	}

	protected void usePigment(Pigment pigment)
			throws LackOfSourceException, UndefinedPigmentOperation {

		if (!paintsAndPigmentsManager.isPigmentAvailable(pigment)) {
			throw new LackOfSourceException(
					"Nie można użyć podanego pigmentu. Nie ma takiego w maszynie.");
		}

		Paint resPaint = pigment.getResultingPaint(currentPaint);
		if (resPaint == null) {
			throw new UndefinedPigmentOperation("Brak reguły użycia pigmentu: " + pigment
					+ " dla koloru: " + currentPaint.getColour() + ".");
		}

		currentPaint = resPaint;
		printCurrentPaint();
	}

	protected void stopBlending() {
		currentPaint = null;
		System.out.println("Koniec mieszania.\n");
	}

	protected Paint getCurrentPaint() {
		return currentPaint;
	}

	private void printCurrentPaint() {
		System.out
				.println("Aktualna farba:   kolor: " + currentPaint.getColour() + ", toksyczność: "
						+ currentPaint.getToxicity() + ", jakość: " + currentPaint.getQuality());
	}
}
