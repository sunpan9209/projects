package edu.cmu.cs.cs214.hw5.framework;

import java.sql.Time;
import java.util.ArrayList;

public class FWUser {
	private ArrayList<Post> posts;
	private String id;
	private ArrayList<FWUser> followers;
	private ArrayList<FWUser> followings;
	private Time time;


	public ArrayList<Post> getPosts() {
		return posts;
	}

	public void addPost(Post post) {
		this.posts.add(post);
	}

	public void addfollower(FWUser follower) {
		this.followers.add(follower);
	}

	public ArrayList<FWUser> getFollowers() {
		return followers;
	}

	public void addFollowings(FWUser following) {
		followings.add(following);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

}
