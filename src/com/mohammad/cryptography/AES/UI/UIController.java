package com.mohammad.cryptography.AES.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIController extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("UI.fxml"));
		UIEventHandler eventHandler = new UIEventHandler();
		eventHandler.init(primaryStage);
		
		Parent root = loader.load();
		primaryStage.setTitle("AES");
		primaryStage.setScene(new Scene(root, 600,500));
		primaryStage.show();
	}

}
