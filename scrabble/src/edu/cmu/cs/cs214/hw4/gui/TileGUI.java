package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.cmu.cs.cs214.hw4.tile.NormalTile;

public class TileGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4549071428247061311L;
	private static final Color COL_FIRM = new Color(0xFA, 0xF0, 0xBE); // blond
	private final NormalTile tile;
	private JLabel letter;
	private JLabel value;

	/**
	 * draw a tile with value and letter
	 * 
	 * @param tile
	 */
	public TileGUI(NormalTile tile) {
		this.tile = tile;
		this.setBackground(COL_FIRM);
		setLayout(new BorderLayout(0, 0));

		this.letter = new JLabel("");
		this.letter.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		this.letter.setHorizontalTextPosition(SwingConstants.CENTER);
		this.letter.setHorizontalAlignment(SwingConstants.CENTER);
		add(this.letter, BorderLayout.CENTER);

		this.value = new JLabel("");
		value.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		value.setVerticalAlignment(SwingConstants.BOTTOM);
		this.value.setHorizontalAlignment(SwingConstants.RIGHT);
		add(this.value, BorderLayout.EAST);
		this.update();
	}

	/**
	 * draw the tile
	 */
	private void update() {
		if (tile != null) {
			this.letter.setText("" + this.tile.getLetter());
			this.value.setText("" + this.tile.getValue());
		}
	}

	public NormalTile getTile() {
		return tile;
	}
}
