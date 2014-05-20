package edu.cmu.cs.cs214.hw4.tile;

import edu.cmu.cs.cs214.hw4.core.Game;
import edu.cmu.cs.cs214.hw4.core.Location;
import edu.cmu.cs.cs214.hw4.core.Player;

public class BoomTile extends SpecialTile {
	private static final int PRICE = 20;
	private static final String NAME = "Boom";

	public BoomTile(Player player) {
		super(player, PRICE, NAME);
	}

	@Override
	public int action(Game game, Location location, int score) {
		if (score == 0)
			return 0;
		game.getBoard().boom(location);
		return game.getBoard().calScore();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BoomTile)
			return getOwner() == ((BoomTile) obj).getOwner();
		else
			return false;
	}

	@Override
	public int hashCode() {
		return PRICE;
	}
}
