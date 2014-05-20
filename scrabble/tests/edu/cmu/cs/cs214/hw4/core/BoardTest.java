package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.cmu.cs.cs214.hw4.tile.BoomTile;
import edu.cmu.cs.cs214.hw4.tile.NegativePointTile;
import edu.cmu.cs.cs214.hw4.tile.NoEffectTile;
import edu.cmu.cs.cs214.hw4.tile.NormalTile;

public class BoardTest {
	private Board board;

	@Before
	public void setUp() throws Exception {
		board = new Board();
		board.reset();
		board.addTile(new NormalTile('w', 4), board.getLocation(7, 7));
		board.addPlayedLocation(board.getLocation(7, 7));
		board.addTile(new NormalTile('o', 1), board.getLocation(7, 8));
		board.addPlayedLocation(board.getLocation(7, 8));
		board.addTile(new NormalTile('r', 1), board.getLocation(7, 9));
		board.addPlayedLocation(board.getLocation(7, 9));
		board.addTile(new NormalTile('d', 2), board.getLocation(7, 10));
		board.addPlayedLocation(board.getLocation(7, 10));
		board.addTile(new NormalTile('a', 1), board.getLocation(8, 10));
		board.addTile(new NormalTile('y', 4), board.getLocation(9, 10));
		board.addSpecialTile(new BoomTile(new Player("Pan")),
				board.getLocation(8, 7));
		board.addSpecialTile(new NegativePointTile(new Player("Pan")),
				board.getLocation(8, 8));
		board.addSpecialTile(new NoEffectTile(new Player("Pan")),
				board.getLocation(8, 9));
	}

	/**
	 * check if the score is right assuming a valid is played
	 *      word
	 *      ***a
	 *         y
	 */
	@Test
	public void testScore() {
		assertTrue(board.calScore() == 23);
	}

	/**
	 * test the effect of a special tile
	 *      word
	 *      o**a
	 *      r  y
	 *      l
	 *      d  
	 */
	@Test
	public void testBoom() {
		board.reset();
		board.addTile(new NormalTile('o', 1), board.getLocation(8, 7));
		board.addPlayedLocation(board.getLocation(8, 7));
		board.addTile(new NormalTile('r', 1), board.getLocation(9, 7));
		board.addPlayedLocation(board.getLocation(9, 7));
		board.addTile(new NormalTile('l', 1), board.getLocation(10, 7));
		board.addPlayedLocation(board.getLocation(10, 7));
		board.addTile(new NormalTile('d', 2), board.getLocation(11, 7));
		board.addPlayedLocation(board.getLocation(11, 7));
		board.boom(board.getLocation(8, 7));
		assertTrue(board.calScore() == 4);
	}
}
