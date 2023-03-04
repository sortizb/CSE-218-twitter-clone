package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SignUpStep2Controller {

	@FXML
	private TextField tfName;
	@FXML
	private Label lblNameWarning, lblGenderWarning;
	@FXML
	private VBox vBox;
	@FXML
	private RadioButton rbMale, rbFemale, rbOther;
	
	private String gender = "";
	
	public void next(MouseEvent event) throws IOException {
		lblNameWarning.setVisible(false);
		lblGenderWarning.setVisible(false);
		
		String name = tfName.getText();
		if (name.isBlank()) {
			lblNameWarning.setVisible(true);
			return;
		}
		
		if (gender.isEmpty()) {
			lblGenderWarning.setVisible(true);
			return;
		}
		
		SignUpController.setName(name);
		SignUpController.setGender(gender);
		VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/SignUpStep3.fxml"));
		SignUpController.nextStep(temp);
	}
	
	public void prev(MouseEvent event) {
		SignUpController.previusStep();
	}
	
	public void cancel(MouseEvent event) throws IOException {
		new SceneController().switchToLogin(event);
	}
	
	public void selectGender(ActionEvent event) {
		if (rbMale.isSelected()) {
			gender = "Male";
			return;
		}
		
		if (rbFemale.isSelected()) {
			gender = "Female";
			return;
		}
		
		if (rbOther.isSelected()) {
			gender = "Other";
			return ;
		}
	}
	
	public void noFocus(MouseEvent event) {
		vBox.requestFocus();
	}
}
