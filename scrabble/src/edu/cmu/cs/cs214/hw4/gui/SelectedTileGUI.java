package edu.cmu.cs.cs214.hw4.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw4.tile.NormalTile;
import edu.cmu.cs.cs214.hw4.tile.SpecialTile;

public class SelectedTileGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -385265382212332553L;
	private TileGUI tile = null;
	private SpecialTileGUI specialTile = null;

	/**
	 * represent your hand
	 */
	public SelectedTileGUI() {
		setLayout(new GridLayout(1, 0, 0, 0));
	}

	/**
	 * draw the tile
	 * 
	 * @param tile
	 */
	public void updateTile(NormalTile tile) {
		if (this.tile != null)
			this.remove(this.tile);
		if (tile == null) {
			this.tile = null;
		} else {
			this.tile = new TileGUI(tile);
			this.tile.setOpaque(true);
			this.add(this.tile);
		}
		this.validate();
		this.repaint();
	}

	/**
	 * draw the special tile
	 * 
	 * @param tile
	 */
	public void updateSpecialTile(SpecialTile tile) {
		if (this.specialTile != null)
			this.remove(this.specialTile);

		if (tile == null) {
			this.specialTile = null;
		} else {
			this.specialTile = new SpecialTileGUI(tile);
			this.specialTile.setOpaque(true);
			this.add(this.specialTile);
		}
		this.validate();
		this.repaint();
	}
}
