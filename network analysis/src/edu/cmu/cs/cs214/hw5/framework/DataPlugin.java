package edu.cmu.cs.cs214.hw5.framework;


/**
 * The interface for a data plugin
 * 
 * @author weisiyu
 * 
 */
public interface DataPlugin {
	/**
	 * Let the framework know the data source of the plugin
	 * 
	 * @return
	 */
	public String getdataSource();

	/**
	 * Ask the plugin to get data from source
	 * 
	 * @return
	 */
	public UserData getDataFromSource(String userID);

	/**
	 * Set the listener for the arriving data
	 * 
	 * @param listener
	 */
	public void setDataArriveListener(DataListener listener);
}
