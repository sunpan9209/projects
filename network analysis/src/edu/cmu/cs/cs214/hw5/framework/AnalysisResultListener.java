package edu.cmu.cs.cs214.hw5.framework;

import javax.swing.JPanel;

/**
 * The interface of an analysis result listener
 * 
 * @author weisiyu
 * 
 */

public interface AnalysisResultListener {

	/**
	 * Perform an action when an analysis result arrives
	 * 
	 * @param panel
	 *            the result in the format of a JPanel
	 * @param name
	 *            the name of the analysis result
	 */
	public void onAnalysisResultArrive(JPanel panel, String name);
}
