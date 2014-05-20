package edu.cmu.cs.cs214.hw5.framework;

import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * The implementation of the framework core, which is the controller part in the
 * MVC pattern, coordinate between data, plugin and view
 * 
 * @author weisiyu
 * 
 */

public class FrameworkImpl implements AnalysisResultListener, DataListener {

	private ArrayList<DataPlugin> dataPlugins;
	private ArrayList<AnalysisPlugin> analysisPlugins;
	private StatusChangeListener listener;
	private UserData data;
	private DataPlugin currentSource;

	public FrameworkImpl() {
		dataPlugins = new ArrayList<DataPlugin>();
		analysisPlugins = new ArrayList<AnalysisPlugin>();
	}

	/**
	 * Register a data plugin
	 * 
	 * @param plugin
	 *            the plugin that is registered
	 */
	public void registerDataPlugin(DataPlugin plugin) {
		dataPlugins.add(plugin);
		plugin.setDataArriveListener(this);
		notifyNewDataPlugin(plugin);
	}

	/**
	 * Set a listener to the changes, which is the GUI in this case
	 * 
	 * @param listener
	 *            the observer of the changes
	 */
	public void setListener(StatusChangeListener listener) {
		this.listener = listener;
	}

	/**
	 * Register an analysis plugin
	 * 
	 * @param plugin
	 *            the plugin being registered
	 */
	public void registerAnalysisPlugin(AnalysisPlugin plugin) {
		analysisPlugins.add(plugin);
		plugin.setAnalysisResultListener(this);
		notifyNewAnalysisPlugin(plugin);
	}

	/**
	 * Start an analysis process by asking the data plugins to retrieve data
	 * 
	 * @param userID
	 *            The user id the analysis is to be performed on
	 */
	public void startAnalyze(String userID) {
		currentSource.getDataFromSource(userID);
	}

	/**
	 * Get the current source chosen by the framework user
	 * 
	 * @return the current source
	 */
	public DataPlugin getCurrentSource() {
		return currentSource;
	}

	public void setCurrentSouce(DataPlugin plugin) {
		currentSource = plugin;
	}

	/**
	 * Helper method, notify the listener that new data plugin is registered
	 * 
	 * @param plugin
	 */
	private void notifyNewDataPlugin(DataPlugin plugin) {
		listener.onNewDataPluginRegistered(plugin);
	}

	/**
	 * Notify the listener that new analysis plugin is registered
	 * 
	 * @param plugin
	 */
	private void notifyNewAnalysisPlugin(AnalysisPlugin plugin) {
		listener.onNewAnalysisPluginRegistered(plugin);
	}

	@Override
	public void onAnalysisResultArrive(JPanel panel, String name) {
		listener.onNewAnalysisResult(panel, name);
	}

	@Override
	public void onDataArrive(UserData userData) {
		this.data = userData;
		for (AnalysisPlugin plugin : analysisPlugins) {
			plugin.analyze(data);
		}
	}

	@Override
	public void onExceptionHappen() {
		listener.onExceptionHappen();
	}

}
