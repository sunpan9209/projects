package edu.cmu.cs.cs214.hw5.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;

import edu.cmu.cs.cs214.hw5.framework.DataListener;
import edu.cmu.cs.cs214.hw5.framework.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.Post;
import edu.cmu.cs.cs214.hw5.framework.UserData;

/**
 * Data Plugin to retrieve user and their posts information from GitHub API
 * @author shanshangao
 *
 */
public class GithubDataPlugin implements DataPlugin{
	
	private RepositoryService repoService;
	private CommitService commitService;
	private GitHubClient client;
	private int requestLimit;
	private int requestRemaining;
	private DataListener dataListener;
	private UserData latestUserData;
	
	public GithubDataPlugin() {
		repoService = new RepositoryService();
		commitService = new CommitService();
		client = new GitHubClient();
//		client.setOAuth2Token("28f3cdb0a9e52e40d51e9e87c273ff871316f3fc");
//		client.setOAuth2Token("02209c771a1e0662a28a5a4b8c328dc06f945d7d");
		client.setCredentials("IrisG33", "woshig33");
		requestLimit= client.getRequestLimit();
		requestRemaining = client.getRemainingRequests();
		latestUserData = new UserData();
		System.out.println(requestLimit);
	}

	@Override
	public String getdataSource() {
		return "GitHub";
	}

	@Override
	public UserData getDataFromSource(String userID) {
		ArrayList<Post> posts = getPosts(userID);
		latestUserData.setPosts(posts);
		latestUserData.setId(userID);
		dataListener.onDataArrive(latestUserData);
		return latestUserData;
		
	}

	@Override
	public void setDataArriveListener(DataListener listener) {
		dataListener = listener;
	}
	
	/**
	 * get posts information by given user ID
	 * @param userID user ID on GitHub
	 * @return all the commits from all the repository the user have created on GitHub
	 */
	private ArrayList<Post> getPosts(String userID) {
		try {
			
			ArrayList<Post> posts = new ArrayList<Post>();
			List<Repository> repositories = repoService.getRepositories(userID);
			for (Repository repo : repositories) {
				try {
					List<RepositoryCommit> repoCommits =  commitService.getCommits(repo);
					for (RepositoryCommit rc : repoCommits) {
						Post post = new Post();
						Commit commit = rc.getCommit();
						post.setId(commit.getSha());
						post.setMessage(commit.getMessage());
						CommitUser commiter = commit.getCommitter();
						post.setCreatedAt(commiter.getDate());
						post.setSharedTimes(repo.getWatchers());
						posts.add(post);
						System.out.println(post.getMessage());
						System.out.println(post.getId());
						System.out.println(post.getSharedTimes());
					}
					
				} catch (RequestException e) {
					System.out.println(e.getMessage());
					dataListener.onExceptionHappen();
					return posts;
				}		
				
			}
			return posts;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			dataListener.onExceptionHappen();
			return null;
		}
	}
	

	/**
	 * get request limit 
	 * @return request limit
	 */
	public int getRequestLimit() {
		return requestLimit;
	}

	/**
	 * get request remaining
	 * @return remaining number of request
	 */
	public int getRequestRemaining() {
		return requestRemaining;
	}


}
