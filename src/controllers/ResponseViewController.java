package controllers;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Data;
import application.Response;
import application.Tools;
import application.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class ResponseViewController implements Initializable{

	@FXML
	private Label lblAuthor, lblDate, lblLikes;
	@FXML
	private TextArea taText;
	@FXML
	private ImageView imgLike;
	@FXML
	private Button btnLike, btnDelete;
	@FXML
	private Circle cProfilePic;
	
	private static User loggedUser;
	private static Response inputResponse;
	private Response responseData;
	private boolean likedByUser;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loggedUser = HomePageController.getLoggedUser();
		responseData = inputResponse;
		lblAuthor.setText(responseData.getAuthor().getUsername());
		lblDate.setText(Tools.calculateTimeDifference(responseData.getDate(), LocalDateTime.now()));
		taText.setText(responseData.getText());
		lblLikes.setText(responseData.getLikes() + "");
		likedByUser = responseData.containsLikedUser(loggedUser);
		loadProfilePic();
		
		if (likedByUser) {
			imgLike.setImage(new Image(getClass().getResourceAsStream("/icons/heart-filled.png")));
		}
		
		if (responseData.getAuthor().equals(HomePageController.getLoggedUser())) {
			btnDelete.setVisible(true);
		}
		else {
			btnDelete.setVisible(false);
		}
	}
	
	public void like(MouseEvent event) {
		if (likedByUser) {
			responseData.removeLikedUsers(loggedUser);
			likedByUser = false;
			imgLike.setImage(new Image(getClass().getResourceAsStream("/icons/heart-blank.png")));
		}
		else {
			responseData.addLikedUser(loggedUser);
			likedByUser = true;
			imgLike.setImage(new Image(getClass().getResourceAsStream("/icons/heart-filled.png")));
		}
		lblLikes.setText(responseData.getLikes() + "");
		Data.getData().saveData();
	}
	
	public void delete(MouseEvent event) {
		Alert alert = Tools.createAlert(AlertType.CONFIRMATION, "Deleting Response!", 
				"Do you want to delete this response?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			responseData.deleteResponseFromParent();
			Data.getData().saveData();
			Tools.createAlert(AlertType.CONFIRMATION, "Done!", "Response deleted successfully").showAndWait();
		}
	}
	
	private void loadProfilePic() {
		Image profilePic = new Image(responseData.getAuthor().getProfilePic());
		cProfilePic.setFill(new ImagePattern(profilePic));
	}
	
	public static void setInputResponse(Response response) {
		inputResponse = response;
	}
	
}
