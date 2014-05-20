package edu.cmu.cs.cs214.hw6;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.cs214.hw6.plugin.wordcount.WordCountClient;
import edu.cmu.cs.cs214.hw6.plugin.wordprefix.WordPrefixClient;
import edu.cmu.cs.cs214.hw6.util.Log;

/**
 * An abstract client class used primarily for code reuse between the
 * {@link WordCountClient} and {@link WordPrefixClient}.
 */
public abstract class AbstractClient {
	private final String mMasterHost;
	private final int mMasterPort;
	private List<String> results;
	private static final String TAG = "Client";

	/**
	 * The {@link AbstractClient} constructor.
	 * 
	 * @param masterHost
	 *            The host name of the {@link MasterServer}.
	 * @param masterPort
	 *            The port that the {@link MasterServer} is listening on.
	 */
	public AbstractClient(String masterHost, int masterPort) {
		mMasterHost = masterHost;
		mMasterPort = masterPort;
		results = new ArrayList<String>();
	}

	protected abstract MapTask getMapTask();

	protected abstract ReduceTask getReduceTask();

	public void execute() {
		final MapTask mapTask = getMapTask();
		final ReduceTask reduceTask = getReduceTask();
		Socket socket = null;
		try {
			socket = new Socket(mMasterHost, mMasterPort);
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			out.writeObject(mapTask);
		//	out.reset();
			out.writeObject(reduceTask);
			ObjectInputStream in = new ObjectInputStream(
					socket.getInputStream());
			results = (List<String>) in.readObject();
			for (String path : results) {
				Log.i("Path", path);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Warning! Received exception while interacting with master.",
					e);
			return;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				// Ignore because we're about to exit anyway.
			}
		}
	}

}
