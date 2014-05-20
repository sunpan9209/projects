package edu.cmu.cs.cs214.hw5.framework;

import java.util.ArrayList;

/**
 * 
 * @author weisiyu
 *
 */

public class UserData {
	private ArrayList<Post> posts;
	private String id;
	private ArrayList<UserData> followers;
	private ArrayList<UserData> followings;


	public ArrayList<Post> getPosts() {
		return posts;
	}

	public void setPosts(ArrayList<Post> posts) {
		this.posts = posts;
	}

	public void addfollower(UserData follower) {
		this.followers.add(follower);
	}

	public ArrayList<UserData> getFollowers() {
		return followers;
	}

	public void addFollowings(UserData following) {
		followings.add(following);
	}

	public void setFollowers(ArrayList<UserData> followers) {
		this.followers = followers;
	}

	public void setFollowings(ArrayList<UserData> followings) {
		this.followings = followings;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
