package application;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.TreeMap;

public class Post implements Serializable, Comparable<Post>{

	private static final long serialVersionUID = -8854035584670971587L;
	private User author;
	private String text;
	private LocalDateTime date;
	private int likes;
	private String imageSource;
	private TreeMap<String, User> likedUsers;
	private LinkedList<Response> responses;
	private int numResponses;
	private String keyWords;
	private double trendPoints;
	
	public Post(User author, String text, String imageSource, String keyWords) {
		this.author = author;
		this.text = text;
		this.imageSource = imageSource;
		likes = 0;
		date = LocalDateTime.now();
		likedUsers = new TreeMap<>();
		responses = new LinkedList<>();
		numResponses = 0;
		this.keyWords = keyWords;
		calculateTrendPoints();
	}

	public User getAuthor() {
		return author;
	}

	public String getText() {
		return text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public int getLikes() {
		return likes;
	}

	public String getImageSource() {
		return imageSource;
	}
	
	public int getNumResponses() {
		return numResponses;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public double getTrendPoints() {
		return trendPoints;
	}

	public boolean  containsLikedUser(User user) {
		return likedUsers.containsKey(user.getUsername());
	}
	
	public void addLikedUser(User user) {
		likedUsers.put(user.getUsername(), user);
		likes++;
		calculateTrendPoints();
	}
	
	public void removeLikedUser(User user) {
		likedUsers.remove(user.getUsername());
		likes--;
		calculateTrendPoints();
	}
	
	public Response getResponseAt(int index) {
		return responses.get(index);
	}
	
	public void addResponse(User author, String text) {
		responses.add(new Response(author, text, this));
		numResponses++;
		calculateTrendPoints();
	}
	
	public void removeResponse(Response response) {
		responses.remove(response);
		numResponses--;
		calculateTrendPoints();
	}
	
	@Override
	//Return values are inverted, so less popular will be removed first in priority queue
	public int compareTo(Post p) {
		if (this.trendPoints > p.trendPoints) 
			return -1;
		
		else if (this.trendPoints < p.trendPoints)
			return 1;
		
		else
			return 0;
	}
	private void calculateTrendPoints() {
		trendPoints = (date.getYear()* 2) + (date.getDayOfYear() * 1.15) 
				+ (likes * 1.25) + (numResponses * 1.25);
	}



}
