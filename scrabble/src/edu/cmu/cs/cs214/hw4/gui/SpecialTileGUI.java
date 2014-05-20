package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.cmu.cs.cs214.hw4.tile.SpecialTile;

public class SpecialTileGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4556650465235634206L;
	private static final Color COL_FIRM = new Color(0xFA, 0xF0, 0xBE); // blond
	private final SpecialTile tile;
	private JLabel type;

	/**
	 * special tile panel, N for negative, E for no effect, B for boom, R for
	 * reverse, T for extra turn
	 * 
	 * @param tile
	 */
	public SpecialTileGUI(SpecialTile tile) {
		this.tile = tile;
		this.setBackground(COL_FIRM);
		setLayout(new BorderLayout(0, 0));

		this.type = new JLabel("");
		this.type.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		this.type.setHorizontalTextPosition(SwingConstants.CENTER);
		this.type.setHorizontalAlignment(SwingConstants.CENTER);
		add(this.type, BorderLayout.CENTER);
		this.type.setText("" + this.tile.getName().substring(0, 1));
	}
}
