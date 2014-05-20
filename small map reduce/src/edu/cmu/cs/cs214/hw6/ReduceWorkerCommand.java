package edu.cmu.cs.cs214.hw6;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.cmu.cs.cs214.hw6.util.KeyValuePair;
import edu.cmu.cs.cs214.hw6.util.Log;
import edu.cmu.cs.cs214.hw6.util.WorkerStorage;

/**
 * execute a reduce task on a server
 */
public class ReduceWorkerCommand extends WorkerCommand {
	private static final long serialVersionUID = 2959976909122090865L;
	private static final String TAG = "ReduceWorkerCommand";
	private final ReduceTask reduce;
	private final Map<WorkerInfo, String> infos;
	private final String workerName;
	private final int signal;
	private final int size;
	private transient ExecutorService mExecutor;
	private final int MAX_POOL_SIZE = Runtime.getRuntime()
			.availableProcessors();

	public ReduceWorkerCommand(ReduceTask reduce,
			Map<WorkerInfo, String> infos, String name, int signal, int size) {
		this.reduce = reduce;
		this.infos = infos;
		workerName = name;
		this.signal = signal;
		this.size = size;
		int numThreads = Math.min(MAX_POOL_SIZE, size);
		mExecutor = Executors.newFixedThreadPool(numThreads);
	}

	@Override
	public void run() {
		int numThreads = Math.min(MAX_POOL_SIZE, size);
		mExecutor = Executors.newFixedThreadPool(numThreads);
		Socket masterSocket = getSocket();
		String uniqueName = workerName + System.currentTimeMillis();
		File file = new File(
				WorkerStorage.getFinalResultsDirectory(workerName), uniqueName);
		ResultEmitter emitter = new ResultEmitter(file);
		try {
			List<ShuffleCallable> shuffleCallables = new ArrayList<ShuffleCallable>();
			for (Entry<WorkerInfo, String> r : infos.entrySet()) {
				shuffleCallables.add(new ShuffleCallable(signal, r.getValue(),
						r.getKey(), size));
			}
			List<Future<List<KeyValuePair>>> results = null;
			try {
				results = mExecutor.invokeAll(shuffleCallables);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			Map<String, List<String>> values = new HashMap<String, List<String>>();
			for (int i = 0; i < results.size(); i++) {
				ShuffleCallable shuffleCallable = shuffleCallables.get(i);
				Future<List<KeyValuePair>> result = results.get(i);
				try {
					for (KeyValuePair pair : result.get()) {
						if (values.get(pair.getKey()) == null) {
							values.put(pair.getKey(), new ArrayList<String>());
							values.get(pair.getKey()).add(pair.getValue());
						} else {
							values.get(pair.getKey()).add(pair.getValue());
						}
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (ExecutionException e) {
					String workerHost = shuffleCallable.getWorker().getHost();
					int workerPort = shuffleCallable.getWorker().getPort();
					String info = String.format("[host=%s, port=%d]",
							workerHost, workerPort);
					Log.e(TAG,
							"Warning! Failed to execute shuffle task for worker: "
									+ info, e.getCause());
				}
			}
			for (Entry<String, List<String>> r : values.entrySet()) {
				try {
					reduce.execute(r.getKey(), r.getValue().iterator(), emitter);
				} catch (IOException e) {
					Log.e(TAG, "I/O error while executing reducetask.", e);
				}
			}
			try {
				ObjectOutputStream out = new ObjectOutputStream(
						masterSocket.getOutputStream());
				out.writeObject(file.getPath());
			} catch (IOException e) {
				Log.e(TAG, "I/O error while executing reducetask.", e);
			}
		} finally {
			try {
				masterSocket.close();
			} catch (IOException e) {
				// Ignore because we're about to exit anyway.
			}
		}
	}

	/**
	 * send shuffle command to map worker
	 */
	private static class ShuffleCallable implements
			Callable<List<KeyValuePair>> {
		private final int signal;
		private final WorkerInfo worker;
		private final String fileName;
		private final int size;

		public ShuffleCallable(int signal, String fileName, WorkerInfo worker,
				int size) {
			this.signal = signal;
			this.worker = worker;
			this.fileName = fileName;
			this.size = size;
		}

		/**
		 * Returns the {@link WorkerInfo} object that provides information about
		 * the worker that this callable task is responsible for interacting
		 * with.
		 */
		public WorkerInfo getWorker() {
			return worker;
		}

		@Override
		public List<KeyValuePair> call() throws Exception {
			Socket socket = null;
			List<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
			try {
				socket = new Socket(worker.getHost(), worker.getPort());
				ObjectOutputStream out = new ObjectOutputStream(
						socket.getOutputStream());
				out.writeObject(new ShuffleWorkerCommand(signal, worker
						.getName(), fileName, size));
				ObjectInputStream in = new ObjectInputStream(
						socket.getInputStream());
				KeyValuePair pair = (KeyValuePair) in.readObject();
				while (pair.getKey() != null && pair.getValue() != null) {
					pairs.add(pair);
					pair = (KeyValuePair) in.readObject();
				}
				return pairs;
			} catch (Exception e) {
				throw e;
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
}
