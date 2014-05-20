package edu.cmu.cs.cs214.hw4.tile;

import edu.cmu.cs.cs214.hw4.core.Game;
import edu.cmu.cs.cs214.hw4.core.Location;
import edu.cmu.cs.cs214.hw4.core.Player;

public class ExtraTurnTile extends SpecialTile {
	private static final int PRICE = 10;
	private static final String NAME = "Turn";

	public ExtraTurnTile(Player player) {
		super(player, PRICE, NAME);
	}

	@Override
	public int action(Game game, Location location, int score) {
		if (location.getSpecialTile().getOwner().getName()
				.equals(game.getCurrentPlayer().getName())) {
			return score;
		}
		game.extraTurn(location.getSpecialTile().getOwner());
		return score;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ExtraTurnTile)
			return getOwner() == ((ExtraTurnTile) obj).getOwner();
		else
			return false;
	}

	@Override
	public int hashCode() {
		return PRICE;
	}
}
