package controllers;

import java.io.IOException;

import application.Data;
import application.Tools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SignUpStep1Controller{

	@FXML
	private TextField tfUsername, tfEmail;
	@FXML
	private Label lblUsernameWarning, lblEmailWarning;
	@FXML
	private VBox vBox;
	
	public void next(MouseEvent event) throws IOException {
		lblUsernameWarning.setVisible(false);
		lblEmailWarning.setVisible(false);
		String username = tfUsername.getText();
		if(username.isBlank()) {
			lblUsernameWarning.setText("Please enter a username");
			lblUsernameWarning.setVisible(true);
			return;
		}
		
		if (Data.getData().containsUser(username)) {
			lblUsernameWarning.setText("This username has already been chosen");
			lblUsernameWarning.setVisible(true);
			return;
		}
		
		String email = tfEmail.getText();
		if (!Tools.validateEmail(email)) {
			lblEmailWarning.setVisible(true);
			return;
		}
		
		SignUpController.setUsername(username);
		SignUpController.setEmail(email);
		VBox nextStep = FXMLLoader.load(getClass().getResource("/sub_scenes/SignUpStep2.fxml"));
		SignUpController.nextStep(nextStep);
	}
	
	public void cancel(MouseEvent event) throws IOException {
		new SceneController().switchToLogin(event);
	}
	
	public void noFocus(MouseEvent event) {
		vBox.requestFocus();
	}
}
