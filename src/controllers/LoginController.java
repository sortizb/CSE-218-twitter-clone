package controllers;

import java.io.IOException;

import application.Data;
import application.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class LoginController {

	@FXML 
	private Label lblUserNotFound, lblWrongPassword; 
	@FXML
	private TextField tfUsername;
	@FXML
	private PasswordField pfPassword;
	@FXML
	private VBox vBox;

	public void signIn(MouseEvent event) throws IOException {
	
		String username = tfUsername.getText();
		String password = pfPassword.getText();
		User user = Data.getData().findUser(username);
		lblUserNotFound.setText("");	//Reset labels text
		lblWrongPassword.setText("");
		if (user == null) {
			lblUserNotFound.setText("User not found. Please verify and try again");
			return;
		}
		
		if (!user.getPassword().equals(password)) {
			lblWrongPassword.setText("Wrong Password. Please try again");
			return;
		}
		
		HomePageController.setLoggedUser(user);
		new SceneController().switchToHome(event);
	}
	
	public void switchToSignUp(MouseEvent event) throws IOException {
		new SceneController().switchToSignUp(event);
	}
	
	public void switchToChangePass(MouseEvent event) throws IOException {
		new SceneController().switchToChangePass(event);
	}
	
	public void noFocus(MouseEvent event) {
		vBox.requestFocus();
	}
}
