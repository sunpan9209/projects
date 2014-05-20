package edu.cmu.cs.cs214.hw5.framework;

import javax.swing.JPanel;

/**
 * The interface define the methods for a listener of the framework core, which
 * is the GUI in this case
 * 
 * @author weisiyu
 * 
 */

public interface StatusChangeListener {

	/**
	 * Act when new analysis result is available
	 * 
	 * @param result
	 *            An analysis result
	 */
	public void onNewAnalysisResult(JPanel result, String name);

	/**
	 * Act when new analysis plugin is registered
	 * 
	 * @param plugin
	 *            The plugin that is registered
	 */
	public void onNewAnalysisPluginRegistered(AnalysisPlugin plugin);

	/**
	 * Act when new data plugin is registered
	 * 
	 * @param plugin
	 */
	public void onNewDataPluginRegistered(DataPlugin plugin);

	/**
	 * Handle when an exception happens
	 */
	public void onExceptionHappen();

}
