package controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SceneController {

	private Parent root;
	private Scene scene;
	private Stage stage;
	
	public void switchToLogin(MouseEvent event) throws IOException { 
		
		root = FXMLLoader.load(getClass().getResource("/scenes/LoginScene.fxml"));
		stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
			
	}
	
	public void switchToChangePass(MouseEvent event) throws IOException { 

		root = FXMLLoader.load(getClass().getResource("/scenes/ChangePasswordScene.fxml"));
		stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	public void switchToSignUp(MouseEvent event) throws IOException { 
		
		root = FXMLLoader.load(getClass().getResource("/scenes/SignUpScene.fxml"));
		stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}	
	
	public void switchToHome(MouseEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("/scenes/HomePage.fxml"));
		stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
