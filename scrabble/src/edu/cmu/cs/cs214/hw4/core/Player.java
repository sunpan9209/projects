package edu.cmu.cs.cs214.hw4.core;

import edu.cmu.cs.cs214.hw4.tile.NormalTile;
import edu.cmu.cs.cs214.hw4.tile.SpecialTile;

public class Player {
	private String name;
	private Rack rack;
	private int score;

	/**
	 * A player can only manage his own rack
	 * 
	 * @param name
	 *            player's name
	 */
	public Player(String name) {
		this.name = name;
		rack = new Rack();
		score = 0;
	}

	public int getScore() {
		return score;
	}

	public String getName() {
		return name;
	}

	public Rack getRack() {
		return rack;
	}

	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * put special tile into rack and minus the price if score high enough
	 * 
	 * @param tile
	 *            SpecialTile
	 * @return true on success
	 */
	public boolean addSpecialTile(SpecialTile tile) {
		int price = tile.getPrice();
		if (score < price)
			return false;
		if (rack.addSpecialTile(tile)) {
			score -= price;
			return true;
		}
		return false;
	}

	/**
	 * remove special tile from rack
	 * 
	 * @param tile
	 *            SpecialTile
	 */
	public void removeSpecialTile(SpecialTile tile) {
		rack.getSpecialRack().remove(tile);
	}

	/**
	 * add normal tile into rack
	 * 
	 * @param tile
	 *            NormalTile
	 */
	public void addNormalTile(NormalTile tile) {
		rack.addNormalTile(tile);
	}

	/**
	 * remove normal tile from rack
	 * 
	 * @param tile
	 *            NormalTile
	 */
	public void removeNormalTile(NormalTile tile) {
		rack.removeNormalTile(tile);
	}
}
