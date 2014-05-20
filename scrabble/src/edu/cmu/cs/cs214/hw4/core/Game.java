package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayDeque;
import java.util.ArrayList;

import edu.cmu.cs.cs214.hw4.tile.BoomTile;
import edu.cmu.cs.cs214.hw4.tile.ExtraTurnTile;
import edu.cmu.cs.cs214.hw4.tile.NegativePointTile;
import edu.cmu.cs.cs214.hw4.tile.NoEffectTile;
import edu.cmu.cs.cs214.hw4.tile.NormalTile;
import edu.cmu.cs.cs214.hw4.tile.ReverseOrderTile;
import edu.cmu.cs.cs214.hw4.tile.SpecialTile;

public class Game {
	private Board board;
	private Player[] players;
	private TileBag bag;
	private int numPlayers;
	private Dictionary dict;
	private Player currentPlayer;
	private NormalTile currentNormalTile; // normal tile on hand
	private SpecialTile currentSpecialTile; // special tile on hand
	private ArrayDeque<Player> extraTurn; // record players with an extra turn
	private Player trigger; // for the extra turn tile

	/**
	 * game takes charge of the scrabble, interacts with GUI game has all
	 * necessary moves, only one tile can be manipulated at one time in hand
	 */
	public Game() {
		board = new Board();
		bag = new TileBag();
		dict = new Dictionary();
		numPlayers = 0;
		extraTurn = new ArrayDeque<Player>();
		trigger = null;
	}

	public NormalTile getCurrentNormalTile() {
		return currentNormalTile;
	}

