package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import application.Data;
import application.Post;
import application.Tools;
import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class ProfileController implements Initializable{

	@FXML
	private Circle cProfilePic;
	@FXML
	private Label lblUsername, lblFollowers, lblTotalPosts,
				  lblName, lblGender, lblChars, lblEmail, lblDate, lblJoinDate;
	@FXML
	private HBox banner, hbEditDesc, hbChars;
	@FXML
	private Button btnFollow, btnShowMore, btnEditBanner;
	@FXML
	private TextArea taDesc;
	@FXML
	private ListView<Parent> lvUserPosts;
	
	private static User viewedUser;
	private static boolean isAdmin;
	private int postsIndex;
	private int numPosts;
	private boolean followedUser;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		postsIndex = 0;
		numPosts = viewedUser.getNumPosts();
		isAdmin = viewedUser.equals(HomePageController.getLoggedUser());
		
		lblUsername.setText(viewedUser.getUsername());
		loadProfilePic();
		loadBanner();
		lblFollowers.setText(viewedUser.getFollowers() + "");
		lblTotalPosts.setText(viewedUser.getNumPosts() + "");
		lblName.setText(viewedUser.getName());
		lblGender.setText(viewedUser.getGender());
		taDesc.setText(viewedUser.getDescription());
		lblEmail.setText(viewedUser.getEmail());
		lblJoinDate.setText(viewedUser.getDateOfJoinning().toString());
		hbChars.setVisible(false);
		
		if (!isAdmin) {
			btnFollow.setDisable(false);
			btnEditBanner.setVisible(false);
			hbEditDesc.setVisible(false);
		}
		else {
			btnFollow.setDisable(true);
			btnEditBanner.setVisible(true);
			hbEditDesc.setVisible(true);
		}
		
		try {
			loadPosts();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		followedUser = HomePageController.getLoggedUser().follows(viewedUser);
		if (followedUser) {
			btnFollow.setText("Followed");
		}
		
		taDesc.setTextFormatter(new TextFormatter<String>(change -> 
        	change.getControlNewText().length() <= 400 ? change : null));
		taDesc.setEditable(false);
	}
	
	private void loadPosts() throws IOException {
		int count = 0;
		while (postsIndex < numPosts && count < 10) {
			Post postData = viewedUser.getPostsAt(postsIndex++);
			PostViewController.setPost(postData);
			Parent postView = FXMLLoader.load(getClass().getResource("/sub_scenes/PostView.fxml"));
			lvUserPosts.getItems().add(postView);
			count++;
		}
		if (postsIndex < numPosts) {
			btnShowMore.setVisible(true);
		}
		else {
			btnShowMore.setVisible(false);
		}
	}
	
	public void showMore(MouseEvent event) throws IOException {
		loadPosts();
	}
	
	public void follow(MouseEvent event) throws IOException {
		if (followedUser) {
			HomePageController.getLoggedUser().removeFollowedUser(viewedUser);
			viewedUser.removeFollower();
			Data.getData().saveData();
			reloadHomePage();
		} else {
			HomePageController.getLoggedUser().addFollowedUser(viewedUser);
			viewedUser.addFollower();
			Data.getData().saveData();
			reloadHomePage();
		}
	}
	
	public void changeProfilePic(MouseEvent event) throws IOException {
		if (!isAdmin)
			return;
			
		String newPicUrl = selectImage();
		if (newPicUrl == null)
			return;
		
		HomePageController.getLoggedUser().setProfilePic(newPicUrl);
		Data.getData().saveData();
		reloadHomePage();
	}
	
	public void changeBannerPic(MouseEvent event) throws IOException {
		String newPicUrl = selectImage();
		if (newPicUrl == null)
			return;
		HomePageController.getLoggedUser().setBannerPic(newPicUrl);
		Data.getData().saveData();
		reloadHomePage();
	}
	
	public void editDesc(MouseEvent event) {
		taDesc.setEditable(true);
		lblChars.setText(taDesc.getLength() + "");
		hbChars.setVisible(true);
	}

	public void countChars(KeyEvent event) {
		int chars = taDesc.getLength();
		lblChars.setText(chars + "");
	}
	
	public void confirm(MouseEvent event) {
		String newDesc = taDesc.getText();
		HomePageController.getLoggedUser().setDescription(newDesc);
		Data.getData().saveData();
		hbChars.setVisible(false);
		taDesc.setEditable(false);
	}
	
	public void cancelEditDesc(MouseEvent event) {
		taDesc.setText(HomePageController.getLoggedUser().getDescription());
		hbChars.setVisible(false);
		taDesc.setEditable(false);
	}
	
	private void loadProfilePic() {
		Image profilePic = new Image(viewedUser.getProfilePic());
		cProfilePic.setFill(new ImagePattern(profilePic));
	}
	
	private void loadBanner() {
		BackgroundSize backgroundSize = new BackgroundSize(Double.MAX_VALUE,
				Double.MAX_VALUE,
		        false,
		        false,
		        false,
		        true);
		BackgroundImage image = new BackgroundImage(new Image(viewedUser.getBannerPic()),
		        BackgroundRepeat.SPACE,
		        BackgroundRepeat.SPACE,
		        BackgroundPosition.CENTER,
		        backgroundSize);

		banner.setBackground(new Background(image));
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
			default:
				sceneSource = "/sub_scenes/MainFeed.fxml";
				break;
		}
		VBox parent = FXMLLoader.load(getClass().getResource(sceneSource));
		HomePageController.getStaticBp().setCenter(parent);
	}
	
	private String selectImage() throws IOException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter imgFilter = 
			new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
		
		fileChooser.getExtensionFilters().add(imgFilter);
		File selectedFile = fileChooser.showOpenDialog(null);
	    if (selectedFile != null) {
	        return Tools.copyFileToMediaFolder(selectedFile);
	    }
	    return null;
	}
	
	public static void setViewedUser(User user) {
		viewedUser = user;
	}
	
	public void noListFocus(MouseEvent event) {
		lvUserPosts.getSelectionModel().select(null);
	}
	
	public static boolean getIsAdmin() {
		return isAdmin;
	}
}
