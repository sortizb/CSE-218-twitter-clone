package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import application.Data;
import application.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MainFeedController implements Initializable{

	@FXML
	private ListView<Parent> lvPosts;
	@FXML
	private Button btnShowMore;
	@FXML
	private VBox root;
	
	private int postsIndex;
	private int numPosts;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		numPosts = Data.getData().getTotalPosts();
		postsIndex = 0;
		try {
			loadPosts();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadPosts() throws IOException {
		int count = 0;
		while (postsIndex < numPosts && count < 10) {
			Post postData = Data.getData().getPostAt(postsIndex++);
			//Check if author is followed, else skip this post
			if (!HomePageController.getLoggedUser().follows(postData.getAuthor()))
				continue;
			
			PostViewController.setPost(postData);
			Parent postView = FXMLLoader.load(getClass().getResource("/sub_scenes/PostView.fxml"));
			lvPosts.getItems().add(postView);
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
	
	public void noListFocus(MouseEvent event) {
		lvPosts.getSelectionModel().select(null);
	}
}
