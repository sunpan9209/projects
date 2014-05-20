package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import edu.cmu.cs.cs214.hw4.tile.SpecialTile;

public class CoreTest {
	private Game game;

	@Before
	public void setUp() throws Exception {
		game = new Game();
	}

	/**
	 * test the game sequence, may not calculate the score
	 */
	@Test
	public void playTest() {
		game.initPlayers(2);
		game.addPlayer("Pan");
		game.addPlayer("Sun");
		game.startTurn();

		// test the first round
		assertFalse(game.pick(7, 7));
		assertFalse(game.endGame());
		game.startTurn();
		
		// test exchanging 
		assertTrue(game.exchange(game.getCurrentPlayer().getRack().getNormalRack()));

		// test placement, like pick,drop and add, remove
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[0]);
		assertTrue(game.addTileOnBoard(7, 7));
		assertTrue(game.pick(7, 7));
		assertTrue(game.addTileOnBoard(7, 7));
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[1]);
		assertFalse(game.addTileOnBoard(7, 7));
		game.drop();
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[2]);
		game.drop();
		assertTrue(game.pick(7, 7));
		assertFalse(game.addTileOnBoard(7, 8));
		game.drop();
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[0]);
		assertTrue(game.addTileOnBoard(7, 7));
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[1]);
		assertFalse(game.addTileOnBoard(7, 9));
		assertTrue(game.addTileOnBoard(7, 8));
		assertFalse(game.pick(7, 7));
		assertTrue(game.pick(7, 8));
		assertTrue(game.addTileOnBoard(6, 7));
		assertFalse(game.pick(7, 7));
		assertTrue(game.pick(6, 7));
		assertTrue(game.addTileOnBoard(7, 6));
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[2]);
		assertFalse(game.addTileOnBoard(6, 7));
		assertTrue(game.addTileOnBoard(7, 5));

		// test the purchase
		game.startTurn();
		game.getCurrentPlayer().setScore(100);
		assertTrue(game.purchase(SpecialTile.NEGATIVE));
		assertFalse(game.addSpecialTileOnBoard(7, 7));
		assertTrue(game.addSpecialTileOnBoard(7, 4));
		assertTrue(game.purchase(SpecialTile.REVERSE));
		assertTrue(game.addSpecialTileOnBoard(8, 4));
		assertTrue(game.purchase(SpecialTile.NOEFFECT));
		assertTrue(game.addSpecialTileOnBoard(9, 4));
		assertTrue(game.purchase(SpecialTile.BOMB));
		assertFalse(game.addSpecialTileOnBoard(9, 4));
		assertTrue(game.addSpecialTileOnBoard(10, 4));
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[0]);
		assertTrue(game.addTileOnBoard(7, 4));
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[1]);
		assertTrue(game.addTileOnBoard(8, 4));
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[2]);
		assertTrue(game.addTileOnBoard(9, 4));
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[3]);
		assertTrue(game.addTileOnBoard(10, 4));
		assertTrue(game.getBoard().takeEffect(game, 100) == 0);

		// test submitting, may not calculate the score since we don't know what
		// tile is played, but every step of a submit has been tested in
		// boardTest.
		game.submit();
		game.endTurn();
		assertTrue(game.getWinner().getName().equals("Pan"));
		assertFalse(game.endGame());

		// test a new round
		game.startTurn();
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[2]);
		assertFalse(game.addTileOnBoard(5, 7));
		assertTrue(game.addTileOnBoard(6, 7));
		game.selectTile(game.getCurrentPlayer().getRack().getNormalRack()[5]);
		assertTrue(game.addTileOnBoard(6, 6));
		assertTrue(game.pick(6, 6));
		assertTrue(game.addTileOnBoard(5, 7));
		game.submit();
		assertFalse(game.purchase(SpecialTile.REVERSE));
		assertFalse(game.endGame());

		// test reorder
		game.reorder();
		game.endTurn();
		game.startTurn();
		assertTrue(game.getCurrentPlayer().getName().equals("Pan"));
	}
}