	public SpecialTile getCurrentSpecialTile() {
		return currentSpecialTile;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public TileBag getBag() {
		return bag;
	}

	public Board getBoard() {
		return board;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	/**
	 * set the number of players
	 * 
	 * @param number
	 */
	public void initPlayers(int num) {
		players = new Player[num];
	}

	/**
	 * add a player with a name set the currentPlyer to the first
	 * 
	 * @param name
	 *            no equal name
	 */
	public boolean addPlayer(String name) {
		for (int i = 0; i < numPlayers; i++) {
			if (players[i].getName().equals(name)) {
				return false;
			}
		}
		players[numPlayers] = new Player(name);
		fillRack(players[numPlayers]);
		numPlayers++;
		currentPlayer = players[0];
		return true;
	}

	private void fillRack(Player player) {
		while (!(player.getRack().isFull())) {
			NormalTile tile = bag.takeTile();
			if (tile == null) {
				break;
			}
			player.addNormalTile(tile);
		}
	}

	/**
	 * select a tile from the rack of the current player by hand
	 * 
	 * @param NormalTile
	 */
	public void selectTile(NormalTile tile) {
		currentNormalTile = tile;
		currentPlayer.removeNormalTile(tile);
	}

	/**
	 * put a tile from the board in hand
	 * 
	 * @param x
	 *            row
	 * @param y
	 *            column
	 * @return true if there is a tile on the location
	 */
	public boolean pick(int x, int y) {
		Location loc = board.getLocation(x, y);
		currentNormalTile = board.takePlacedNormalTile(loc);
		if (currentNormalTile == null)
			return false;
		return true;
	}

	/**
	 * drop a tile from hand back to the rack
	 */
	public void drop() {
		currentPlayer.addNormalTile(currentNormalTile);
		currentNormalTile = null;
	}

	/**
	 * add one tile in hand onto the board
	 * 
	 * @param x
	 *            row
	 * @param y
	 *            column
	 * @return true if success
	 */
	public boolean addTileOnBoard(int x, int y) {
		Location loc = board.getLocation(x, y);
		// check if the placement is valid
		if (!(board.sameLine(loc))) {
			return false;
		}
		if (board.addTile(currentNormalTile, loc)) {
			// clear the hand
			currentNormalTile = null;
			// record the location
			board.addPlayedLocation(loc);
			return true;
		}
		return false;
	}

	/**
	 * add a special tile in hand onto board
	 * 
	 * @param x
	 *            row
	 * @param y
	 *            column
	 * @return true if success
	 */
	public boolean addSpecialTileOnBoard(int x, int y) {
		Location loc = board.getLocation(x, y);
		if (board.addSpecialTile(currentSpecialTile, loc)) {
			// clear the hand
			currentSpecialTile = null;
			return true;
		}
		return false;
	}

	/**
	 * buy a special tile by the index of the special tile. The tile will be put
	 * into the rack and also in hand
	 * 
	 * @param num
	 *            index
	 * @return false if no enough score or no enough space in rack
	 */
	public boolean purchase(int num) {
		SpecialTile tile = null;
		if (num == SpecialTile.BOMB) {
			tile = new BoomTile(currentPlayer);
			if (currentPlayer.addSpecialTile(tile)) {
				currentSpecialTile = tile;
				return true;
			}
			return false;
		} else if (num == SpecialTile.NEGATIVE) {
			tile = new NegativePointTile(currentPlayer);
			if (currentPlayer.addSpecialTile(tile)) {
				currentSpecialTile = tile;
				return true;
			}
			return false;
		} else if (num == SpecialTile.NOEFFECT) {
			tile = new NoEffectTile(currentPlayer);
			if (currentPlayer.addSpecialTile(tile)) {
				currentSpecialTile = tile;
				return true;
			}
			return false;
		} else if (num == SpecialTile.REVERSE) {
			tile = new ReverseOrderTile(currentPlayer);
			if (currentPlayer.addSpecialTile(tile)) {
				currentSpecialTile = tile;
				return true;
			}
			return false;
		} else if (num == SpecialTile.EXTRA) {
			tile = new ExtraTurnTile(currentPlayer);
			if (currentPlayer.addSpecialTile(tile)) {
				currentSpecialTile = tile;
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * begin a new turn, fill the rack and update the board
	 */
	public void startTurn() {
		fillRack(currentPlayer);
		board.reset();
	}

	/**
	 * check if the words from the board are all valid
	 * 
	 * @return true if valid
	 */
	private boolean check() {
		ArrayList<String> words = board.getWords();
		if (words == null) {
			return false;
		}
		for (String word : words) {
			if (!(dict.contain(word))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * first, check validation, then update score and adjust score by applying
	 * the effects of special tiles, finally set the score of the current
	 * player.
	 * 
	 * @return false if the words are not all valid
	 */
	public boolean submit() {
		if (!check()) {
			return false;
		}
		int score = board.calScore();
		score = board.takeEffect(this, score);
		score = currentPlayer.getScore() + score;
		currentPlayer.setScore(score);
		return true;
	}

	/**
	 * change to next player who may have an extra turn
	 */
	public void endTurn() {
		if (extraTurn.size() == 0) {
			if (trigger != null) {
				currentPlayer = trigger;
				trigger = null;
			}
			for (int i = 0; i < numPlayers; i++) {
				if (players[i].getName().equals(currentPlayer.getName())) {
					currentPlayer = players[(i + 1) % numPlayers];
					break;
				}
			}
		} else {
			currentPlayer = extraTurn.pop();
		}
	}

	/**
	 * game ends if no tile in bag and current player's rack.
	 * 
	 * @return true if it is the end
	 */
	public boolean endGame() {
		if (bag.getTileNum() == 0
				&& currentPlayer.getRack().getNormalNum() == 0)
			return true;
		else
			return false;
	}

	/**
	 * find the winner
	 * 
	 * @return the player who has the highest score
	 */
	public Player getWinner() {
		int highest = 0;
		Player winner = null;
		for (int i = 0; i < numPlayers; i++) {
			int score = players[i].getScore();
			if (score > highest) {
				highest = score;
				winner = players[i];
			}
		}
		return winner;
	}

	/**
	 * exchange some tiles, put them back in the bag, get new ones
	 * 
	 * @param list
	 *            tiles that you exchange
	 * @return false if no enough tiles in the bag
	 */
	public boolean exchange(NormalTile[] list) {
		int num = list.length;
		if (bag.getTileNum() < num)
			return false;
		for (NormalTile tile : list) {
			bag.addTile(tile);
			currentPlayer.removeNormalTile(tile);
		}
		fillRack(currentPlayer);
		return true;
	}

	/**
	 * reverse the order of players
	 */
	public void reorder() {
		Player[] tempPlayers = new Player[numPlayers];
		int index = 0;
		for (int i = numPlayers - 1; i >= 0; i--) {
			tempPlayers[i] = players[index];
			index++;
		}
		for (int i = 0; i < numPlayers; i++) {
			players[i] = tempPlayers[i];
		}
	}

	/**
	 * record the player who has an extra turn and add the next player in the
	 * original order.
	 * 
	 * @param player
	 *            the owner of the extraTurn tile
	 */
	public void extraTurn(Player player) {
		if (extraTurn.size() == 0) {
			trigger = currentPlayer;
		}
		extraTurn.push(player);
	}
}
