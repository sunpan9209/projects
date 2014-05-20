package edu.cmu.cs.cs214.hw4.tile;

import edu.cmu.cs.cs214.hw4.core.Game;
import edu.cmu.cs.cs214.hw4.core.Location;
import edu.cmu.cs.cs214.hw4.core.Player;

public class ReverseOrderTile extends SpecialTile {
	private static final int PRICE = 15;
	private static final String NAME = "Reverse";

	public ReverseOrderTile(Player player) {
		super(player, PRICE, NAME);
	}

	@Override
	public int action(Game game, Location location, int score) {
		game.reorder();
		return score;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ReverseOrderTile)
			return getOwner() == ((ReverseOrderTile) obj).getOwner();
		else
			return false;
	}

	@Override
	public int hashCode() {
		return PRICE;
	}
}
