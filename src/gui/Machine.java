/**
 * 
 */
package gui;

import javafx.collections.ObservableList;
import paint_and_pigment_logic.Colour;
import paint_and_pigment_logic.Paint;
import paint_and_pigment_logic.Pigment;
import paint_and_pigment_logic.UseOfPigment;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public interface Machine {

	public void addPaint(Paint paint) throws Exception;

	public void addPigment(Pigment pigment) throws Exception;

	public void addUseOfPigment(Pigment pigment, UseOfPigment useOfPigment) throws Exception;

	public void editPaint(Paint paint) throws Exception;

	public void editPigment(Pigment pigment) throws Exception;

	public void editUseOfPigment(Pigment pigment, UseOfPigment useOfPigment) throws Exception;

	public void deleteUseOfPigment(Pigment pigment, Colour initialPaintColour) throws Exception;

	public Paint getCurrentPaint();

	public void loadPaintToBlend(Paint paint) throws Exception;

	public void usePigment(Pigment pigment) throws Exception;

	public void stopBlending();

	public ObservableList<Paint> getPaints();

	public ObservableList<Pigment> getPigments();

	public ObservableList<Colour> getInitialColours(Pigment pigment) throws Exception;
}
