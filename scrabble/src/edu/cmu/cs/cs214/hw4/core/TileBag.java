package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;
import java.util.Random;

import edu.cmu.cs.cs214.hw4.tile.NormalTile;

public class TileBag {
	private ArrayList<NormalTile> tiles;
	private int tileNum;

	/**
	 * a tile bag has all initialized available tiles, game take a random tile
	 * from bag.
	 */
	public TileBag() {
		tiles = new ArrayList<NormalTile>();
		tileNum = 0;
		init();
	}

	/**
	 * put a tile back into the bag
	 * 
	 * @param tile
	 *            NormalTile
	 */
	public void addTile(NormalTile tile) {
		tiles.add(tile);
		tileNum++;
	}

	private void init() {
		for (int i = 1; i <= 12; i++) {
			addTile(new NormalTile('e', 1));
			if (i <= 9) {
				addTile(new NormalTile('a', 1));
				addTile(new NormalTile('i', 1));
			}
			if (i <= 8) {
				addTile(new NormalTile('o', 1));
			}
			if (i <= 6) {
				addTile(new NormalTile('n', 1));
				addTile(new NormalTile('r', 1));
				addTile(new NormalTile('t', 1));
			}
			if (i <= 4) {
				addTile(new NormalTile('l', 1));
				addTile(new NormalTile('s', 1));
				addTile(new NormalTile('u', 1));
				addTile(new NormalTile('d', 2));
			}
			if (i <= 3) {
				addTile(new NormalTile('g', 2));
			}
			if (i <= 2) {
				addTile(new NormalTile('b', 3));
				addTile(new NormalTile('c', 3));
				addTile(new NormalTile('m', 3));
				addTile(new NormalTile('p', 3));
				addTile(new NormalTile('f', 4));
				addTile(new NormalTile('h', 4));
				addTile(new NormalTile('v', 4));
				addTile(new NormalTile('w', 4));
				addTile(new NormalTile('y', 4));
			}
			if (i <= 1) {
				addTile(new NormalTile('k', 5));
				addTile(new NormalTile('j', 8));
				addTile(new NormalTile('x', 8));
				addTile(new NormalTile('q', 10));
				addTile(new NormalTile('z', 10));
			}
		}
	}

	public int getTileNum() {
		return tileNum;
	}

	public boolean isEmpty() {
		return tileNum == 0;
	}

	public ArrayList<NormalTile> getTileBag() {
		return tiles;
	}

	private NormalTile getRandomTile() {
		Random random = new Random();
		int randomIndex = random.nextInt(tileNum);
		NormalTile tile = tiles.get(randomIndex);
		tiles.remove(randomIndex);
		tileNum = tiles.size();
		return tile;
	}

	/**
	 * take a random tile from the bag
	 * 
	 * @return NormalTile or null if no tile in bag
	 */
	public NormalTile takeTile() {
		if (tileNum > 0) {
			return getRandomTile();
		}
		return null;
	}
}
