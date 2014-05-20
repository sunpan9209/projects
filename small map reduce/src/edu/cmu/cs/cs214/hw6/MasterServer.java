package edu.cmu.cs.cs214.hw6;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.cmu.cs.cs214.hw6.util.Log;
import edu.cmu.cs.cs214.hw6.util.StaffUtils;

/**
 * This class represents the "master server" in the distributed map/reduce
 * framework. The {@link MasterServer} is in charge of managing the entire
 * map/reduce computation from beginning to end. The {@link MasterServer}
 * listens for incoming client connections on a distinct host/port address, and
 * is passed an array of {@link WorkerInfo} objects when it is first initialized
 * that provides it with necessary information about each of the available
 * workers in the system (i.e. each worker's name, host address, port number,
 * and the set of {@link Partition}s it stores). A single map/reduce computation
 * managed by the {@link MasterServer} will typically behave as follows:
 * 
 * <ol>
 * <li>Wait for the client to submit a map/reduce task.</li>
 * <li>Distribute the {@link MapTask} across a set of "map-workers" and wait for
 * all map-workers to complete.</li>
 * <li>Distribute the {@link ReduceTask} across a set of "reduce-workers" and
 * wait for all reduce-workers to complete.</li>
 * <li>Write the locations of the final results files back to the client.</li>
 * </ol>
 */
public class MasterServer extends Thread {
	private final int mPort;
	private final List<WorkerInfo> mWorkers;
	private static final String TAG = "Master";
	private static final int POOL_SIZE = Runtime.getRuntime()
			.availableProcessors();
	private final ExecutorService mExecutor;

