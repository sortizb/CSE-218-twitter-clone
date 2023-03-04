package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import application.Data;
import application.Post;
import application.Tools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class SearchSceneController implements Initializable{

	@FXML
	private ListView<Parent> lvSearch;
	@FXML
	private Label lblWordSearched, lblSearchType;
	@FXML
	private Button btnShowMore;
	
	private static String wordSearched;
	private PriorityQueue<Post> searchPriorityQueue;
	private int numPosts;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		numPosts = Data.getData().getTotalPosts();
		searchPriorityQueue = new PriorityQueue<>(1000);
		preparePriorityQueue();
		if (searchPriorityQueue.isEmpty()) {
			Tools.createAlert(AlertType.WARNING, "No Results Found", 
					"No posts were found with the proportioned key word").showAndWait();
			return;
		}
		try {
			loadPosts();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		lblWordSearched.setText(wordSearched);
		lblSearchType.setText("Posts");
		return;
	}
	
	private void loadPosts() throws IOException {
		int count = 0;
		while (!searchPriorityQueue.isEmpty() && count < 10) {
			Post postData = searchPriorityQueue.remove();
			PostViewController.setPost(postData);
			Parent postView = FXMLLoader.load(getClass().getResource("/sub_scenes/PostView.fxml"));
			lvSearch.getItems().add(postView);
			count++;
		}
		if (!searchPriorityQueue.isEmpty()) {
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
			if (post.getKeyWords().contains(wordSearched)) {
				searchPriorityQueue.add(post);
				addedPosts++;
			}	
			index++;
		}
	}
	
	public void showMore(MouseEvent event) throws IOException {
		loadPosts();
	}
	
	public void noListFocus(MouseEvent event) {
		lvSearch.getSelectionModel().select(null);
	}
	
	public static void setWordSearched(String wordSearched) {
		SearchSceneController.wordSearched = wordSearched;
	}
}
