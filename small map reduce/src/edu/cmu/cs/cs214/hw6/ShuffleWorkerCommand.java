package edu.cmu.cs.cs214.hw6;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import edu.cmu.cs.cs214.hw6.util.KeyValuePair;
import edu.cmu.cs.cs214.hw6.util.Log;
import edu.cmu.cs.cs214.hw6.util.WorkerStorage;

/**
 * execute a shuffle task on a map server
 */
public class ShuffleWorkerCommand extends WorkerCommand {
	private static final long serialVersionUID = -42141145088770485L;
	private final int signal;
	private final String workerName;
	private final String fileName;
	private final int size;
	private static final String TAG = "ShuffleWorkerCommand";

	public ShuffleWorkerCommand(int signal, String workerName, String fileName,
			int size) {
		this.signal = signal;
		this.workerName = workerName;
		this.fileName = fileName;
		this.size = size;
	}

	@Override
	public void run() {
		Socket socket = getSocket();
		File file = new File(
				WorkerStorage.getIntermediateResultsDirectory(workerName),
				fileName);
		try {
			FileInputStream in = new FileInputStream(file);
			Scanner scanner = new Scanner(in);
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Scanner lineBreaker = new Scanner(line);
				String key = lineBreaker.next();
				String value = lineBreaker.next();
				if ((Math.abs(key.hashCode() % size) == signal)) {
					KeyValuePair pair = new KeyValuePair(key, value);
					out.writeObject(pair);
					out.reset();
				}
				lineBreaker.close();
			}
			scanner.close();
			in.close();
			KeyValuePair end = new KeyValuePair(null, null);
			out.writeObject(end);
			out.reset();
		} catch (IOException e) {
			Log.e(TAG, "I/O error while executing shuffletask.", e);
		}
	}

}
