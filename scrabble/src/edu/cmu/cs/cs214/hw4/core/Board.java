package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

import edu.cmu.cs.cs214.hw4.tile.NormalTile;
import edu.cmu.cs.cs214.hw4.tile.SpecialTile;

public class Board {
	private Location[][] locations;
	private Location[] playedLocations; // record played locations every turn
	private int playedLocationNum;
	private static final int LENGTH = 15;
	private static final int MAXPLACEDLOCATION = 7;
	private boolean hasCenterTile;
	private static final Factor[][] factors = {
			{ Factor.TW, Factor.NS, Factor.NS, Factor.DL, Factor.NS, Factor.NS,
					Factor.NS, Factor.TW, Factor.NS, Factor.NS, Factor.NS,
					Factor.DL, Factor.NS, Factor.NS, Factor.TW, },
			{ Factor.NS, Factor.DW, Factor.NS, Factor.NS, Factor.NS, Factor.TL,
					Factor.NS, Factor.NS, Factor.NS, Factor.TL, Factor.NS,
					Factor.NS, Factor.NS, Factor.DW, Factor.NS, },
			{ Factor.NS, Factor.NS, Factor.DW, Factor.NS, Factor.NS, Factor.NS,
					Factor.DL, Factor.NS, Factor.DL, Factor.NS, Factor.NS,
					Factor.NS, Factor.DW, Factor.NS, Factor.NS, },
			{ Factor.DL, Factor.NS, Factor.NS, Factor.DW, Factor.NS, Factor.NS,
					Factor.NS, Factor.DL, Factor.NS, Factor.NS, Factor.NS,
					Factor.DW, Factor.NS, Factor.NS, Factor.DL, },
			{ Factor.NS, Factor.NS, Factor.NS, Factor.NS, Factor.DW, Factor.NS,
					Factor.NS, Factor.NS, Factor.NS, Factor.NS, Factor.DW,
					Factor.NS, Factor.NS, Factor.NS, Factor.NS, },
			{ Factor.NS, Factor.TL, Factor.NS, Factor.NS, Factor.NS, Factor.TL,
					Factor.NS, Factor.NS, Factor.NS, Factor.TL, Factor.NS,
					Factor.NS, Factor.NS, Factor.TL, Factor.NS, },
			{ Factor.NS, Factor.NS, Factor.DL, Factor.NS, Factor.NS, Factor.NS,
					Factor.DL, Factor.NS, Factor.DL, Factor.NS, Factor.NS,
					Factor.NS, Factor.DL, Factor.NS, Factor.NS, },
			{ Factor.TW, Factor.NS, Factor.NS, Factor.DL, Factor.NS, Factor.NS,
					Factor.NS, Factor.TL, Factor.NS, Factor.NS, Factor.NS,
					Factor.DL, Factor.NS, Factor.NS, Factor.TW, },
			{ Factor.NS, Factor.NS, Factor.DL, Factor.NS, Factor.NS, Factor.NS,
					Factor.DL, Factor.NS, Factor.DL, Factor.NS, Factor.NS,
					Factor.NS, Factor.DL, Factor.NS, Factor.NS, },
			{ Factor.NS, Factor.TL, Factor.NS, Factor.NS, Factor.NS, Factor.TL,
					Factor.NS, Factor.NS, Factor.NS, Factor.TL, Factor.NS,
					Factor.NS, Factor.NS, Factor.TL, Factor.NS, },
			{ Factor.NS, Factor.NS, Factor.NS, Factor.NS, Factor.DW, Factor.NS,
					Factor.NS, Factor.NS, Factor.NS, Factor.NS, Factor.DW,
					Factor.NS, Factor.NS, Factor.NS, Factor.NS, },
			{ Factor.DL, Factor.NS, Factor.NS, Factor.DW, Factor.NS, Factor.NS,
					Factor.NS, Factor.DL, Factor.NS, Factor.NS, Factor.NS,
					Factor.DW, Factor.NS, Factor.NS, Factor.DL, },
			{ Factor.NS, Factor.NS, Factor.DW, Factor.NS, Factor.NS, Factor.NS,
					Factor.DL, Factor.NS, Factor.DL, Factor.NS, Factor.NS,
					Factor.NS, Factor.DW, Factor.NS, Factor.NS, },
			{ Factor.NS, Factor.DW, Factor.NS, Factor.NS, Factor.NS, Factor.TL,
					Factor.NS, Factor.NS, Factor.NS, Factor.TL, Factor.NS,
					Factor.NS, Factor.NS, Factor.DW, Factor.NS, },
			{ Factor.TW, Factor.NS, Factor.NS, Factor.DL, Factor.NS, Factor.NS,
					Factor.NS, Factor.TW, Factor.NS, Factor.NS, Factor.NS,
					Factor.DL, Factor.NS, Factor.NS, Factor.TW, } };

