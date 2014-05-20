package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import edu.cmu.cs.cs214.hw4.core.Game;
import edu.cmu.cs.cs214.hw4.core.Location;
import edu.cmu.cs.cs214.hw4.tile.NormalTile;
import edu.cmu.cs.cs214.hw4.tile.SpecialTile;

public class GameGUI extends JFrame implements ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4644250805803288185L;
	private static final int SIZE = 32;
	private Game game;
	private BoardGUI board;
	private RackGUI rack;
	private SelectedTileGUI selectedTile;
	private ArrayList<NormalTile> exchangeTiles;
	private JButton submit;
	private JButton skip;
	private JButton exchange;
	private JButton purchase;
	private JLabel remainingTiles;
	private JLabel score;
	private boolean readyToExchange;
	private boolean readyToPlace;

	/**
	 * game frame, listen your mouse and the buttons
	 * 
	 * @param game
	 *            your game in the core package
	 */
	public GameGUI(Game game) {
		this.game = game;
		exchangeTiles = new ArrayList<NormalTile>();
		readyToExchange = false;
		readyToPlace = false;
		init(game);
	}

	private void init(Game game) {
		setTitle("Player: " + game.getCurrentPlayer().getName());
		setBounds(100, 100, 900, 527);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		JPanel panelMain = new JPanel();
		this.getContentPane().add(panelMain, BorderLayout.NORTH);
		panelMain.setLayout(new BorderLayout(0, 0));

		// information section at the top
		JPanel panelTop = new JPanel();
		panelMain.add(panelTop, BorderLayout.NORTH);
		panelTop.setMaximumSize(new Dimension(32767, 20));
		panelTop.setPreferredSize(new Dimension(10, 20));
		panelTop.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelTop.setLayout(new BorderLayout(0, 0));
		this.remainingTiles = new JLabel("remainingTiles: "
				+ game.getBag().getTileNum());
		panelTop.add(remainingTiles);
		this.score = new JLabel("Score: " + game.getCurrentPlayer().getScore());
		panelTop.add(score, BorderLayout.EAST);

		// board section
		JPanel panelBoard = new JPanel();
		panelBoard.setMinimumSize(new Dimension(300, 300));
		panelMain.add(panelBoard, BorderLayout.WEST);
		panelBoard
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelBoard.setLayout(new GridLayout(1, 0, 0, 0));
		this.board = new BoardGUI(game.getBoard(), this);
		board.setPreferredSize(new Dimension(15 * SIZE, 15 * SIZE));
		panelBoard.add(board);

		// player section
		JPanel panelPlayer = new JPanel();
		panelPlayer.setMinimumSize(new Dimension(100, 10));
		panelMain.add(panelPlayer, BorderLayout.CENTER);
		panelPlayer.setPreferredSize(new Dimension(200, 10));
		panelPlayer.setLayout(new BorderLayout(0, 0));
		JPanel panelRack = new JPanel();
		panelRack.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelPlayer.add(panelRack, BorderLayout.CENTER);
		panelRack.setLayout(new BorderLayout(0, 0));
		JPanel panelSelected = new JPanel();
		panelRack.add(panelSelected, BorderLayout.SOUTH);
		panelSelected.setPreferredSize(new Dimension(10, 50));
		panelSelected.setMinimumSize(new Dimension(10, 50));
		panelSelected.setBorder(null);
		panelSelected.setLayout(new BorderLayout(0, 0));
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(100, 10));
		panelSelected.add(panel1, BorderLayout.CENTER);
		this.selectedTile = new SelectedTileGUI();
		panel1.add(selectedTile);
		selectedTile.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null,
				null));
		selectedTile.setPreferredSize(new Dimension(SIZE, SIZE));
		selectedTile.addMouseListener(this);
		JPanel panel2 = new JPanel();
		panelRack.add(panel2, BorderLayout.NORTH);
		panel2.setLayout(new BorderLayout(0, 0));
		JPanel panel3 = new JPanel();
		panel2.add(panel3, BorderLayout.NORTH);
		this.rack = new RackGUI(game.getCurrentPlayer().getRack(), this);
		panel3.add(rack);
		rack.setPreferredSize(new Dimension(6 * SIZE, 1 * SIZE));

		// button section
		JPanel buttons = new JPanel();
		panel2.add(buttons, BorderLayout.SOUTH);
		this.submit = new JButton("Submit");
		submit.addActionListener(this);
		buttons.add(submit);
		this.exchange = new JButton("Exchange");
		exchange.addActionListener(this);
		buttons.add(exchange);
		this.purchase = new JButton("Purchase");
		purchase.addActionListener(this);
		buttons.add(purchase);
		this.skip = new JButton("Skip");
		skip.addActionListener(this);
		buttons.add(skip);

		this.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if (o instanceof LocationGUI)
			clickedOnLocation(o);
		else if (o instanceof TileGUI)
			clickedOnTile(o);
		else if (o instanceof SelectedTileGUI)
			clickedOnSelectedTile(o);
		update();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == skip)
			skip();
		else if (o == exchange)
			exchange();
		else if (o == purchase)
			purchase();
		else if (o == submit)
			submit();
		// if game ended
		if (!this.isVisible())
			return;
		update();
	}

	/**
	 * update the game frame
	 */
	private void update() {
		this.board.update(game.getBoard(), game.getCurrentPlayer());
		this.rack.update(game.getCurrentPlayer().getRack());
		this.selectedTile.updateTile(game.getCurrentNormalTile());
		this.selectedTile.updateSpecialTile(game.getCurrentSpecialTile());
		setTitle("Player: " + game.getCurrentPlayer().getName());
		this.remainingTiles.setText("remainingTiles: "
				+ game.getBag().getTileNum());
		this.score.setText("Score: " + game.getCurrentPlayer().getScore());
	}

	/**
	 * what happens after you click on the selected tile
	 * 
	 * @param o
	 */
	private void clickedOnSelectedTile(Object o) {
		if (readyToPlace) {
			if (game.getCurrentNormalTile() != null) {
				game.drop();
				readyToPlace = false;
				enableButtons();
				if (game.getBoard().getPlayedLocationNum() > 0) {
					exchange.setEnabled(false);
					skip.setEnabled(false);
				}
			}
		}
	}

	/**
	 * press the submit button
	 */
	private void submit() {
		if (game.submit()) {
			// end the game
			if (game.endGame()) {
				JOptionPane.showMessageDialog(null,
						"Winner: " + game.getWinner());
				this.setVisible(false);
				return;
			}
			game.endTurn();
			game.startTurn();
			enableButtons();
			readyToExchange = false;
			readyToPlace = false;
			// submit fails
		} else {
			JOptionPane.showMessageDialog(null, "Not a word");
			for (Location loc : game.getBoard().getPlayedLocations()) {
				if (loc == null) {
					continue;
				}
				NormalTile tile = game.getBoard().removeTile(loc);
				game.getCurrentPlayer().addNormalTile(tile);
			}
			game.getBoard().reset();
			enableButtons();
			readyToExchange = false;
			readyToPlace = false;
		}
	}

	/**
	 * purchase button
	 */
	private void purchase() {
		try {
			String message = "Select the type of special tile to purchase: \n"
					+ SpecialTile.NEGATIVE + " for NegativePoint N with 10\n"
					+ SpecialTile.BOMB + " for Boom B with 20\n"
					+ SpecialTile.NOEFFECT + " for NoEffect E with 10\n"
					+ SpecialTile.REVERSE + " for ReverseOrder R with 15\n"
					+ SpecialTile.EXTRA + " for ExtraTurn T with 10\n";
			int type = Integer.parseInt(JOptionPane.showInputDialog(message));
			if (game.purchase(type)) {
				score.setText("Score: " + game.getCurrentPlayer().getScore());
				readyToPlace = true;
				disableButtons();
			} else
				JOptionPane.showMessageDialog(null,
						"You can't buy a special tile!");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid type");
		}
	}

	/**
	 * exchange button
	 */
	private void exchange() {
		// select the tile you want to exchange
		if (!readyToExchange) {
			readyToExchange = true;
			exchange.setText("Confirm");
			submit.setEnabled(false);
			exchange.setEnabled(true);
			skip.setEnabled(false);
			purchase.setEnabled(false);
		} else {
			if (exchangeTiles.size() == 0) {
				JOptionPane.showMessageDialog(null, "No Tile selected!",
						"Error", JOptionPane.PLAIN_MESSAGE);
				readyToExchange = false;
				exchange.setText("Exchange");
				enableButtons();
				return;
			}
			NormalTile[] toExchange = convertHelper(exchangeTiles);
			if (game.exchange(toExchange)) {
				exchangeTiles.clear();
				exchange.setText("Exchange");
				skip();
				// if fails, return tiles
			} else {
				JOptionPane.showMessageDialog(null, "No Enough Tiles!",
						"Error", JOptionPane.PLAIN_MESSAGE);
				for (NormalTile tile : exchangeTiles) {
					game.getCurrentPlayer().addNormalTile(tile);
				}
				exchangeTiles.clear();
				readyToExchange = false;
				exchange.setText("Exchange");
				enableButtons();
			}
		}
	}

	/**
	 * skip this turn
	 */
	private void skip() {
		// check if game ends
		if (game.endGame()) {
			JOptionPane.showMessageDialog(null, "Winner: " + game.getWinner());
			this.setVisible(false);
			return;
		}
		game.endTurn();
		game.startTurn();
		enableButtons();
		readyToExchange = false;
		readyToPlace = false;
	}

	/**
	 * what happens if you click a tile in your rack
	 * 
	 * @param o
	 */
	private void clickedOnTile(Object o) {
		NormalTile tile = ((TileGUI) o).getTile();
		// put it into exchange list
		if (readyToExchange) {
			if (tile != null) {
				game.getCurrentPlayer().removeNormalTile(tile);
				exchangeTiles.add(tile);
			}
			return;
		}
		// select a tile by hand
		if (!readyToPlace) {
			if (tile == null)
				return;
			readyToPlace = true;
			game.selectTile(tile);
			disableButtons();
			// drop tile back in your rack
		} else {
			if (game.getCurrentSpecialTile() != null)
				return;
			readyToPlace = false;
			game.drop();
			enableButtons();
			if (game.getBoard().getPlayedLocationNum() > 0) {
				exchange.setEnabled(false);
				skip.setEnabled(false);
			}
		}
	}

	/**
	 * what happens if you click on a location
	 * 
	 * @param o
	 */
	private void clickedOnLocation(Object o) {
		if (readyToExchange)
			return;
		Location loc = ((LocationGUI) o).getLoc();
		// pick up a tile you just placed
		if (!readyToPlace) {
			if (game.pick(loc.getRow(), loc.getCol())) {
				readyToPlace = true;
				disableButtons();
			}
			// add the tile
		} else {
			if (game.addSpecialTileOnBoard(loc.getRow(), loc.getCol())) {
				readyToPlace = false;
				enableButtons();
				if (game.getBoard().getPlayedLocationNum() > 0) {
					exchange.setEnabled(false);
					skip.setEnabled(false);
				}
			} else {
				if (game.addTileOnBoard(loc.getRow(), loc.getCol())) {
					readyToPlace = false;
					enableButtons();
					exchange.setEnabled(false);
					skip.setEnabled(false);
				}
			}
		}
	}

	/**
	 * turn an ArrayList into an array
	 * 
	 * @param tiles
	 * @return
	 */
	private NormalTile[] convertHelper(ArrayList<NormalTile> tiles) {
		NormalTile[] normalTiles = new NormalTile[tiles.size()];
		int index = 0;
		for (NormalTile tile : tiles) {
			normalTiles[index] = tile;
			index++;
		}
		return normalTiles;
	}

	private void enableButtons() {
		submit.setEnabled(true);
		exchange.setEnabled(true);
		skip.setEnabled(true);
		purchase.setEnabled(true);
	}

	private void disableButtons() {
		submit.setEnabled(false);
		exchange.setEnabled(false);
		skip.setEnabled(false);
		purchase.setEnabled(false);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		return;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		return;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		return;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		return;
	}
}
