/**
 * 
 */
package machine_driver;

import gui.Machine;
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
public class SampleMachine implements Machine {

	private PaintsAndPigmentsManager paintsAndPigmentsManager;
	private Blender blender;

	public SampleMachine() {
		paintsAndPigmentsManager = new PaintsAndPigmentsManager();
		blender = new Blender(paintsAndPigmentsManager);
	}

	public void addPaint(Paint paint) throws MachineException {
		paintsAndPigmentsManager.addPaint(paint);
	}

	public void addPigment(Pigment pigment) throws MachineException {
		paintsAndPigmentsManager.addPigment(pigment);
	}

	public void addUseOfPigment(Pigment pigment, UseOfPigment useOfPigment)
			throws MachineException, DataException {
		paintsAndPigmentsManager.addUseOfPigment(pigment, useOfPigment);
	}

	public void editPaint(Paint paint) throws MachineException {
		paintsAndPigmentsManager.editPaint(paint);
	}

	public void editPigment(Pigment pigment) throws MachineException {
		paintsAndPigmentsManager.editPigment(pigment);
	}

	public void editUseOfPigment(Pigment pigment, UseOfPigment useOfPigment)
			throws MachineException, DataException {
		paintsAndPigmentsManager.editUseOfPigment(pigment, useOfPigment);
	}

	public void deleteUseOfPigment(Pigment pigment, Colour initialPaintColour)
			throws MachineException, DataException {
		paintsAndPigmentsManager.deleteUseOfPigment(pigment, initialPaintColour);
	}

	public Paint getCurrentPaint() {
		return blender.getCurrentPaint();
	}

	public void loadPaintToBlend(Paint paint) throws LackOfSourceException {
		blender.loadPaintToBlend(paint);
	}

	public void usePigment(Pigment pigment)
			throws LackOfSourceException, UndefinedPigmentOperation {
		blender.usePigment(pigment);
	}

	public void stopBlending() {
		blender.stopBlending();
	}

	public ObservableList<Paint> getPaints() {
		return paintsAndPigmentsManager.getPaints();
	}

	public ObservableList<Pigment> getPigments() {
		return paintsAndPigmentsManager.getPigments();
	}

	public ObservableList<Colour> getInitialColours(Pigment pigment) throws MachineException {
		return paintsAndPigmentsManager.getInitialColours(pigment);
	}
}
