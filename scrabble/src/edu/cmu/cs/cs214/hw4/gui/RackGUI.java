package edu.cmu.cs.cs214.hw4.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import edu.cmu.cs.cs214.hw4.core.Rack;
import edu.cmu.cs.cs214.hw4.tile.NormalTile;

public class RackGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4023846656205700624L;
	private static final int TILE_NUMBER = 7;
	private static final int BORDER = 1;
	private static final Color COLOR = new Color(128, 128, 128); // Grey
	private MouseListener listener;

	/**
	 * rack panel, listen the game frame
	 * 
	 * @param rack
	 * @param listener
	 */
	public RackGUI(Rack rack, MouseListener listener) {
		super();
		this.listener = listener;
		this.setLayout(new GridLayout(1, TILE_NUMBER));
		init(rack);
	}

	private void init(Rack rack) {
		NormalTile[] tiles = rack.getNormalRack();
		for (int i = 0; i < TILE_NUMBER; i++) {
			NormalTile tile;
			if (i < tiles.length) {
				tile = tiles[i];
			} else
				tile = null;
			JPanel panel = new TileGUI(tile);
			// draw borders
			if (i == 0) {
				// draw all sides
				panel.setBorder(BorderFactory.createLineBorder(COLOR));
			} else {
				// draw all sides except left edge
				panel.setBorder(BorderFactory.createMatteBorder(BORDER, 0,
						BORDER, BORDER, COLOR));
			}
			panel.addMouseListener(listener);
			this.add(panel);
		}
	}

	/**
	 * redraw the rack
	 * 
	 * @param rack
	 */
	public void update(Rack rack) {
		this.removeAll();
		this.init(rack);
		this.validate();
		this.repaint();
	}
}