	/**
	 * The {@link MasterServer} constructor.
	 * 
	 * @param masterPort
	 *            The port to listen on.
	 * @param workers
	 *            Information about each of the available workers in the system.
	 */
	public MasterServer(int masterPort, List<WorkerInfo> workers) {
		mPort = masterPort;
		mWorkers = workers;
		mExecutor = Executors.newFixedThreadPool(POOL_SIZE);
	}

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(mPort);
			} catch (IOException e) {
				Log.e(TAG, "Could not open server socket on port " + mPort
						+ ".", e);
				return;
			}
			Log.i(TAG, "Listening for incoming tasks on port " + mPort + ".");
			while (true) {
				try {
					Socket clientSocket = serverSocket.accept();
					mExecutor.execute(new ClientTaskHandler(clientSocket,
							mWorkers));
				} catch (IOException e) {
					Log.e(TAG,
							"Error while listening for incoming connections.",
							e);
					break;
				}
			}
			Log.i(TAG, "Shutting down...");
			try {
				serverSocket.close();
			} catch (IOException e) {
				// Ignore because we're about to exit anyway.
			}
		} finally {
			mExecutor.shutdown();
		}
	}

	/**
	 * Handles tasks from a client
	 */
	private static class ClientTaskHandler implements Runnable {
		private final Socket mSocket;
		private List<WorkerInfo> workers;
		private Map<String, List<String>> groups;
		private Map<String, String> distribution;
		private ExecutorService mExecutor;
		private static final String TAG = "Master";
		private static final int MAX_POOL_SIZE = Runtime.getRuntime()
				.availableProcessors();

		public ClientTaskHandler(Socket socket, List<WorkerInfo> mWorkers) {
			mSocket = socket;
			workers = mWorkers;
			int numThreads = Math.min(MAX_POOL_SIZE, workers.size());
			mExecutor = Executors.newFixedThreadPool(numThreads);
			groups = new HashMap<String, List<String>>();
			distribution = new HashMap<String, String>();
			partitionByWorker(workers);
			distribute();
		}

		/**
		 * divide partitions by workers
		 * 
		 * @param mWorkers
		 */
		private void partitionByWorker(List<WorkerInfo> mWorkers) {
			for (WorkerInfo worker : mWorkers) {
				for (Partition part : worker.getPartitions()) {
					if (groups.get(part.getPartitionName()) == null) {
						groups.put(part.getPartitionName(),
								new ArrayList<String>());
						groups.get(part.getPartitionName()).add(
								worker.getName());
					} else {
						groups.get(part.getPartitionName()).add(
								worker.getName());
					}
				}
			}
		}

		/**
		 * randomly pick workers for partitions.
		 */
		private void distribute() {
			for (Entry<String, List<String>> r : groups.entrySet()) {
				Random random = new Random();
				int randomIndex = random.nextInt(r.getValue().size());
				distribution.put(r.getKey(), r.getValue().get(randomIndex));
			}
		}

		/**
		 * distributing partitions to workers
		 * 
		 * @param worker
		 * @return mapped partitions
		 */
		private List<Partition> partitionForSending(WorkerInfo worker) {
			List<Partition> partitions = new ArrayList<Partition>();
			for (Entry<String, String> r : distribution.entrySet()) {
				if (r.getValue().equals(worker.getName())) {
					for (Partition part : worker.getPartitions()) {
						if (part.getPartitionName().equals(r.getKey())) {
							partitions.add(part);
						}
					}
				}
			}
			return partitions;
		}

		@Override
		public void run() {
			try {
				ObjectInputStream in = new ObjectInputStream(
						mSocket.getInputStream());
				MapTask map = (MapTask) in.readObject();
				ReduceTask reduce = (ReduceTask) in.readObject();
				Map<WorkerInfo, String> infos = new HashMap<WorkerInfo, String>();
				List<String> paths = new ArrayList<String>();
				try {
					List<MapCallable> mapCallables = new ArrayList<MapCallable>();
					for (WorkerInfo worker : workers) {
						List<Partition> partitions = partitionForSending(worker);
						if (partitions != null) {
							mapCallables.add(new MapCallable(map, worker,
									partitions));
						}
					}
					List<Future<String>> responses = null;
					try {
						responses = mExecutor.invokeAll(mapCallables);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					for (int i = 0; i < responses.size(); i++) {
						MapCallable mapCallable = mapCallables.get(i);
						Future<String> response = responses.get(i);
						try {
							infos.put(mapCallable.getWorker(), response.get());
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						} catch (ExecutionException e) {
							String workerHost = mapCallable.getWorker()
									.getHost();
							int workerPort = mapCallable.getWorker().getPort();
							String info = String.format("[host=%s, port=%d]",
									workerHost, workerPort);
							Log.e(TAG,
									"Warning! Failed to execute maptask for worker: "
											+ info, e.getCause());
						}
					}
				} finally {
					mExecutor.shutdown();
				}
				try {
					int numThreads = Math.min(MAX_POOL_SIZE, workers.size());
					mExecutor = Executors.newFixedThreadPool(numThreads);
					List<ReduceCallable> reduceCallables = new ArrayList<ReduceCallable>();
					for (int i = 0; i < workers.size(); i++) {
						reduceCallables.add(new ReduceCallable(reduce, workers
								.get(i), infos, i, workers.size()));
					}
					List<Future<String>> results = null;
					try {
						results = mExecutor.invokeAll(reduceCallables);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					for (int i = 0; i < results.size(); i++) {
						ReduceCallable reduceCallable = reduceCallables.get(i);
						Future<String> result = results.get(i);
						try {
							paths.add(result.get());
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						} catch (ExecutionException e) {
							String workerHost = reduceCallable.getWorker()
									.getHost();
							int workerPort = reduceCallable.getWorker()
									.getPort();
							String info = String.format("[host=%s, port=%d]",
									workerHost, workerPort);
							Log.e(TAG,
									"Warning! Failed to execute reducetask for worker: "
											+ info, e.getCause());
						}
					}
				} finally {
					mExecutor.shutdown();
				}
				ObjectOutputStream out = new ObjectOutputStream(
						mSocket.getOutputStream());
				out.writeObject(paths);
			} catch (IOException e) {
				Log.e(TAG, "Connection lost.", e);
			} catch (ClassNotFoundException e) {
				Log.e(TAG, "Received invalid task from client.", e);
			} finally {
				try {
					mSocket.close();
				} catch (IOException e) {
					// Ignore because we're about to exit anyway.
				}
			}
		}

		/**
		 * send map command to worker
		 */
		private static class MapCallable implements Callable<String> {
			private final MapTask map;
			private final WorkerInfo worker;
			private final List<Partition> partitions;

			public MapCallable(MapTask map, WorkerInfo worker,
					List<Partition> partitions) {
				this.map = map;
				this.worker = worker;
				this.partitions = partitions;
			}

			/**
			 * Returns the {@link WorkerInfo} object that provides information
			 * about the worker that this callable task is responsible for
			 * interacting with.
			 */
			public WorkerInfo getWorker() {
				return worker;
			}

			@Override
			public String call() throws Exception {
				Socket socket = null;
				try {
					socket = new Socket(worker.getHost(), worker.getPort());
					ObjectOutputStream out = new ObjectOutputStream(
							socket.getOutputStream());
					out.writeObject(new MapWorkerCommand(map, partitions));
					ObjectInputStream in = new ObjectInputStream(
							socket.getInputStream());
					return (String) in.readObject();
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

		/**
		 * send reduce command to worker
		 */
		private static class ReduceCallable implements Callable<String> {
			private final ReduceTask reduce;
			private final WorkerInfo worker;
			private final Map<WorkerInfo, String> infos;
			private final int signal;
			private final int size;

			public ReduceCallable(ReduceTask reduce, WorkerInfo worker,
					Map<WorkerInfo, String> infos, int signal, int size) {
				this.reduce = reduce;
				this.worker = worker;
				this.infos = infos;
				this.signal = signal;
				this.size = size;
			}

			/**
			 * Returns the {@link WorkerInfo} object that provides information
			 * about the worker that this callable task is responsible for
			 * interacting with.
			 */
			public WorkerInfo getWorker() {
				return worker;
			}

			@Override
			public String call() throws Exception {
				Socket socket = null;
				try {
					socket = new Socket(worker.getHost(), worker.getPort());
					ObjectOutputStream out = new ObjectOutputStream(
							socket.getOutputStream());
					out.writeObject(new ReduceWorkerCommand(reduce, infos,
							worker.getName(), signal, size));
					ObjectInputStream in = new ObjectInputStream(
							socket.getInputStream());
					return (String) in.readObject();
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

	/********************************************************************/
	/***************** STAFF CODE BELOW. DO NOT MODIFY. *****************/
	/********************************************************************/

	/**
	 * Starts the master server on a distinct port. Information about each
	 * available worker in the distributed system is parsed and passed as an
	 * argument to the {@link MasterServer} constructor. This information can be
	 * either specified via command line arguments or via system properties
	 * specified in the <code>master.properties</code> and
	 * <code>workers.properties</code> file (if no command line arguments are
	 * specified).
	 */
	public static void main(String[] args) {
		StaffUtils.makeMasterServer(args).start();
	}

}
