package edu.cmu.cs.cs214.hw4.tile;

public class NormalTile {
	private char letter;
	private int value;

	/**
	 * A normal tile has a fixed value and letter
	 * 
	 * @param letter
	 * @param value
	 */
	public NormalTile(char letter, int value) {
		this.letter = letter;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public char getLetter() {
		return letter;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NormalTile) {
			NormalTile tile = (NormalTile) obj;
			return letter == tile.getLetter() && value == tile.getValue();
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return value;
	}
}
