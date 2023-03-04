package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.PriorityQueue;
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

public class TrendsController implements Initializable{

	@FXML
	private ListView<Parent> lvPosts;
	@FXML
	private Button btnShowMore;
	@FXML
	private VBox root;
	
	private PriorityQueue<Post> postsPriorityQueue;
	private int numPosts;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		numPosts = Data.getData().getTotalPosts();
		postsPriorityQueue = new PriorityQueue<>(1000);
		preparePriorityQueue();
		try {
			loadPosts();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadPosts() throws IOException {
		int count = 0;
		while (!postsPriorityQueue.isEmpty() && count < 10) {
			Post postData = postsPriorityQueue.remove();
			PostViewController.setPost(postData);
			Parent postView = FXMLLoader.load(getClass().getResource("/sub_scenes/PostView.fxml"));
			lvPosts.getItems().add(postView);
			count++;
		}
		if (!postsPriorityQueue.isEmpty()) {
			btnShowMore.setVisible(true);
		}
		else {
			btnShowMore.setVisible(false);
		}
	}
	
	private void preparePriorityQueue() {
		int index = 0;
		int addedPosts = 0;
		while (index < numPosts && addedPosts < 1000) {
			Post post = Data.getData().getPostAt(index);
			postsPriorityQueue.add(post);
			addedPosts++;	
			index++;
		}
	}
	
	public void showMore(MouseEvent event) throws IOException {
		loadPosts();
	}
	
	public void noListFocus(MouseEvent event) {
		lvPosts.getSelectionModel().select(null);
	}
}
