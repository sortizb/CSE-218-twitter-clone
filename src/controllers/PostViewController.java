package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Data;
import application.Post;
import application.Response;
import application.Tools;
import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class PostViewController implements Initializable{

	@FXML
	private Label lblAuthor, lblDate, lblLikes, lblResponses;
	@FXML
	private TextArea taText;
	@FXML
	private ImageView imgLike, imgResponses, imgPicture;
	@FXML
	private ListView<Parent> lvResponses;
	@FXML
	private VBox vBox;
	@FXML
	private HBox hbEditDesc;
	@FXML
	private Button btnDelete;
	@FXML
	private Circle cProfilePic;
	
	private static Post inputPost;
	private Post postData;
	private boolean likedByUser;
	private boolean showResponses;
	private boolean showMoreBtnAdded;
		
	private User loggedUser;
	private int responsesIndex;	
	private int numResponses;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loggedUser = HomePageController.getLoggedUser();
		postData = inputPost;
		numResponses = postData.getNumResponses();
		responsesIndex = 0;
		lvResponses.setVisible(false);
		showResponses = false;
		showMoreBtnAdded = false;
		
		lblAuthor.setText(postData.getAuthor().getUsername());
		lblDate.setText(Tools.calculateTimeDifference(postData.getDate(), LocalDateTime.now()));
		taText.setText(postData.getText());
		lblLikes.setText(postData.getLikes() + "");
		lblResponses.setText(numResponses + "");
		likedByUser = postData.containsLikedUser(loggedUser); //User has already liked the post
		loadProfilePic();
		
		if (likedByUser) { //Change image to filled heart
			imgLike.setImage(new Image(getClass().getResourceAsStream("/icons/heart-filled.png")));
		}
		
		if (postData.getImageSource() != null) {
			imgPicture.setImage(new Image(postData.getImageSource()));
			imgPicture.prefHeight(230);
		}
		
		if (loggedUser.equals(postData.getAuthor())) {
			btnDelete.setVisible(true);
		}
		else {
			btnDelete.setVisible(false);
		}
	}
	
	public void like(MouseEvent event) {
		if (likedByUser) {
			postData.removeLikedUser(loggedUser);
			imgLike.setImage(new Image(getClass().getResourceAsStream("/icons/heart-blank.png")));
			likedByUser = false;
			lblLikes.setText(postData.getLikes() + "");
		}
		else {
			postData.addLikedUser(loggedUser);
			imgLike.setImage(new Image(getClass().getResourceAsStream("/icons/heart-filled.png")));
			likedByUser = true;
			lblLikes.setText(postData.getLikes() + "");
		}
		Data.getData().saveData();
	}
	
	public void respond(MouseEvent event) throws IOException {
		
		if (!showResponses) {
			NewResponseController.setParentPost(postData);
			NewResponseController.setLabelResponses(lblResponses);
			Parent newResponse = FXMLLoader.load(getClass().getResource("/sub_scenes/NewResponse.fxml"));
			lvResponses.getItems().add(newResponse);
			loadResponses();
			lvResponses.setPrefHeight(300);
			lvResponses.setVisible(true);
			imgResponses.setImage(new Image(getClass().getResourceAsStream("/icons/comments-filled.png")));
			showResponses = true;
		}
		else {
			lvResponses.setPrefHeight(0);
			lvResponses.setVisible(false);
			lvResponses.getItems().clear();
			responsesIndex = 0;
			imgResponses.setImage(new Image(getClass().getResourceAsStream("/icons/comments-blank.png")));
			showResponses = false;
		}
		Data.getData().saveData();
	}
	
	public void delete(MouseEvent event) throws IOException {
		Alert alert = Tools.createAlert(AlertType.CONFIRMATION, "Deleting Post!", 
				"Are you sure about deleting this post?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Data.getData().deletePost(postData);
			Data.getData().saveData();
			reloadHomePage();
		}
	}
	
	public void showProfile(MouseEvent event) throws IOException {
		ProfileController.setViewedUser(postData.getAuthor());
		VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/Profile.fxml"));
		HomePageController.getStaticBp().setCenter(temp);
		HomePageController.setOnPage('p');
	}
	
	private void loadResponses() throws IOException {
		int count = 0;
		while (responsesIndex < numResponses && count < 5) {
			Response responseData = postData.getResponseAt(responsesIndex++);
			ResponseViewController.setInputResponse(responseData);
			Parent responseView = FXMLLoader.load(getClass().getResource("/sub_scenes/ResponseView.fxml"));
			lvResponses.getItems().add(responseView);
			count++;
		}
		if (responsesIndex < numResponses && !showMoreBtnAdded)
				vBox.getChildren().add(createShowMoreButton());
	}
	
	private Button createShowMoreButton() {
		Button btnShowMore = new Button("Show More");
		btnShowMore.setOnAction(e-> {
			try {
				loadResponses();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		btnShowMore.setStyle("	-fx-background-color: #1DA1F2;"
				+ "	-fx-background-radius: 15px;"
				+ "	-fx-text-fill: white;"
				+ "	-fx-border-color: #1DA1F2;"
				+ "	-fx-border-radius: 15;"
				+ "	-fx-font-weight: 700;");
		return btnShowMore;
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
			case 's':
				sceneSource = "/sub_scenes/SearchScene.fxml";
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
	
	private void loadProfilePic() {
		Image profilePic = new Image(postData.getAuthor().getProfilePic());
		cProfilePic.setFill(new ImagePattern(profilePic));
	}
	
	public void noListFocus(MouseEvent event) {
		lvResponses.getSelectionModel().select(null);
	}
	
	public static void setPost(Post post) {
		inputPost = post;
	}
}