	/**
	 * board controls and records all locations, thus has access to played tiles
	 * So board takes responsibilities like finding words, calculating score,
	 * applying effects of special tiles to game and so on
	 */
	public Board() {
		hasCenterTile = false;
		locations = new Location[LENGTH][LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < LENGTH; j++) {
				locations[i][j] = new Location(i, j, factors[i][j]);
			}
		}
		reset();
	}

	/**
	 * add a tile next to another tile or at the center
	 * 
	 * @param tile
	 *            NormalTile
	 * @param loc
	 *            location on board
	 * @return true on success
	 */
	public boolean addTile(NormalTile tile, Location loc) {
		if (!hasCenterTile && !loc.equals(locations[LENGTH / 2][LENGTH / 2]))
			return false;
		if (loc.equals(locations[LENGTH / 2][LENGTH / 2])) {
			hasCenterTile = true;
			return loc.setTile(tile);
		} else if (hasNeighbors(loc)) // if next to some square with tile
			return loc.setTile(tile);
		else
			return false;
	}

	/**
	 * add a SpecialTile on board but special tile can not be put above a normal
	 * tile.
	 * 
	 * @param tile
	 *            SpecialTile
	 * @param loc
	 *            location on board
	 * @return true if the location is empty
	 */
	public boolean addSpecialTile(SpecialTile tile, Location loc) {
		if (!loc.isEmpty()) // already has a normal tile
			return false;
		return loc.setSpecialTile(tile);
	}

	private boolean hasNeighbors(Location loc) {
		int x = loc.getRow();
		int y = loc.getCol();
		// left and right
		for (int i = x - 1; i <= x + 1; i += 2)
			if (i >= 0 && i < LENGTH && !locations[i][y].isEmpty())
				return true;
		// up and down
		for (int j = y - 1; j <= y + 1; j += 2)
			if (j >= 0 && j < LENGTH && !locations[x][j].isEmpty())
				return true;
		return false;
	}

	/**
	 * remove NormalTile from a location on board which could be the center.
	 * 
	 * @param loc
	 *            Location
	 * @return removed NormalTile
	 */
	public NormalTile removeTile(Location loc) {
		NormalTile tile = loc.removeTile();
		if (loc.equals(locations[LENGTH / 2][LENGTH / 2])) // if center is
															// removed
			hasCenterTile = false;
		return tile;
	}

	/**
	 * remove SpecialTile from a location on board including the center.
	 * 
	 * @param loc
	 *            Location
	 * @return removed SpecialTile
	 */
	public SpecialTile removeSpecial(Location loc) {
		return loc.removeSpecialTile();
	}

	public int getPlayedLocationNum() {
		return playedLocationNum;
	}

	public int getLength() {
		return LENGTH;
	}

	public Location[] getPlayedLocations() {
		return playedLocations;
	}

	/**
	 * remove tile from a recent clicked location by hand
	 * 
	 * @param loc
	 *            already recorded location
	 * @return NormalTile
	 */
	public NormalTile takePlacedNormalTile(Location loc) {
		// nothing played
		if (playedLocationNum == 0)
			return null;
		// remove the last firstly
		if (!playedLocations[playedLocationNum - 1].equals(loc))
			return null;
		NormalTile tile = removeTile(loc);
		if (tile == null)
			return null;
		playedLocations[playedLocationNum - 1] = null;
		playedLocationNum--;
		return tile;
	}

	private boolean sameRow(Location loc) {
		int col = loc.getCol();
		int left = loc.getRow();
		int right = left;
		// find the boundary
		for (int i = 0; i < MAXPLACEDLOCATION; i++) {
			if (playedLocations[i] != null) {
				if (playedLocations[i].getCol() != col)
					return false;
				else if (playedLocations[i].getRow() < left)
					left = playedLocations[i].getRow();
				else if (playedLocations[i].getRow() > right)
					right = playedLocations[i].getRow();
			}
		}
		// check if in the same row
		for (int i = left + 1; i < right; i++)
			if (locations[i][col].isEmpty())
				return false;
		return true;
	}

	private boolean sameCol(Location loc) {
		int row = loc.getRow();
		int top = loc.getCol();
		int bottom = top;
		// find the boundary
		for (int i = 0; i < MAXPLACEDLOCATION; i++) {
			if (playedLocations[i] != null) {
				if (playedLocations[i].getRow() != row)
					return false;
				else if (playedLocations[i].getCol() < top)
					top = playedLocations[i].getCol();
				else if (playedLocations[i].getCol() > bottom)
					bottom = playedLocations[i].getCol();
			}
		}
		// check if in the same column
		for (int i = top + 1; i < bottom; i++)
			if (locations[row][i].isEmpty())
				return false;
		return true;
	}

	/**
	 * check if a location is in the same line with other placed locations
	 * 
	 * @param loc
	 *            Location
	 * @return true if on the same line
	 */
	public boolean sameLine(Location loc) {
		if (playedLocationNum != 0 && !(sameRow(loc) || sameCol(loc))) {
			return false;
		}
		return true;
	}

	private String getRowWord(Location loc) {
		int row = loc.getRow();
		int col = loc.getCol();
		int left;
		int right;
		// find the boundary
		for (left = row; left >= 0; left--)
			if (locations[left][col].isEmpty())
				break;
		for (right = row; right < LENGTH; right++)
			if (locations[right][col].isEmpty())
				break;
		left++;
		// make it to be string
		char[] word = new char[right - left];
		for (int i = left; i < right; i++)
			word[i - left] = locations[i][col].getTile().getLetter();
		return new String(word);
	}

	private String getColWord(Location loc) {
		int row = loc.getRow();
		int col = loc.getCol();
		int top;
		int bottom;
		// find the boundary
		for (top = col; top >= 0; top--)
			if (locations[row][top].isEmpty())
				break;
		for (bottom = col; bottom < LENGTH; bottom++)
			if (locations[row][bottom].isEmpty())
				break;
		top++;
		// make it to be string
		char[] word = new char[bottom - top];
		for (int i = top; i < bottom; i++)
			word[i - top] = locations[row][i].getTile().getLetter();
		return new String(word);
	}

	/**
	 * get all possible words from all recent played locations which may not be
	 * valid
	 * 
	 * @return all possible words, null if no word found
	 */
	public ArrayList<String> getWords() {
		ArrayList<String> words = new ArrayList<String>();
		if (playedLocationNum == 0) {
			return null;
		}
		if (playedLocationNum == 1) {
			if (getRowWord(playedLocations[0]).length() > 1)
				words.add(getRowWord(playedLocations[0]));
			if (getColWord(playedLocations[0]).length() > 1)
				words.add(getColWord(playedLocations[0]));
			if ((getRowWord(playedLocations[0]).length() <= 1)
					&& (getColWord(playedLocations[0]).length() <= 1))
				words.add(getRowWord(playedLocations[0]));
			return words;
		}
		// check if row word
		if (sameRow(playedLocations[0])) {
			words.add(getRowWord(playedLocations[0]));
			for (Location loc : playedLocations) {
				if (loc == null)
					continue;
				String word = getColWord(loc);
				if (word.length() > 1) {
					words.add(word);
				}
			}
			// check if column word
		} else if (sameCol(playedLocations[0])) {
			words.add(getColWord(playedLocations[0]));
			for (Location loc : playedLocations) {
				if (loc == null)
					continue;
				String word = getRowWord(loc);
				if (word.length() > 1) {
					words.add(word);
				}
			}
		}
		return words;
	}

	private int rowScore(Location loc) {
		int row = loc.getRow();
		int col = loc.getCol();
		int left;
		int right;
		// find the boundary
		for (left = row; left >= 0; left--)
			if (locations[left][col].isEmpty())
				break;
		for (right = row; right < LENGTH; right++)
			if (locations[right][col].isEmpty())
				break;
		left++;
		int score = 0;
		int multiplier = 1;
		// score multiplied by the factor
		for (int i = left; i < right; i++) {
			Factor factor = locations[i][col].getFactor();
			switch (factor) {
			case NS:
				score += locations[i][col].getTile().getValue();
				break;
			case DL:
				score += locations[i][col].getTile().getValue() * 2;
				break;
			case DW:
				score += locations[i][col].getTile().getValue();
				multiplier = multiplier * 2;
				break;
			case TL:
				score += locations[i][col].getTile().getValue() * 3;
				break;
			case TW:
				score += locations[i][col].getTile().getValue();
				multiplier = multiplier * 3;
				break;
			}
		}
		return (score * multiplier);
	}

	private int colScore(Location loc) {
		int row = loc.getRow();
		int col = loc.getCol();
		int top;
		int bottom;
		// find the boundary
		for (top = col; top >= 0; top--)
			if (locations[row][top].isEmpty())
				break;
		for (bottom = col; bottom < LENGTH; bottom++)
			if (locations[row][bottom].isEmpty())
				break;
		top++;
		int score = 0;
		int multiplier = 1;
		// score multiplied by the factor
		for (int i = top; i < bottom; i++) {
			Factor factor = locations[row][i].getFactor();
			switch (factor) {
			case NS:
				score += locations[row][i].getTile().getValue();
				break;
			case DL:
				score += locations[row][i].getTile().getValue() * 2;
				break;
			case DW:
				score += locations[row][i].getTile().getValue();
				multiplier = multiplier * 2;
				break;
			case TL:
				score += locations[row][i].getTile().getValue() * 3;
				break;
			case TW:
				score += locations[row][i].getTile().getValue();
				multiplier = multiplier * 3;
				break;
			}
		}
		return (score * multiplier);
	}

	/**
	 * calculate the final score assuming all words are valid because calScore
	 * is after checking the words
	 * 
	 * @return score
	 */
	public int calScore() {
		int score = 0;
		Location tempLoc = null;
		// find a played location
		for (Location loc : playedLocations) {
			if (loc != null) {
				tempLoc = loc;
				break;
			}
		}
		if (playedLocationNum == 0) {
			return score;
		}
		// if one played location, do not calculate single letter twice
		if (playedLocationNum == 1) {
			if (getRowWord(tempLoc).length() > 1)
				score += rowScore(tempLoc);
			if (getColWord(tempLoc).length() > 1)
				score += colScore(tempLoc);
			if ((getRowWord(tempLoc).length() <= 1)
					&& (getColWord(tempLoc).length() <= 1))
				score += rowScore(tempLoc);
			return score;
		}
		// check if in the same row
		if (sameRow(tempLoc)) {
			score += rowScore(tempLoc);
			for (Location loc : playedLocations) {
				if (loc == null)
					continue;
				String word = getColWord(loc);
				if (word.length() > 1) {
					score += colScore(loc);
				}
			}
			// check if in the same column
		} else if (sameCol(tempLoc)) {
			score += colScore(tempLoc);
			for (Location loc : playedLocations) {
				if (loc == null)
					continue;
				String word = getRowWord(loc);
				if (word.length() > 1) {
					score += rowScore(loc);
				}
			}
		}
		return score;
	}

	/**
	 * apply the effect of all covered SpecialTile
	 * 
	 * @param game
	 * @param score
	 * @return the final score after being adjusted
	 */
	public int takeEffect(Game game, int score) {
		for (Location loc : playedLocations) {
			if (loc == null || !loc.hasSpecialTile())
				continue;
			SpecialTile tile = loc.getSpecialTile();
			score = tile.action(game, loc, score);
			loc.removeSpecialTile();
			tile.getOwner().removeSpecialTile(tile);
		}
		return score;
	}

	/**
	 * record the recent location
	 * 
	 * @param loc
	 */
	public void addPlayedLocation(Location loc) {
		if (loc == null)
			return;
		playedLocations[playedLocationNum] = loc;
		playedLocationNum++;
	}

	/**
	 * clean up all recent locations for the next turn
	 */
	public void reset() {
		playedLocations = new Location[MAXPLACEDLOCATION];
		playedLocationNum = 0;
	}

	/**
	 * remove all tiles in the range of 3 tiles, including the recorded
	 * locations
	 * 
	 * @param loc
	 */
	public void boom(Location loc) {
		int row = loc.getRow();
		int col = loc.getCol();
		for (int i = row - 2; i < row + 3; i++) {
			for (int j = col - 2; j < col + 3; j++) {
				// find the location on the boundary
				if ((i >= 0) && (i <= LENGTH) && (j >= 0) && (j <= LENGTH)) {
					// use the Manhattan distance
					if (Math.abs(Math.abs(i - row) + Math.abs(j - col)) < 3) {
						// remove SpecialTile
						if (locations[i][j].hasSpecialTile()) {
							SpecialTile tile = locations[i][j]
									.removeSpecialTile();
							tile.getOwner().removeSpecialTile(tile);
						}
						// remove NormalTile
						locations[i][j].removeTile();
						// remove the recorded location
						for (int k = 0; k < MAXPLACEDLOCATION; k++) {
							if (playedLocations[k] == null) {
								continue;
							}
							if (playedLocations[k].equals(locations[i][j])) {
								playedLocations[k] = null;
								playedLocationNum--;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * set words from a location negative
	 * 
	 * @param loc
	 *            location
	 * @return negative score
	 */
	public int negative(Location loc) {
		int score = 0;
		if (getRowWord(loc).length() > 1)
			score += rowScore(loc);
		if (getColWord(loc).length() > 1)
			score += colScore(loc);
		if ((getRowWord(loc).length() <= 1) && (getColWord(loc).length() <= 1))
			score += rowScore(loc);
		return -score;
	}

	/**
	 * find a location on board according to the coordinate
	 * 
	 * @param x
	 *            row
	 * @param y
	 *            column
	 * @return location on board
	 */
	public Location getLocation(int x, int y) {
		return locations[x][y];
	}
}
