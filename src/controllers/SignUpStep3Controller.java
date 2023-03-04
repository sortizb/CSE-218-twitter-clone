package controllers;

import java.io.IOException;

import application.Tools;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SignUpStep3Controller {

	@FXML
	private PasswordField pfPassword, pfConfirmPassword;
	@FXML
	private TextArea taPasswordWarning;
	@FXML
	private Label lblMatchWarning;
	@FXML
	private VBox vBox;

	public void signUp(MouseEvent event) throws IOException {
		taPasswordWarning.setVisible(false);
		lblMatchWarning.setVisible(false);
		
		String password = pfPassword.getText();
		if(!Tools.validatePassword(password)) {
			taPasswordWarning.setVisible(true);
			return;
		}
		
		if (!password.equals(pfConfirmPassword.getText())) {
			lblMatchWarning.setVisible(true);
			return;
		}
		
		SignUpController.setPassword(password);
		SignUpController.createUser();
		Tools.createAlert(AlertType.CONFIRMATION, "SignedUp Successfully!", "").showAndWait();
		new SceneController().switchToLogin(event);
	}
	
	public void cancel(MouseEvent event) throws IOException {
		new SceneController().switchToLogin(event);
	}
	
	public void prev(MouseEvent event) {
		SignUpController.previusStep();
	}
	
	public void noFocus(MouseEvent event) {
		vBox.requestFocus();
	}
}
