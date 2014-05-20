package edu.cmu.cs.cs214.hw4.core;

import edu.cmu.cs.cs214.hw4.tile.NormalTile;
import edu.cmu.cs.cs214.hw4.tile.SpecialTile;

public class Location {
	private int row;
	private int col;
	private NormalTile myTile;
	private boolean isEmpty;
	private SpecialTile special;
	private boolean hasSpecialTile;
	private Factor factor;

	/**
	 * A location can have only one normal tile and one special tile. Multiplier
	 * can be reused.
	 * 
	 * @param x
	 *            row
	 * @param y
	 *            column
	 * @param factor
	 *            multiplier
	 */
	public Location(int x, int y, Factor factor) {
		row = x;
		col = y;
		isEmpty = true;
		hasSpecialTile = false;
		this.factor = factor;
		myTile = null;
		special = null;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Factor getFactor() {
		return factor;
	}

	/**
	 * put a special tile on this location
	 * 
	 * @param tile
	 *            special tile
	 * @return true if empty
	 */
	public boolean setSpecialTile(SpecialTile tile) {
		if (tile == null)
			return false;
		// can't override own placed special tile
		if (hasSpecialTile) {
			if (special.getOwner() == tile.getOwner()) {
				return false;
			}
		}
		special = tile;
		hasSpecialTile = true;
		return true;
	}

	/**
	 * put a normal tile on this location
	 * 
	 * @param tile
	 *            normal tile
	 * @return true if empty
	 */
	public boolean setTile(NormalTile tile) {
		if (!isEmpty)
			return false;
		myTile = tile;
		isEmpty = false;
		return true;
	}

	public boolean hasSpecialTile() {
		return hasSpecialTile;
	}

	public SpecialTile getSpecialTile() {
		return special;
	}

	/**
	 * this location is at the center of the board
	 * 
	 * @return true if at center
	 */
	public boolean isCenter() {
		return ((row == 7) && (col == 7));
	}

	/**
	 * remove SpecialTile
	 * 
	 * @return SpecialTile or null
	 */
	public SpecialTile removeSpecialTile() {
		SpecialTile tile = special;
		special = null;
		hasSpecialTile = false;
		return tile;
	}

	/**
	 * remove NormalTile
	 * 
	 * @return NormalTile or null
	 */
	public NormalTile removeTile() {
		if (isEmpty)
			return null;
		NormalTile tile = myTile;
		myTile = null;
		isEmpty = true;
		return tile;
	}

	public NormalTile getTile() {
		return myTile;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Location))
			return false;
		Location p = (Location) obj;
		return p.getRow() == row && p.getCol() == col;
	}

	@Override
	public int hashCode() {
		return row * (col + 1);
	}
}
