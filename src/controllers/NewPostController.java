package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Data;
import application.Tools;
import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class NewPostController implements Initializable{

	@FXML
	private TextArea taText;
	@FXML
	private Label lblImageSource, lblPostWarning, lblChars;
	@FXML
	private TextField tfKeyWords;
	
	private String imageSource = null;
	
	public void selectImage(MouseEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter imgFilter = 
			new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
		
		fileChooser.getExtensionFilters().add(imgFilter);
		File selectedFile = fileChooser.showOpenDialog(null);
	    if (selectedFile != null) {
	        imageSource = Tools.copyFileToMediaFolder(selectedFile);
	        lblImageSource.setText(selectedFile.toString());
	    }
	}
	
	public void post(MouseEvent event) throws IOException {
		lblPostWarning.setVisible(false);
		
		String text = taText.getText();
		if (text.isBlank()) {
			lblPostWarning.setVisible(true);
			return;
		}
		
		String keyWords = tfKeyWords.getText();
		User loggedUser = HomePageController.getLoggedUser();
		Data.getData().addPost(loggedUser, text, imageSource, keyWords);
		Data.getData();
		reloadHomePage();
	}
	
	public void cancel(MouseEvent event) {
		((VBox)(HomePageController.getStaticBp().getCenter())).getChildren().remove(1);
	}
	
	public void countChars(KeyEvent event) {
		int numChars = taText.getLength();
		lblChars.setText(numChars + "");
	}
	
	private void reloadHomePage() throws IOException {
		String sceneSource = null;
		switch (HomePageController.getOnPage()) {
			case 'e': 
				sceneSource = "/sub_scenes/Explore.fxml";
				break;
			case 'p': 
				sceneSource = "/sub_scenes/Profile.fxml";
				break;
			case 't':
				sceneSource = "/sub_scenes/Trends.fxml";
				break;
			default:
				sceneSource = "/sub_scenes/MainFeed.fxml";
				break;
		}
		VBox parent = FXMLLoader.load(getClass().getResource(sceneSource));
		HomePageController.getStaticBp().setCenter(parent);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		taText.setTextFormatter(new TextFormatter<String>(change -> 
    	change.getControlNewText().length() <= 280 ? change : null));
	}
}
