package application;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.TreeMap;

public class Response implements Serializable{

	private static final long serialVersionUID = -6517377151707318420L;
	private User author;
	private LocalDateTime date;
	private String text;
	private int likes;
	private Post parentPost;
	private TreeMap<String, User> likedUsers;
	
	public Response(User author, String text, Post parentPost) {
		this.author = author;
		this.text = text;
		this.parentPost = parentPost;
		likedUsers = new TreeMap<>();
		date = LocalDateTime.now();
	}
	
	public User getAuthor() {
		return author;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getLikes() {
		return likes;
	}
	
	public void deleteResponseFromParent() {
		parentPost.removeResponse(this);
	}
	
	public boolean containsLikedUser(User user) {
		return likedUsers.containsKey(user.getUsername());
	}
	
	public void addLikedUser(User user) {
		likedUsers.put(user.getUsername(), user);
		likes++;
	}
	
	public void removeLikedUsers(User user) {
		likedUsers.remove(user.getUsername());
		likes--;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
}
