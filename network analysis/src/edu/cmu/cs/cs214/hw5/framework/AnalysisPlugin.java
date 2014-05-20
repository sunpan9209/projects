package edu.cmu.cs.cs214.hw5.framework;


/**
 * The interface of an analysis plugin which defines the method an analysis
 * plugin is supposed to provide
 * 
 * @author weisiyu
 * 
 */
public interface AnalysisPlugin {

	/**
	 * Let the analysis plugin begin analyze with the given data
	 * 
	 * @param data
	 */
	public void analyze(UserData userData);

	/**
	 * Get the name of a analysis plugin
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Get the analysis result from the analysis plugin
	 * 
	 * @return
	 */
	public void setAnalysisResultListener(AnalysisResultListener listener);
}
