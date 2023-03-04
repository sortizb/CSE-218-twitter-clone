package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import application.Data;
import application.Post;
import application.Tools;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class NewResponseController implements Initializable{
	
	@FXML
	private TextArea taText;
	@FXML
	private Label lblResponseWarning, lblChars;
	
	private static Post inputPost;
	private Post parentPost;	
	
	private static Label inputLabel;
	private Label lblNumResponses;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		parentPost = inputPost;
		lblNumResponses = inputLabel;
		taText.setTextFormatter(new TextFormatter<String>(change -> 
    	change.getControlNewText().length() <= 280 ? change : null));
	}
	
	public void respond(MouseEvent event) throws IOException {
		String text = taText.getText();
		lblResponseWarning.setVisible(false);
		if (text.isBlank()) {
			lblResponseWarning.setVisible(true);
			return;
		}
		parentPost.addResponse(HomePageController.getLoggedUser(), text);
		Data.getData().saveData();
		Tools.createAlert(AlertType.CONFIRMATION, "Done!", "Response posted successfully!").showAndWait();
		lblNumResponses.setText(parentPost.getNumResponses() + "");
	}
	
	public void countChars(KeyEvent event) {
		int numChars = taText.getLength();
		lblChars.setText(numChars + "");
	}
	
	public static void setParentPost(Post post) {
		inputPost = post;
	}
	
	public static void setLabelResponses(Label label) {
		inputLabel = label;
	}
	
}
