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

public class PlayerAddGUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6406670037792361638L;
	private JButton addButton;
	private Game game;
	private JTextField name;
	private int number;
	private static final String ERR_NAME = "You need to provide a valid name!";
	private static final String SAME_NAME = "no same name allowed!";

	/**
	 * add a player
	 * 
	 * @param game
	 * @param numPlayers
	 */
	public PlayerAddGUI(Game game, int numPlayers) {
		init(game);
		number = numPlayers;
	}

	private void init(Game game) {
		this.game = game;
		setTitle("Player");
		setBounds(100, 100, 300, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));
		setResizable(false);
		setLocation(500, 100);

		JPanel panelBox = new JPanel();
		add(panelBox, BorderLayout.NORTH);
		panelBox.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panelName = new JPanel();
		panelBox.add(panelName);
		panelName.setLayout(new BorderLayout(0, 0));
		JLabel nameLabel = new JLabel("Name");
		panelName.add(nameLabel, BorderLayout.EAST);

		JPanel panelInput = new JPanel();
		panelBox.add(panelInput);
		name = new JTextField();
		panelInput.add(name);
		name.setMaximumSize(new Dimension(2147483647, 28));
		name.setToolTipText("please type in your name");
		name.setColumns(10);

		JPanel panelEmpty = new JPanel();
		add(panelEmpty, BorderLayout.CENTER);

		JPanel panelButton = new JPanel();
		add(panelButton, BorderLayout.SOUTH);

		addButton = new JButton("Add");
		addButton.addActionListener(this);
		panelButton.add(addButton);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			// add a player with name
			if (game.getNumPlayers() < number) {
				// input field should not be empty
				if (name.getText().equals("")) {
					JOptionPane.showMessageDialog(new JFrame(), ERR_NAME,
							"Error", JOptionPane.ERROR_MESSAGE);
					name.requestFocus();
					return;
				}
				// no same name allowed
				if (!(game.addPlayer(name.getText()))) {
					JOptionPane.showMessageDialog(new JFrame(), SAME_NAME,
							"Error", JOptionPane.ERROR_MESSAGE);
					name.requestFocus();
					return;
				}
				// if enough players are added, start the game
				if (game.getNumPlayers() == number) {
					setVisible(false);
					gameStart();
					return;
				}
				name.setText("");
				return;
			}
		}
	}

	private void gameStart() {
		new GameGUI(game);
	}
}
