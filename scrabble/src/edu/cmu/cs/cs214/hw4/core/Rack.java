package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

import edu.cmu.cs.cs214.hw4.tile.NormalTile;
import edu.cmu.cs.cs214.hw4.tile.SpecialTile;

public class Rack {
	private static final int MAXLETTERS = 7;
	private static final int MAXSPECIALTILES = 4;
	private NormalTile[] normalRack; // use array for the order
	private ArrayList<SpecialTile> specialRack;
	private int normalNum;

	/**
	 * rack could be a container for NormalTile and SpecialTile
	 */
	public Rack() {
		normalRack = new NormalTile[MAXLETTERS];
		specialRack = new ArrayList<SpecialTile>();
		normalNum = 0;
	}

	/**
	 * get all normal tiles in rack
	 * 
	 * @return array of tiles with the same order
	 */
	public NormalTile[] getNormalRack() {
		NormalTile[] rack = new NormalTile[normalNum];
		int index = 0;
		for (NormalTile tile : normalRack) {
			if (tile != null) {
				rack[index] = tile;
				index++;
			}
		}
		return rack;
	}

	public int getNormalNum() {
		return normalNum;
	}

	public ArrayList<SpecialTile> getSpecialRack() {
		return specialRack;
	}

	public int getSpecialNum() {
		return specialRack.size();
	}

	public boolean isEmpty() {
		return (normalNum == 0);
	}

	public boolean isFull() {
		return (normalNum == MAXLETTERS);
	}

	/**
	 * set null the NormalTile
	 * 
	 * @param tile
	 *            NormalTile
	 */
	public void addNormalTile(NormalTile tile) {
		for (int i = 0; i < MAXLETTERS; i++) {
			if (normalRack[i] == null) {
				normalRack[i] = tile;
				normalNum++;
				return;
			}
		}
	}

	/**
	 * set NormalTile null
	 * 
	 * @param tile
	 *            NormalTile
	 */
	public void removeNormalTile(NormalTile tile) {
		for (int i = 0; i < MAXLETTERS; i++) {
			if (normalRack[i] == tile) {
				normalRack[i] = null;
				normalNum--;
				return;
			}
		}
	}

	/**
	 * put a special tile into this rack
	 * 
	 * @param tile
	 *            SpecialTile
	 * @return true if rack is not full
	 */
	public boolean addSpecialTile(SpecialTile tile) {
		if (specialRack.size() >= MAXSPECIALTILES)
			return false;
		specialRack.add(tile);
		return true;
	}
}
