/**
 * 
 */
package gui;

import java.util.Random;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import machine_driver.SampleMachine;
import paint_and_pigment_logic.Colour;
import paint_and_pigment_logic.Paint;
import paint_and_pigment_logic.Pigment;
import paint_and_pigment_logic.UseOfPigment;
import rx.observables.JavaFxObservable;

/**
 * 
 * @author Krzysztof Piesiewicz
 */
public class MainController {

	@FXML
	private Pane parent;

	@FXML
	private Button addPaint, savePaint, cancelPaint, addPigment, savePigment, cancelPigment,
			addUseOfPigment, saveUseOfPigment, cancelUseOfPigment, deleteUseOfPigment, blend,
			usePigment, finishBlending;

	@FXML
	private TextField paintColour, paintToxicity, paintQuality, pigmentName, initialColour,
			resultingColour, toxicityChange, qualityChange, currColour, currToxicity, currQuality,
			chosenPigment;

	@FXML
	private ComboBox<Colour> chooseColour;

	@FXML
	private ListView<Paint> paintsList;

	@FXML
	private ListView<Pigment> pigmentsList;

	@FXML
	private ObservableList<Paint> paints;

	@FXML
	private ObservableList<Pigment> pigments;

	private Paint paint;

	private Pigment pigment;

	private Colour colour;

	private boolean isPaintBeingAdded, isPaintBeingEdited, isPigmentBeingAdded,
			isPigmentBeingEdited, isUseOfPigmentBeingAdded, isUseOfPigmentBeingEdited,
			isPaintBeingBlended, useOfPigmentPreview, choosingPigment, paintPreview;

	private Machine machine;

	@FXML
	private void initialize() {

		Platform.runLater(() -> parent.requestFocus());

		machine = new SampleMachine();
		try {
			AppConfigurator.readConfig(machine);
		} catch (ConfigFileException e) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Maszyna do produkcji farb");
			alert.setHeaderText("Błąd wczytywania konfiguracji maszyny");
			alert.setContentText(e.getMessage());
			alert.getDialogPane().setPrefSize(800, 400);
			alert.showAndWait();
			System.exit(1);
		}

		paints = machine.getPaints();
		pigments = machine.getPigments();

		paintsList.setItems(paints);
		pigmentsList.setItems(pigments);

