package edu.cmu.cs.cs214.hw5.framework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class FrameworkGUI implements StatusChangeListener {

	private static final String TITLE = "Social Network Analysis";
	private static final String SOURCE_SELECT = "Choose Social Network";
	private static final String USER_ID = "User id of the user you want to analyze:";
	private static final String START = "Start Analyze";
	private static final String NO_ID = "Please specify a user id first";
	private static final String NO_SOURCE = "Please select a social network to start";
	private static final String RUNNING = "The analysis has started";
	private static final String INFORMATION = "Information :";
	private static final String RESULT = "See the analyses results";
	private static final String EXCEPTION = "Exception, wrong user ID or reaching rate limit";

	private JFrame frame;

	private JMenuBar bar;

	private JPanel mainPanel;

	private JMenu sourceMenu;

	private JTextArea userID;

	private JTextArea information;

	private JButton start;

	private FrameworkImpl core;

	private JTabbedPane tabs;

	private boolean firstResultArrived; // a marker

	public FrameworkGUI(FrameworkImpl impl) {

		firstResultArrived = false;

		core = impl;

		frame = new JFrame(TITLE);

		mainPanel = new JPanel();

		bar = new JMenuBar();
		sourceMenu = new JMenu(SOURCE_SELECT);
		sourceMenu.setMnemonic(KeyEvent.VK_F);
		bar.add(sourceMenu);

		frame.setJMenuBar(bar);

		mainPanel.add(new JLabel(INFORMATION));
		information = new JTextArea(1, 30);
		information.setEditable(false);
		mainPanel.add(information);

		mainPanel.add(new JLabel(USER_ID));
		userID = new JTextArea(1, 20);
		userID.setEditable(true);
		mainPanel.add(userID);

		start = new JButton(START);
		mainPanel.add(start);

		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (core.getCurrentSource() == null) {
					information.setText(NO_SOURCE);
					return;
				}
				if (userID.getText().length() <= 0) {
					information.setText(NO_ID);
					return;
				}
				information.setText(RUNNING);
				core.startAnalyze(userID.getText());
			}

		});

		tabs = new JTabbedPane();
		mainPanel.add(tabs);

		frame.setContentPane(mainPanel);
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	@Override
	public void onNewAnalysisResult(JPanel result, String name) {

		// if first result arrived, clear the empty tabs initialized
		if (firstResultArrived == false) {
			firstResultArrived = true;
			tabs.removeAll();
		}

		information.setText(RESULT);
		tabs.addTab(name, result);
	}

	@Override
	public void onNewAnalysisPluginRegistered(AnalysisPlugin plugin) {

		// allocate space for analysis plugins
		JPanel emptyPanel = new JPanel();
		JTextArea emptyArea = new JTextArea(15, 40);
		emptyArea.setEditable(false);
		emptyPanel.add(emptyArea);
		tabs.add(plugin.getName(), emptyPanel);
	}

	@Override
	public void onNewDataPluginRegistered(DataPlugin plugin) {
		JMenuItem source = new JMenuItem(plugin.getdataSource());
		sourceMenu.add(source);
		source.setMnemonic(KeyEvent.VK_F);
		source.addActionListener(new DataSourceListener(plugin));
	}

	/**
	 * The class that listens to a user's action of choosing a data source
	 * 
	 * @author weisiyu
	 * 
	 */
	private class DataSourceListener implements ActionListener {
		private DataPlugin plugin;

		public DataSourceListener(DataPlugin plugin) {
			this.plugin = plugin;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			core.setCurrentSouce(plugin);
		}

	}

	@Override
	public void onExceptionHappen() {
		information.setText(EXCEPTION);
	}

}
