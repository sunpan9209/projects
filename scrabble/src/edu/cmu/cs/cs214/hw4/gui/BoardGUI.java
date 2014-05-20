package edu.cmu.cs.cs214.hw4.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw4.core.Board;
import edu.cmu.cs.cs214.hw4.core.Player;

public class BoardGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1147938621031711597L;
	private static final Color COLOR = new Color(128, 128, 128); // grey
	private static final int BORDER = 1;
	private LocationGUI[][] locations;
	private MouseListener listener;

	/**
	 * board panel contains locations
	 * 
	 * @param board
	 * @param listener
	 *            the game
	 */
	public BoardGUI(Board board, MouseListener listener) {
		super();
		this.listener = listener;
		locations = new LocationGUI[board.getLength()][board.getLength()];
		this.setLayout(new GridLayout(board.getLength(), board.getLength()));
		init(board);
	}

	private void init(Board board) {
		for (int row = 0; row < board.getLength(); row++) {
			for (int col = 0; col < board.getLength(); col++) {
				this.locations[row][col] = new LocationGUI(board.getLocation(
						row, col));
				LocationGUI loc = this.locations[row][col];
				// draw borders
				if (row == 0) {
					if (col == 0) {
						// draw all sides
						loc.setBorder(BorderFactory.createLineBorder(COLOR));
					} else {
						// draw all sides except left edge
						loc.setBorder(BorderFactory.createMatteBorder(BORDER,
								0, BORDER, BORDER, COLOR));
					}
				} else {
					if (col == 0) {
						// draw all sides except top
						loc.setBorder(BorderFactory.createMatteBorder(0,
								BORDER, BORDER, BORDER, COLOR));
					} else {
						// skip top and left edges
						loc.setBorder(BorderFactory.createMatteBorder(0, 0,
								BORDER, BORDER, COLOR));
					}
				}
				loc.addMouseListener(listener);
				this.add(loc);
			}
		}
	}

	/**
	 * update the board
	 * 
	 * @param board
	 * @param player
	 *            currentPlayer
	 */
	public void update(Board board, Player player) {
		for (int row = 0; row < board.getLength(); row++)
			for (int col = 0; col < board.getLength(); col++)
				this.locations[row][col].update(board.getLocation(row, col),
						player);
		this.validate();
		this.repaint();
	}
}
