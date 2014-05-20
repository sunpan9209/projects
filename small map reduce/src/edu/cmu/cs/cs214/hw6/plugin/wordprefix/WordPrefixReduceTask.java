package edu.cmu.cs.cs214.hw6.plugin.wordprefix;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.cmu.cs.cs214.hw6.Emitter;
import edu.cmu.cs.cs214.hw6.ReduceTask;

/**
 * The reduce task for a word-prefix map/reduce computation.
 */
public class WordPrefixReduceTask implements ReduceTask {
	private static final long serialVersionUID = 6763871961687287020L;

	@Override
	public void execute(String key, Iterator<String> values, Emitter emitter)
			throws IOException {
		Map<String, Integer> counter = new HashMap<String, Integer>();
		while (values.hasNext()) {
			String word = values.next();
			if (counter.get(word) == null) {
				counter.put(word, 1);
			} else {
				int prev = counter.get(word);
				counter.put(word, prev + 1);
			}
		}
		int maxNum = Collections.max(counter.values());
		for (Entry<String, Integer> r : counter.entrySet()) {
			if (r.getValue() == maxNum) {
				emitter.emit(key, r.getKey());
				emitter.close();
				return;
			}
		}
		emitter.close();
		return;
	}

}
