package controllers;

import java.io.IOException;

import application.Data;
import application.Tools;
import application.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ChangePasswordController {

	@FXML
	private TextField tfUsername;
	@FXML
	private PasswordField pfPassword, pfConfirmPassword;
	@FXML
	private Label lblUsernameWarning, lblMatchWarning;
	@FXML
	private TextArea taPasswordWarning;
	@FXML
	private VBox vBox;
	
	public void changePassword(MouseEvent event) throws IOException {
		lblUsernameWarning.setVisible(false);
		taPasswordWarning.setVisible(false);
		lblMatchWarning.setVisible(false);
		
		String username = tfUsername.getText();
		User user = Data.getData().findUser(username);
		if (user == null) {
			lblUsernameWarning.setVisible(true);
			return;
		}
		
		String password = pfPassword.getText();
		if (!Tools.validatePassword(password)) {
			taPasswordWarning.setVisible(true);
			return;
		}
		
		if (!password.equals(pfConfirmPassword.getText())) {
			lblMatchWarning.setVisible(true);
			return;
		}
		
		user.setPassword(password);
		Tools.createAlert(AlertType.CONFIRMATION, "Password Changed Successfully!", "");
		new SceneController().switchToLogin(event);
	}
	
	public void cancel(MouseEvent event) throws IOException {
		new SceneController().switchToLogin(event);
	}
	
	public void noFocus(MouseEvent event) {
		vBox.requestFocus();
	}
}
