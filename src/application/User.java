package application;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.TreeMap;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String username;
	private String gender;
	private String email;
	private String password;
	private String profilePic;
	private String bannerPic;
	private LinkedList<Post> userPosts; 
	private int numPosts;
	private TreeMap<String, User> followedUsers;
	private long followers;
	private String description;
	private LocalDate dateOfJoinning;
	
	public User (String username, String email, String name, String gender, String password) {
		this.name = name;
		this.username = username;
		this.gender = gender;
		this.email = email;
		this.password = password;
		userPosts = new LinkedList<>();
		numPosts = 0;
		followedUsers = new TreeMap<>();
		followers = 0;
		dateOfJoinning = LocalDate.now();
		profilePic = "/media/default-user.png";
		bannerPic = "/media/default-banner.png";
		description = "No description provided";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDate getDateOfJoinning() {
		return dateOfJoinning;
	}
	
	public int getNumPosts() {
		return numPosts;
	}
	
	public void setProfilePic(String profilePic) {
		if(!this.profilePic.equals("/media/default-user.png")) {
			File oldPic = new File(this.profilePic);
			oldPic.delete();
		}
		this.profilePic = profilePic;
	}
	
	public String getProfilePic() {
		return profilePic;
	}
	
	public void setBannerPic(String bannerPic) {
		if(!this.bannerPic.equals("/media/default-banner.png")) {
			File oldBanner = new File(this.bannerPic);
			oldBanner.delete();
		}
		this.bannerPic = bannerPic;
	}
	
	public String getBannerPic() {
		return bannerPic;
	}
	
	public boolean equals(Object o) {
		if (o == this)
			return true;
		
		if (!(o instanceof User))
			return false;
		
		User compareUser = (User)o;
		
		if (compareUser.getUsername().equals(this.username))
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", username=" + username + ", gender=" + gender + ", email=" + email
				+ ", password=" + password + "]";
	}

	public Post getPostsAt(int index) {
		return userPosts.get(index);
	}
	
	public void addPost(Post post) {
		userPosts.add(0, post);
		numPosts++;
	}
	
	public void deletePost(Post post) {
		userPosts.remove(post);
		numPosts--;
	}
	
	public LinkedList<Post> getUserPosts() {
		return userPosts;
	}

	public void addFollowedUser(User user) {
		followedUsers.put(user.username, user);
	}
	
	public void removeFollowedUser(User user) {
		followedUsers.remove(user.username);
	}
	
	public boolean follows(User user) {
		return followedUsers.containsKey(user.username);
	}
	
	public long getFollowers() {
		return followers;
	}
	
	public void addFollower() {
		followers++;
	}
	
	public void removeFollower() {
		followers--;
	}
}
