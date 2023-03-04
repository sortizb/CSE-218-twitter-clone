package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;

public class Data implements Serializable{

	private static final long serialVersionUID = 2L;
	private static Data data = null;
	private TreeMap<String, User> allUsers;
	private LinkedList<Post> allPosts;
	private int totalPosts;
	private long mediaId; //Guarantee file copying to source folder with unique name
	
	private Data () {
		allUsers = new TreeMap<>();
		allPosts = new LinkedList<>();
		mediaId = 0;
		totalPosts = 0;
	}
	
	public static Data getData() {
		if (data == null) {
			data = new Data();
			data.loadData();
		}
		return data;
	}
	
	public void loadData() {
		File info = new File ("data.dat");
		try {info.createNewFile();}
		catch (Exception e) {} 
		
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(info))) {
			data = (Data) inputStream.readObject();
			allUsers = data.getAllUsers();
			allPosts = data.getAllPosts();
			mediaId = data.getMediaId();
			totalPosts = data.getTotalPosts();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveData() {
		File info = new File ("data.dat");
		try {info.createNewFile();}
		catch (Exception e) {} 
		
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(info))) {
			outputStream.writeObject(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(String username, String email, String name, String gender, String password) {
		allUsers.put(username, new User(username, email, name, gender, password));
	}
	
	public boolean containsUser(String username) {
		return allUsers.containsKey(username);
	}
	
	public User findUser(String username) {
		return allUsers.get(username);
	}
	
	private TreeMap<String, User> getAllUsers() {
		return allUsers;
	}
	
	private LinkedList<Post> getAllPosts() {
		return allPosts;
	}
	
	public int getTotalPosts() {
		return totalPosts;
	}
	
	public void addPost(User author, String text, String imageSource, String keyWords) {
		Post post = new Post(author, text, imageSource, keyWords);
		allPosts.add(0, post);
		author.addPost(post);
		totalPosts++;
	}
	
	public void deletePost(Post post) {
		allPosts.remove(post);
		post.getAuthor().deletePost(post);
		if (post.getImageSource() != null) {//Delete image of the post, if there exits one
			File file = new File(post.getImageSource());
			file.delete();
		}
		totalPosts--;
	}
	
	public Post getPostAt(int index) {
		return allPosts.get(index);
	}

	public long getMediaId() {
		return mediaId;
	}

	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}
	
	public long useMediaId() {
		return mediaId++;
	}
	
	public User getUser(String username) {
		return allUsers.get(username);
	}
}
