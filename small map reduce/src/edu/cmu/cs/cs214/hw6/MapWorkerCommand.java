package edu.cmu.cs.cs214.hw6;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import edu.cmu.cs.cs214.hw6.util.Log;
import edu.cmu.cs.cs214.hw6.util.WorkerStorage;

/**
 * execute a map task on a server.
 */
public class MapWorkerCommand extends WorkerCommand {
	private static final long serialVersionUID = -6204676313327356024L;
	private final MapTask map;
	private final List<Partition> partitions;
	private static final String TAG = "MapWorkerCommand";

	public MapWorkerCommand(MapTask map, List<Partition> partitions) {
		this.map = map;
		this.partitions = partitions;
	}

	@Override
	public void run() {
		Socket socket = getSocket();
		String workerName = partitions.get(0).getWorkerName();
		String uniqueName = workerName + System.currentTimeMillis();
		File file = new File(
				WorkerStorage.getIntermediateResultsDirectory(workerName),
				uniqueName);
		ResultEmitter emitter = new ResultEmitter(file);
		for (Partition part : partitions) {
			Iterator<File> iterator = part.iterator();
			while (iterator.hasNext()) {
				try {
					FileInputStream in = new FileInputStream(iterator.next());
					map.execute(in, emitter);
					in.close();
				} catch (IOException e) {
					Log.e(TAG, "I/O error for file.", e);
				}
			}
		}
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			out.writeObject(uniqueName);
		} catch (IOException e) {
			Log.e(TAG, "I/O error while executing maptask.", e);
		}
	}
}
