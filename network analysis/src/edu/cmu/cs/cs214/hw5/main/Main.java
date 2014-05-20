package edu.cmu.cs.cs214.hw5.main;

import edu.cmu.cs.cs214.hw5.framework.*;
import edu.cmu.cs.cs214.hw5.plugin.*;

public class Main {

	public static void main(String[] args) {
		FrameworkImpl core = new FrameworkImpl();
		FrameworkGUI Gui = new FrameworkGUI(core);

		core.setListener(Gui);

		DataPlugin git = new GithubDataPlugin();
		core.registerDataPlugin(git);

		DataPlugin twitter = new TwitterDataPlugin();
		core.registerDataPlugin(twitter);

		AnalysisPlugin timeAnalysis = new PostsByTimePlugin();
		core.registerAnalysisPlugin(timeAnalysis);

		AnalysisPlugin rateAnalysis = new ShareRateByTimePlugin();
		core.registerAnalysisPlugin(rateAnalysis);

		AnalysisPlugin keyWordsAnalysis = new KeyWordPlugin();
		core.registerAnalysisPlugin(keyWordsAnalysis);
	}
}
