package edu.cmu.cs.cs214.hw4.tile;

import edu.cmu.cs.cs214.hw4.core.Game;
import edu.cmu.cs.cs214.hw4.core.Location;
import edu.cmu.cs.cs214.hw4.core.Player;

public abstract class SpecialTile {
	private Player owner;
	private int price;
	private String name;
	public static final int NEGATIVE = 0; // index of SpecialTile
	public static final int BOMB = 1;
	public static final int NOEFFECT = 2;
	public static final int REVERSE = 3;
	public static final int EXTRA = 4;

	/**
	 * A special tile has its owner, price and name. It can affect the game and
	 * the score
	 * 
	 * @param player
	 *            the owner
	 * @param price
	 *            price
	 * @param name
	 */
	public SpecialTile(Player player, int price, String name) {
		owner = player;
		this.price = price;
		this.name = name;
	}

	public Player getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	/**
	 * apply the effect of a special tile, like boom, negative and reorder
	 * 
	 * @param game
	 *            current game
	 * @param location
	 *            where the special tile is
	 * @param score
	 *            calculated score for adjustment
	 * @return final score
	 */
	public abstract int action(Game game, Location location, int score);
}
