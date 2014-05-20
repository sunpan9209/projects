package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cmu.cs.cs214.hw4.core.Game;

public class PlayGUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6173250201610930781L;
	private JButton playButton;
	private Game game;
	private JTextField numPlayers;
	private static final String ERROR_NUMBER = " only 2-4 players allowed.";
	private static final String ERROR_INVALID = "Invalid Number";

	/**
	 * welcome interface
	 */
	public PlayGUI() {
		init();
	}

	private void init() {
		game = new Game();
		setTitle("Welcome!");
		setBounds(100, 100, 300, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));
		setResizable(false);
		setLocation(500, 100);

		JPanel panelBox = new JPanel();
		add(panelBox, BorderLayout.NORTH);
		panelBox.setLayout(new GridLayout(0, 2, 0, 0));

		// input
		JPanel panelNumPlayers = new JPanel();
		panelBox.add(panelNumPlayers);
		panelNumPlayers.setLayout(new BorderLayout(0, 0));
		JLabel number = new JLabel("Number of Players");
		panelNumPlayers.add(number, BorderLayout.EAST);

		// only 2-4 players per game
		JPanel panelInput = new JPanel();
		panelBox.add(panelInput);
		numPlayers = new JTextField();
		panelInput.add(numPlayers);
		numPlayers.setMaximumSize(new Dimension(2147483647, 28));
		numPlayers.setToolTipText("between 2 and 4");
		numPlayers.setColumns(10);

		JPanel panelEmpty = new JPanel();
		add(panelEmpty, BorderLayout.CENTER);

		JPanel panelButton = new JPanel();
		add(panelButton, BorderLayout.SOUTH);

		playButton = new JButton("Play");
		playButton.addActionListener(this);
		panelButton.add(playButton);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playButton) {
			int num;
			try {
				num = Integer.parseInt(numPlayers.getText());
				if (num < 2 || num > 4) {
					JOptionPane.showMessageDialog(null, ERROR_NUMBER, "Error",
							JOptionPane.PLAIN_MESSAGE);
					return;
				}
				setVisible(false);
				game.initPlayers(num);
				new PlayerAddGUI(game, num);
			} catch (NumberFormatException exp) {
				JOptionPane.showMessageDialog(null, ERROR_INVALID, "Error",
						JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
}
