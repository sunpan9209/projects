package edu.cmu.cs.cs214.hw5.framework;


/**
 * The interface on an data listener
 * 
 * @author weisiyu
 * 
 */

public interface DataListener {

	/**
	 * Perform action when data arrives
	 * 
	 * @param data
	 *            the arriving data
	 */
	public void onDataArrive(UserData data);

	/**
	 * Handle the exceptions happen in data query
	 */
	public void onExceptionHappen();
}
