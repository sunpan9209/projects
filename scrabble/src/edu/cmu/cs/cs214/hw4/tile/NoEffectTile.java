package edu.cmu.cs.cs214.hw4.tile;

import edu.cmu.cs.cs214.hw4.core.Game;
import edu.cmu.cs.cs214.hw4.core.Location;
import edu.cmu.cs.cs214.hw4.core.Player;

public class NoEffectTile extends SpecialTile {
	private static final int PRICE = 10;
	private static final String NAME = "Effect";

	public NoEffectTile(Player player) {
		super(player, PRICE, NAME);
	}

	@Override
	public int action(Game game, Location location, int score) {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NoEffectTile)
			return getOwner() == ((NoEffectTile) obj).getOwner();
		else
			return false;
	}

	@Override
	public int hashCode() {
		return PRICE;
	}
}
