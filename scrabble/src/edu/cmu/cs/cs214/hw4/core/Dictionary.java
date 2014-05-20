package edu.cmu.cs.cs214.hw4.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public final class Dictionary {
	private static HashSet<String> dictSet;

	/**
	 * a dictionary that contains all valid words
	 */
	static {
		dictSet = new HashSet<String>();
		Scanner scanner = null;
		try {
			File file = new File("assets/words.txt");
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String word = scanner.nextLine();
				dictSet.add(word);
			}
		} catch (FileNotFoundException e) {
			System.err.print("This is no such file");
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	/**
	 * check if a word is valid
	 * 
	 * @param word
	 *            possible word
	 * @return true if valid
	 */
	public boolean contain(String word) {
		return dictSet.contains(word);
	}
}