		initializePaintsActions();
		initializePigmentsActions();
		initializeBlendActions();
	}

	private void initializePaintsActions() {

		JavaFxObservable.actionEventsOf(addPaint).subscribe(e -> {
			closePaintParams();

			isPaintBeingAdded = true;
			isPaintBeingEdited = false;

			paint = null;

			openPaintParams();
			paintColour.setText(randomOriginalPaintName());
			paintColour.setEditable(true);
			paintColour.requestFocus();
			cancelPaint.setDisable(false);
		});

		paintsList.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldPaint, newPaint) -> {

					paint = newPaint;

					if (paint != null) {
						isPaintBeingEdited = true;
						isPaintBeingAdded = false;

						paintPreview = true;

						paintColour.setText(paint.getColour().toString());
						paintToxicity.setText(paint.getToxicity().toString());
						paintQuality.setText(paint.getQuality().toString());

						paintPreview = false;

						openPaintParams();

						savePaint.setDisable(true);
						cancelPaint.setDisable(true);
						paintColour.setEditable(false);
					}
				});

		for (TextField textField : new TextField[] {paintColour, paintToxicity, paintQuality}) {
			textField.textProperty().addListener((obs, oldText, newText) -> {
				if (newText != "") {
					savePaint.setDisable(false);
					cancelPaint.setDisable(false);
				}
			});
		}

		JavaFxObservable.actionEventsOf(savePaint).subscribe(event -> {

			try {
				paint = Paint.parse(paintColour.getText(), paintToxicity.getText(),
						paintQuality.getText());
				if (isPaintBeingEdited) {
					machine.editPaint(paint);
				}
				if (isPaintBeingAdded) {
					machine.addPaint(paint);
					paintsList.getSelectionModel().select(paint);
				}
				savePaint.setDisable(true);
				cancelPaint.setDisable(true);
			} catch (Exception e) {
				warningWindow(e.getMessage());
			}
		});

		JavaFxObservable.actionEventsOf(cancelPaint).subscribe(event -> {
			boolean wasCancelEdition = isPaintBeingEdited;
			isPaintBeingEdited = false;
			isPaintBeingAdded = false;

			closePaintParams();

			if (wasCancelEdition) {
				paintsList.getSelectionModel().select(paint);
			}
		});
	}

	private void openPaintParams() {

		paintColour.setDisable(false);
		paintToxicity.setDisable(false);
		paintQuality.setDisable(false);

		parent.requestFocus();
	}

	private void closePaintParams() {
		paintColour.setText("");
		paintToxicity.setText("");
		paintQuality.setText("");

		paintColour.setDisable(true);
		paintToxicity.setDisable(true);
		paintQuality.setDisable(true);

		paintColour.setEditable(false);

		savePaint.setDisable(true);
		cancelPaint.setDisable(true);

		paintsList.getSelectionModel().clearSelection();
		parent.requestFocus();
	}

	private String randomOriginalPaintName() {
		String name = null;
		Random r = new Random();
		do {
			name = "";
			for (int i = 0; i < 15; i++) {
				name = name + Character.toString((char) ('a' + r.nextInt('z' - 'a' + 1)));
			}
		} while (!isFreePaintName(name));
		return name;
	}

	private boolean isFreePaintName(String name) {
		for (Paint p : paints) {
			if (p.getColour().getName().equals(name)) {
				return false;
			}
		}
		return true;
	}

	private void initializeBlendActions() {

		paintsList.getSelectionModel().selectedItemProperty().addListener((obs, old, newPaint) -> {

			paint = newPaint;

			if (paint != null && !isPaintBeingBlended) {
				blend.setDisable(false);
			}
		});

		JavaFxObservable.actionEventsOf(addPigment).subscribe(event -> {
			stopBlending();
		});
		JavaFxObservable.actionEventsOf(addUseOfPigment).subscribe(event -> {
			stopBlending();
		});

		pigmentsList.getSelectionModel().selectedItemProperty()
				.addListener((obs, old, newPigment) -> {

					if (isPaintBeingBlended) {
						pigment = newPigment;
						if (pigment != null) {
							chosenPigment.setText(pigment.toString());
							usePigment.setDisable(false);
						}
					}
				});

		JavaFxObservable.actionEventsOf(blend).subscribe(event -> {
			currColour.setText(paint.getColour().toString());
			currToxicity.setText(paint.getToxicity().toString());
			currQuality.setText(paint.getQuality().toString());

			currColour.setDisable(false);
			currToxicity.setDisable(false);
			currQuality.setDisable(false);

			blend.setDisable(true);
			finishBlending.setDisable(false);
			pigmentsList.getSelectionModel().clearSelection();

			isPaintBeingBlended = true;

			try {
				machine.loadPaintToBlend(paint);
			} catch (Exception e) {

			}
		});

		JavaFxObservable.actionEventsOf(usePigment).subscribe(event -> {
			try {
				machine.usePigment(pigment);

				Paint p = machine.getCurrentPaint();

				currColour.setText(p.getColour().toString());
				currToxicity.setText(p.getToxicity().toString());
				currQuality.setText(p.getQuality().toString());

			} catch (Exception e) {
				warningWindow(e.getMessage());
			}
		});

		JavaFxObservable.actionEventsOf(finishBlending).subscribe(event -> {
			stopBlending();
		});

		for (TextField textField : new TextField[] {paintToxicity, paintQuality, initialColour,
				resultingColour, toxicityChange, qualityChange}) {
			textField.textProperty().addListener((obs, oldText, newText) -> {
				if (!useOfPigmentPreview && !choosingPigment && !paintPreview) {
					stopBlending();
				}
			});
		}

		for (TextField textField : new TextField[] {paintToxicity, paintQuality}) {
			textField.textProperty().addListener((obs, oldText, newText) -> {
				blend.setDisable(true);
			});
		}
	}

	private void stopBlending() {

		if (isPaintBeingBlended) {
			isPaintBeingBlended = false;
			machine.stopBlending();

			currColour.setText("");
			currToxicity.setText("");
			currQuality.setText("");
			chosenPigment.setText("");

			currColour.setDisable(true);
			currToxicity.setDisable(true);
			currQuality.setDisable(true);
			chosenPigment.setDisable(true);

			usePigment.setDisable(true);
			finishBlending.setDisable(true);
			blend.setDisable(false);
		}
	}

	private void initializePigmentsActions() {

		JavaFxObservable.actionEventsOf(addPigment).subscribe(e -> {

			isPigmentBeingAdded = true;
			isPigmentBeingEdited = false;

			closeUseOfPigment();
			pigmentsList.getSelectionModel().clearSelection();

			pigmentName.setDisable(false);
			pigmentName.setEditable(true);
			pigmentName.setText(randomOriginalPigmentName());
			pigmentName.requestFocus();

			addUseOfPigment.setDisable(true);
			chooseColour.setDisable(true);
		});

		pigmentName.textProperty().addListener((obs, oldText, newText) -> {
			if (!isPigmentBeingAdded) {
				cancelPigment.setDisable(true);
				savePigment.setDisable(true);
			} else {
				cancelPigment.setDisable(false);
				savePigment.setDisable(newText.equals(""));
			}
		});

		pigmentsList.getSelectionModel().selectedItemProperty()
				.addListener((obs, old, newPigment) -> {

					choosingPigment = true;

					closeUseOfPigment();

					pigment = newPigment;

					if (pigment != null) {
						try {
							chooseColour.setItems(machine.getInitialColours(pigment));
						} catch (Exception e) {
							e.printStackTrace();
						}

						isPigmentBeingEdited = true;
						isPigmentBeingAdded = false;

						pigmentName.setDisable(false);
						pigmentName.setText(pigment.toString());

						chooseColour.setDisable(false);
						addUseOfPigment.setDisable(false);
					}

					parent.requestFocus();

					choosingPigment = false;
				});

		JavaFxObservable.actionEventsOf(savePigment).subscribe(event -> {

			try {
				pigment = new Pigment(pigmentName.getText());

				if (isPigmentBeingEdited) {
					machine.editPigment(pigment);
					isPigmentBeingEdited = false;
				}
				if (isPigmentBeingAdded) {
					machine.addPigment(pigment);
					pigmentsList.getSelectionModel().select(pigment);
					isPigmentBeingAdded = false;
				}
				pigmentName.setEditable(false);
				savePigment.setDisable(true);
				cancelPigment.setDisable(true);
				closeUseOfPigment();
			} catch (Exception e) {
				warningWindow(e.getMessage());
			}
			;
		});

		JavaFxObservable.actionEventsOf(cancelPigment).subscribe(e -> {
			isPigmentBeingAdded = false;
			isPigmentBeingEdited = false;

			pigmentName.setText("");
			pigmentName.setEditable(false);

			cancelPigment.setDisable(true);
			savePigment.setDisable(true);

			closeUseOfPigment();
		});

		chooseColour.getSelectionModel().selectedItemProperty()
				.addListener((obs, old, newColour) -> {
					colour = newColour;

					if (colour != null) {

						useOfPigmentPreview = true;

						isUseOfPigmentBeingEdited = true;

						openUseOfPigment();

						UseOfPigment useOfPigment = pigment.getUseOfPigment(colour);

						initialColour.setText(colour.getName());
						resultingColour.setText(useOfPigment.getResultingPaintColour().toString());
						toxicityChange.setText(useOfPigment.getToxicityChanger().toString());
						qualityChange.setText(useOfPigment.getQualityChanger().toString());

						useOfPigmentPreview = false;

						saveUseOfPigment.setDisable(true);

						deleteUseOfPigment.setDisable(false);
					}
				});

		JavaFxObservable.actionEventsOf(addUseOfPigment).subscribe(event -> {
			closeUseOfPigment();
			openUseOfPigment();
			isUseOfPigmentBeingAdded = true;
			isUseOfPigmentBeingEdited = false;
			cancelUseOfPigment.setDisable(false);
			initialColour.setEditable(true);
			initialColour.requestFocus();
		});

		for (TextField textField : new TextField[] {initialColour, resultingColour, toxicityChange,
				qualityChange}) {
			textField.textProperty().addListener((obs, oldText, newText) -> {
				if (!isUseOfPigmentBeingAdded) {
					deleteUseOfPigment.setDisable(false);
				}
				cancelUseOfPigment.setDisable(false);
				saveUseOfPigment.setDisable(false);
			});
		}

		JavaFxObservable.actionEventsOf(saveUseOfPigment).subscribe(event -> {

			try {
				UseOfPigment useOfPigment = UseOfPigment.parse(initialColour.getText(),
						resultingColour.getText(), toxicityChange.getText(),
						qualityChange.getText());

				if (isUseOfPigmentBeingEdited) {
					machine.editUseOfPigment(pigment, useOfPigment);
					isUseOfPigmentBeingEdited = false;
				}
				if (isUseOfPigmentBeingAdded) {
					machine.addUseOfPigment(pigment, useOfPigment);
					isUseOfPigmentBeingAdded = false;
				}

//				closeUseOfPigment();
				saveUseOfPigment.setDisable(true);
				cancelUseOfPigment.setDisable(true);

			} catch (Exception e) {
				warningWindow(e.getMessage());
			}
			;
		});

		JavaFxObservable.actionEventsOf(cancelUseOfPigment).subscribe(event -> {
			closeUseOfPigment();
		});

		JavaFxObservable.actionEventsOf(deleteUseOfPigment).subscribe(event -> {
			try {
				machine.deleteUseOfPigment(pigment, colour);
			} catch (Exception e) {
				warningWindow(e.getMessage());
			}
			;
			closeUseOfPigment();
		});
	}

	private void openUseOfPigment() {
		initialColour.setDisable(false);
		resultingColour.setDisable(false);
		toxicityChange.setDisable(false);
		qualityChange.setDisable(false);

		parent.requestFocus();
	}

	private void closeUseOfPigment() {

		isUseOfPigmentBeingAdded = false;
		isUseOfPigmentBeingEdited = false;

		initialColour.setEditable(false);

		initialColour.setDisable(true);
		resultingColour.setDisable(true);
		toxicityChange.setDisable(true);
		qualityChange.setDisable(true);

		initialColour.setText("");
		initialColour.setText("");
		resultingColour.setText("");
		toxicityChange.setText("");
		qualityChange.setText("");

		deleteUseOfPigment.setDisable(true);
		cancelUseOfPigment.setDisable(true);
		saveUseOfPigment.setDisable(true);

		chooseColour.getSelectionModel().clearSelection();

		parent.requestFocus();
	}

	private String randomOriginalPigmentName() {
		String name = null;
		Random r = new Random();
		do {
			name = "OPI";
			for (int i = 0; i < 15; i++) {
				name = name + Character.toString((char) ('0' + r.nextInt(10)));
			}
		} while (!isFreePigmentName(name));
		return name;
	}

	private boolean isFreePigmentName(String name) {
		for (Pigment p : pigments) {
			if (p.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}

	private void warningWindow(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Maszyna do produkcji farb");
		alert.setHeaderText("Ostrzeżenie.");
		alert.setContentText(message);
		alert.getDialogPane().setPrefSize(800, 400);
		alert.showAndWait();
	}
}
