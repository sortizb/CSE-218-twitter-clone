package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Data;
import application.Tools;
import application.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class HomePageController implements Initializable {

	@FXML
	private BorderPane bpVariable;
	@FXML
	private ChoiceBox<String> cbSearchType;
	@FXML
	private TextField tfSearch;
	
	private static User loggedUser;
	private static BorderPane staticBp;
	private static char onPage;
	private String[] searchChoices = {"Posts", "Users"};
	private String searchType;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		staticBp = bpVariable;
		cbSearchType.getItems().addAll(searchChoices);
		cbSearchType.setOnAction(this::selectSearchType);
		cbSearchType.setValue("Posts");
		try {
			VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/MainFeed.fxml"));
			bpVariable.setCenter(temp);
			onPage = 'h';
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tweet(MouseEvent event) throws IOException {
		if (onPage == 's')
			return; //Can't post on search
		if (onPage == 'p' && !ProfileController.getIsAdmin()) {
			return; //You can't post on someone else's profile
		}
		
		VBox createPost = FXMLLoader.load(getClass().getResource("/sub_scenes/NewPost.fxml"));
		((VBox)(bpVariable.getCenter())).getChildren().add(1, createPost);
	}
	
	public void selectSearchType(ActionEvent event) {
		searchType = cbSearchType.getValue();
	}
	
	public void changeToExplore(MouseEvent event) throws IOException {
		VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/Explore.fxml"));
		bpVariable.setCenter(temp);
		onPage = 'e';
	}
	
	public void changeToHome(MouseEvent event) throws IOException {
		VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/MainFeed.fxml"));
		bpVariable.setCenter(temp);
		onPage = 'h';
	}
	
	public void changeToProfile(MouseEvent event) throws IOException {
		ProfileController.setViewedUser(loggedUser);
		VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/Profile.fxml"));
		bpVariable.setCenter(temp);
		onPage = 'p';
	}
	
	public void changeToTrending(MouseEvent event) throws IOException {
		VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/Trends.fxml"));
		bpVariable.setCenter(temp);
		onPage = 't';
	}
	
	public void logOut(MouseEvent event) throws IOException {
		Alert alert = Tools.createAlert(AlertType.CONFIRMATION, "SignOut!", 
				"Do you want to SignOut?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			new SceneController().switchToLogin(event);
		}	
	}
	
	public void search(MouseEvent event) throws IOException {
		if (tfSearch.getText().isBlank()) {
			Tools.createAlert(AlertType.WARNING, "Search is empty", 
					"The SearchBar is empty").showAndWait();
			return;
		}
		
		if (searchType.equals("Posts")) {
			SearchSceneController.setWordSearched(tfSearch.getText());
			VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/SearchScene.fxml"));
			bpVariable.setCenter(temp);
			onPage = 's';
		}
		else {
			User foundUser = Data.getData().getUser(tfSearch.getText());
			if (foundUser == null) {
				Tools.createAlert(AlertType.WARNING, "User Not Found", 
						"The username provided could not be found").showAndWait();
				return;
			}
			ProfileController.setViewedUser(foundUser);
			VBox temp = FXMLLoader.load(getClass().getResource("/sub_scenes/Profile.fxml"));
			bpVariable.setCenter(temp);
			onPage = 'p';
		}
	}
	
	public void reload(MouseEvent event) throws IOException {
		String sceneSource = null;
		switch (onPage) {
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
		bpVariable.setCenter(parent);
	}
	
	public static void setLoggedUser(User user) {
		loggedUser = user;
	}
	
	public static User getLoggedUser() {
		return loggedUser;
	}

	public static BorderPane getStaticBp() {
		return staticBp;
	}
	
	public static char getOnPage() {
		return onPage;
	}
	
	public static void setOnPage(char index) {
		onPage = index;
	}
}
