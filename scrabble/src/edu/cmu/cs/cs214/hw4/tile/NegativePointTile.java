package edu.cmu.cs.cs214.hw4.tile;

import edu.cmu.cs.cs214.hw4.core.Game;
import edu.cmu.cs.cs214.hw4.core.Location;
import edu.cmu.cs.cs214.hw4.core.Player;

public class NegativePointTile extends SpecialTile {
	private static final int PRICE = 10;
	private static final String NAME = "Negative";

	public NegativePointTile(Player player) {
		super(player, PRICE, NAME);
	}

	@Override
	public int action(Game game, Location location, int score) {
		if (score == 0)
			return 0;
		return (score + game.getBoard().negative(location));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NegativePointTile)
			return getOwner() == ((NegativePointTile) obj).getOwner();
		else
			return false;
	}

	@Override
	public int hashCode() {
		return PRICE;
	}
}
