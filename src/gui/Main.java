package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@FXML
	Parent root;

	@Override
	public void start(Stage stage) {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/MainPane.fxml"));
			root = loader.load();

			Scene scene = new Scene(root);
			scene.getStylesheets().add("/resources/gui.css");
			stage.setScene(scene);

			stage.setTitle("Maszyna do produkcji farb");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
