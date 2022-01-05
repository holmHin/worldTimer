package de.holm.worldTimer.Views;

import de.holm.worldTimer.controller.WTMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WTMain extends Application {
	
	public static void main(String[] args){
		System.out.println("Start Application");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("World Timer");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		Scene scene = new Scene(root);
		WTMainController controller = loader.<WTMainController>getController();
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.show();

		// Settings Path
		
		//Development
		//String SRC_PATH ="de/holm/worldTimer/etc/wtSettings.xml"; 
		
		//Productive
		String SRC_PATH ="etc/wtSettings.xml"; 
		
		SRC_PATH = this.getClass().getClassLoader().getResource("").getPath()+SRC_PATH;
 		System.out.println(SRC_PATH);
		
		//Start Controller
		controller.init(SRC_PATH, scene );
		

	}

}
