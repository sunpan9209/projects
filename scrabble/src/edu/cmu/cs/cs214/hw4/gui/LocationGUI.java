package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.cmu.cs.cs214.hw4.core.Factor;
import edu.cmu.cs.cs214.hw4.core.Location;
import edu.cmu.cs.cs214.hw4.core.Player;

public class LocationGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6884239902362442200L;
	private Location loc;
	private final JLabel multiplier;
	private TileGUI tile = null;
	private SpecialTileGUI specialTile = null;
	private static final Color COL_EMPT = new Color(0xF0, 0xF8, 0xFF); // white
	private static final Color COL_DLET = new Color(0x72, 0xA0, 0xC1); // blue
	private static final Color COL_TLET = new Color(0xBF, 0xFF, 0x00); // green
	private static final Color COL_DWOR = new Color(0xC3, 0x21, 0x48); // red
	private static final Color COL_TWOR = new Color(0xCC, 0x55, 0x00); // orange
	private static final Color COL_CENT = new Color(0x96, 0x00, 0x18); // cMine

	/**
	 * draw a location with different colors
	 * 
	 * @param loc
	 */
	public LocationGUI(Location loc) {
		super();
		this.loc = loc;
		Color col = COL_EMPT;
		String txt = "";
		Factor factor = this.loc.getFactor();
		if (loc.isCenter()) {
			col = COL_CENT;
			txt = "TL";
		} else {
			switch (factor) {
			case NS:
				break;
			case DL:
				col = COL_DLET;
				txt = "DL";
				break;
			case DW:
				col = COL_DWOR;
				txt = "DW";
				break;
			case TL:
				col = COL_TLET;
				txt = "TL";
				break;
			case TW:
				col = COL_TWOR;
				txt = "TW";
				break;
			}
		}
		// initiate the panel
		this.setBackground(col);
		this.setLayout(new BorderLayout(0, 0));
		this.multiplier = new JLabel(txt);
		this.multiplier.setForeground(new Color(255, 255, 255));
		this.multiplier.setHorizontalTextPosition(SwingConstants.CENTER);
		this.multiplier.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(this.multiplier);
		this.setMultiVisibility();
		if (!(this.loc.isEmpty())) {
			this.tile = new TileGUI(this.loc.getTile());
			this.tile.setOpaque(true);
			this.add(this.tile);
		} else if (this.loc.hasSpecialTile()) {
			this.specialTile = new SpecialTileGUI(this.loc.getSpecialTile());
			this.specialTile.setOpaque(true);
			this.add(this.specialTile);
		}
	}

	public Location getLoc() {
		return loc;
	}

	/**
	 * redraw the location
	 * 
	 * @param loc
	 * @param player
	 *            currentPlayer
	 */
	public void update(Location loc, Player player) {
		if ((this.loc.getRow() != loc.getRow())
				|| (this.loc.getCol() != loc.getCol()))
			throw new RuntimeException("not the same location");
		if (this.tile != null) {
			this.remove(this.tile);
			this.tile = null;
		}
		if (this.specialTile != null) {
			this.remove(this.specialTile);
			this.specialTile = null;
		}
		this.loc = loc;
		this.setMultiVisibility();
		this.setTileVisibility(player);
	}

	/**
	 * draw the factor
	 */
	private void setMultiVisibility() {
		boolean visible = !(this.loc.hasSpecialTile() || (!(this.loc.isEmpty())));
		this.multiplier.setVisible(visible);
	}

	/**
	 * draw tile on the location
	 * 
	 * @param player
	 *            currentPlayer
	 */
	private void setTileVisibility(Player player) {
		if (!(this.loc.isEmpty())) {
			this.tile = new TileGUI(this.loc.getTile());
			this.tile.setOpaque(true);
			this.add(this.tile);
		} else if (this.loc.hasSpecialTile()) {
			// player can only see his own special tile
			if (this.loc.getSpecialTile().getOwner().getName()
					.equals(player.getName())) {
				this.specialTile = new SpecialTileGUI(this.loc.getSpecialTile());
				this.specialTile.setOpaque(true);
				this.add(this.specialTile);
			}
		}
	}
}
