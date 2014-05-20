package edu.cmu.cs.cs214.hw5.plugin;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.cs214.hw5.framework.DataListener;
import edu.cmu.cs.cs214.hw5.framework.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.Post;
import edu.cmu.cs.cs214.hw5.framework.UserData;
import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.api.UsersResources;

public class TwitterDataPlugin implements DataPlugin{
		
	private Twitter twitter;
	private UsersResources userResources;
	private DataListener dataListener;
	private UserData latestUserData;
	
	public TwitterDataPlugin() {
		twitter = new TwitterFactory().getInstance();
		userResources = twitter.users();	
		latestUserData = new UserData();
	}

	@Override
	public String getdataSource() {
		return "twitter";
	}

	@Override
	public UserData getDataFromSource(String userID) {
		
		try {
			
			// prepare to create the UserData
			String id = userID;
			ArrayList<Post> posts = getPosts(twitter, userID);
			latestUserData.setId(id);
			latestUserData.setPosts(posts);
			dataListener.onDataArrive(latestUserData);
			return latestUserData;
			
		} catch (TwitterException e) {
			System.out.println(e.getMessage());
			dataListener.onExceptionHappen();
			return latestUserData;
		}
	}
	
	
	@Override
	public void setDataArriveListener(DataListener listener) {
		dataListener = listener;
	}
	
	/**
	 * get user's post on twitter
	 * @param twitter twitter object
	 * @param id user's id on Twitter
	 * @return Posts of users 
	 * @throws TwitterException
	 */
	private ArrayList<Post> getPosts(Twitter twitter, String id) throws TwitterException {
		Paging page = new Paging(1, 100);
		List<Status> statuses = twitter.getUserTimeline(id, page);
		ArrayList<Post> posts = new ArrayList<Post>();
		for (Status s : statuses) {
			// build the post according to the status
			Post p = new Post();
			p.setId(String.valueOf(s.getId()));
			p.setMessage(s.getText());	
			p.setSharedTimes(s.getRetweetCount());
			p.setCreatedAt(s.getCreatedAt());
			posts.add(p);
		}
		return posts;
	}
	
	/**
	 * get user's followers
	 * @param userID the user's Twitter ID
	 * @return followers of Twitter's User class
	 */
	private ArrayList<User> getFollowers(String userID) {
		ArrayList<User> followers = new ArrayList<User>();
		long lCursor = -1;
		try {
			PagableResponseList<User> followersStub = twitter.getFollowersList(userID, lCursor);
			for (User follower : followersStub) {
				followers.add(follower);
				System.out.println(follower.getName());
			}
		} catch (TwitterException e) {
			dataListener.onExceptionHappen();
			e.printStackTrace();
		}
		return followers;
	}
	
	/**
	 * get a user's following
	 * @param userID user's id on Twitter
	 * @return followings of Twitter's User class
	 */
	private ArrayList<User> getFollowings(String userID) {
		ArrayList<User> followings = new ArrayList<User>();
		long cursor = -1;
		IDs ids;
		try {
			ids = twitter.getFriendsIDs(userID, cursor);
			for (long id : ids.getIDs()) {
				followings.add(twitter.showUser(id));
			}
		} catch (TwitterException e) {
			System.out.println(e.getErrorMessage());
			dataListener.onExceptionHappen();
			return null;
		}	
		return followings;
	}


}
