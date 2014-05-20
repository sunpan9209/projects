package edu.cmu.cs.cs214.hw6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * emit key/value pairs into the file system.
 */
public class ResultEmitter implements Emitter {
	private PrintWriter writer;
	private FileOutputStream stream;

	public ResultEmitter(File file) {
		try {
			this.stream = new FileOutputStream(file);
			this.writer = new PrintWriter(stream, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		stream.flush();
		stream.close();
		writer.flush();
		writer.close();
	}

	@Override
	public void emit(String key, String value) throws IOException {
		writer.write(key + " " + value + "\r\n");
		writer.flush();
	}
}
